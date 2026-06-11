# CSCI142-IP-Log-Analyzer

## Overview

The Log Analyzer is a Java program that is meant to process log files and identify failed login attempts. It groups failed attempts by IP address and provides summary information such as the number of failures, usernames involved, and the time range of the failed attempts. Submission for Project 2 of CSCI142 at Stetson University, Fall 2025.

## Features

-Reads log files
-Detects failed login attempts
-Groups failures by IP address
-Tracks unique usernames
-Counts total failed attempts

## How It Works

1. The program reads each log file line by line.
2. Each line is parsed into a `LogEntry` object.
3. Entries with a status of `FAIL` are processed.
4. Failed attempts are grouped by IP address.
5. Data is stored and updated for each IP address.
6. A running total of all failed login attempts is kept.
