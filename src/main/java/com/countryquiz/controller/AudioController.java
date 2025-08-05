package com.countryquiz.controller;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AudioController {
    private Clip backgroundMusic;
    private static final Map<String, Clip> soundEffects = new HashMap<>();
    private boolean musicEnabled = true;
    private static boolean soundEffectsEnabled = true;
    private float volume = 0.7f;

    public AudioController() {
        initializeAudio();
    }

    private void initializeAudio() {
        try {
            loadBackgroundMusic();
            loadSoundEffects();
            System.out.println("Audio initialized successfully");
        } catch (Exception e) {
            System.err.println("Audio initialization error: " + e.getMessage());
            initializeFallbackAudio();
        }
    }

    private void initializeFallbackAudio() {
        try {
            backgroundMusic = AudioSystem.getClip();
            soundEffects.put("correct", AudioSystem.getClip());
            soundEffects.put("wrong", AudioSystem.getClip());
            soundEffects.put("click", AudioSystem.getClip());
        } catch (LineUnavailableException e) {
            System.err.println("Failed to initialize fallback audio: " + e.getMessage());
        }
    }

    private void loadBackgroundMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                Objects.requireNonNull(getClass().getResource("/audio/music.wav")));
        backgroundMusic = AudioSystem.getClip();
        backgroundMusic.open(audioStream);
        setVolume(backgroundMusic, volume);
    }

    private void setVolume(Clip backgroundMusic, float volume) {
        FloatControl volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (20 * Math.log10(volume));
        volumeControl.setValue(dB);
        System.out.println("Volume set to: " + volume + " (dB: " + dB + ")");
    }

    private void loadSoundEffects() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        soundEffects.put("correct", loadClip("/audio/correct.wav"));
        soundEffects.put("wrong", loadClip("/audio/wrong.wav"));
        soundEffects.put("click", loadClip("/audio/click.wav"));
    }

    private Clip loadClip(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                Objects.requireNonNull(getClass().getResource(path)));
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        setVolume(clip, volume);
        return clip;
    }

    public void playMusic() {
        if (musicEnabled && backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.setFramePosition(0);
        }
    }

    public static void playSoundEffect(String effectName) {
        try {
            if (soundEffectsEnabled && soundEffects.containsKey(effectName)) {
                Clip clip = soundEffects.get(effectName);
                if (clip != null) {
                    clip.setFramePosition(0);
                    clip.start();
                    System.out.println("Played: " + effectName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error playing " + effectName + ": " + e.getMessage());
        }
    }
    public void toggleMusic() {
        musicEnabled = !musicEnabled;
        if (musicEnabled) {
            playMusic();
        } else {
            stopMusic();
        }
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }
}