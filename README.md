# Mutual Exclusion Cases

A Java project to demonstrate and test common mutual exclusion algorithms and their potential failures in concurrent environments.

## Algorithms Included

- **Lock Variable**: A simple software-based lock.
- **Strict Alternation**: Forcing threads to take turns.
- **Peterson's Solution**: A classic two-process mutual exclusion algorithm.

## Configuration

Settings are located in `Principal.java`:

- `mutualExclusionType`: Choose the algorithm to test.
- `numberOfIterations`: Set the number of test runs.
- `sleepInCriticalRegion`: Toggle a delay within the critical section to exacerbate race conditions.

## Purpose

This project serves as a practical demonstration of race conditions, atomicity issues, and the necessity of proper synchronization or memory visibility keywords (like `volatile`) in multi-threaded Java applications.
