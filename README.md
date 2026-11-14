# CodeJudge

**A lightweight C program testing and static analysis tool, written in C.**

Built for Software Project Lab 1 (SPL1) at the Institute of Information Technology (IIT), University of Dhaka.

CodeJudge is a **terminal application** that runs on your own machine. It compiles a C source file, runs it against test cases you provide, compares the output with the expected output, and gives a quick static analysis of the source code. It is inspired by online judges, but it is **not** a web platform: there are no user accounts, no problem set, and no server.

## Features

**Testing**
- Compiles a C file with GCC and shows compilation errors (execution is skipped if compilation fails).
- Runs multiple test cases per program. Input and expected output can be typed in or read from files.
- Compares the actual output with the expected output and shows the difference when they do not match.
- Stops programs that run longer than 10 seconds (protection against infinite loops).
- Shows a result summary: total, passed, failed, and pass percentage, plus per-test details.

**Static analysis**
- **Cyclomatic complexity:** counts decision points (`if`, `while`, `for`, `case`, `&&`, `||`).
- **Cognitive complexity and nesting depth:** estimates how hard the code is to read.
- **Buffer overflow risk:** finds fixed-size `char` buffers and unsafe calls (`gets`, `strcpy`, `strcat`, `sprintf`), tracks simple cases where user input flows into them, and reports a risk score with a LOW / MEDIUM / HIGH level.

**Sessions**
- Load several source files in one run, switch between them, and keep a separate test history for each.

## Limitations

Please read these before relying on the results.

- **The analysis is heuristic.** It reads the source as text and matches keywords and patterns. It does not parse C, so it can miss problems (false negatives) or flag harmless code (false positives). Treat the numbers as a rough guide, not a verdict.
- **Output comparison is an exact text match.** There is no tolerance for extra whitespace or for floating-point differences.
- **Only a time limit is enforced.** There is no memory limit and no sandbox. Programs are run directly on your machine, so **do not run code you do not trust.**
- **Single-user and local only.** It tests one program at a time, from the terminal.
- **Needs the `timeout` command**, so it works on Linux and WSL. On macOS, install GNU coreutils (`brew install coreutils`) and make `timeout` available.

## Requirements

- GCC
- Linux, WSL, or macOS with `timeout` available

## Build

From the project root:

```bash
gcc src/codejudge.c src/Testing.c src/cognitive.c src/cyclomatic.c src/metrics.c src/mathlib.c src/output_com.c src/Buffer_overflow.c -o codejudge
```

## Run

```bash
./codejudge
```

Enter the path to the C file you want to test, then use the menu:

```
=================== MENU ===================
1. Run Test Case
2. View Test Results
3. Analyze Code Metrics
4. Advanced Metrics
5. Buffer Overflow Risk Analysis
6. Add New File
7. Switch File
8. Exit
============================================
```

## Try it

The `demo/` folder has sample programs with matching input and expected-output files.

1. Run `./codejudge` and enter `demo/sort.c` as the file path.
2. Choose **1. Run Test Case**, pick **File** for the input, and give `demo/sortin1.txt`.
3. Pick **File** for the expected output and give `demo/sortout1.txt`.
4. Choose **2** to see the result, and **3**, **4** or **5** to see the analysis.

The `test/` folder has small programs for edge cases: a compile error (`ce.c`), a runtime error (`re1.c`), and an infinite loop (`timeout.c`). The `t/` folder has larger sample programs (Dijkstra, KMP, Fenwick tree) and an unsafe-buffer example (`buff.c`) for the overflow check.

## Project structure

```
CodeJudge/
├── src/
│   ├── codejudge.c        # main program and menu
│   ├── Testing.c / .h     # compile, run, compare, results
│   ├── cyclomatic.c       # cyclomatic complexity
│   ├── cognitive.c        # cognitive complexity and nesting
│   ├── Buffer_overflow.c  # buffer overflow risk analysis
│   ├── metrics.c / .h     # shared metrics structures
│   ├── mathlib.c / .h     # helper functions
│   └── output_com.c       # output display and diff
├── demo/                  # sample programs with input/output files
├── test/                  # edge-case programs
├── t/                     # larger sample programs
├── Presentation/          # mid-project presentation
└── README.md
```

## Concepts used

Dynamic memory (`malloc`, `realloc`, `free`), file handling, process execution with `system()`, text-based static analysis, simple taint tracking, and struct-based design in C.

## Author

**Fahmida Munni**, BSSE, IIT, University of Dhaka

## Supervisor

**Md. Saeed Siddik**, Assistant Professor, IIT, University of Dhaka
