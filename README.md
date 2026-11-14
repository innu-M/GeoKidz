# GeoKidz

**A geography quiz game for kids, built with Java Swing.**

GeoKidz helps children learn about countries through flags, capitals, currencies, and languages. Players create an account, learn first in a study mode, then play quiz levels that unlock as their scores improve. A leaderboard shows how everyone is doing.

It is a desktop application that runs locally. There is no server or internet connection needed.

## Features

- **Learning mode:** browse countries and study their flag, capital, currency, and language before playing.
- **Five quiz levels:**
  - Level 1: flags
  - Level 2: capitals
  - Level 3: currencies
  - Level 4: languages
  - Level 5: mastery, with a random mix of all four question types
- **Level unlocking:** each level has 10 questions. Score **7 or more** to unlock the next level.
- **Accounts:** register and log in. Each player's level scores and progress are saved.
- **Leaderboard:** sort players by total score or by a single category (flags, capitals, currencies, languages) or by name.
- **Sound:** background music with a mute toggle, plus sounds for clicks and for correct and wrong answers.
- **Data:** 73 countries with a flag image for each, stored in `countries.json`.

## Built with

- Java (Swing for the interface)
- [Gson](https://github.com/google/gson) for saving user data as JSON
- MVC-style structure: `model`, `view`, and `controller` packages

## Requirements

- JDK 11 or newer
- `gson-2.10.1.jar` (already included in this repository)

## How to run

From the project root, compile and copy the resources (images, audio, data) next to the compiled classes.

**Linux / macOS**

```bash
mkdir -p out
javac -cp gson-2.10.1.jar -d out $(find src/main/java -name "*.java")
cp -r src/main/resources/* out/
java -cp out:gson-2.10.1.jar com.countryquiz.Main
```

**Windows (PowerShell)**

```powershell
mkdir out
javac -cp gson-2.10.1.jar -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
Copy-Item -Recurse src/main/resources/* out/
java -cp "out;gson-2.10.1.jar" com.countryquiz.Main
```

You can also open the project in IntelliJ IDEA, add `gson-2.10.1.jar` as a library, and run `com.countryquiz.Main`.

Run it from the project root: the app reads and writes `users.txt` in the folder you start it from.

## How to play

1. Register a new account and log in.
2. Open **Learn** to study the countries.
3. Pick **Level 1** and answer the questions.
4. Score 7 or more to unlock the next level.
5. Check the **Leaderboard** to compare scores.

## Project structure

```
GeoKidz/
├── src/main/java/com/countryquiz/
│   ├── Main.java               # window and screen navigation
│   ├── model/                  # Country, User, UserDatabase
│   ├── view/
│   │   ├── panel/              # login, register, menu, learning, levels, quiz, leaderboard
│   │   └── components/         # buttons and background panels
│   └── controller/             # GameController (progress), AudioController (sound)
├── src/main/resources/
│   ├── data/countries.json
│   ├── images/                 # interface images and country flags
│   └── audio/                  # music and sound effects
└── gson-2.10.1.jar
```

## Known limitations

- **Passwords are stored in plain text** in `users.txt`. This is fine for a classroom demo on one computer, but do not use a real password.
- Player data is saved to a local file, so progress stays on the computer where the game was played.
- Questions are picked at random, so a country can appear more than once in the same level.

## Author

**Fahmida Munni**, BSSE, Institute of Information Technology, University of Dhaka
