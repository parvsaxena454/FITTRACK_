@echo off
title Push FitTrack to GitHub
echo =========================================================================
echo              FITTRACK - GitHub Upload Helper Script
echo =========================================================================
echo.
echo Make sure you have created an empty repository on GitHub first!
echo (e.g. https://github.com/your-username/fittrack.git)
echo.

set /p REPO_URL="Enter your GitHub Repository URL: "

if "%REPO_URL%"=="" (
    echo [ERROR] No repository URL entered. Exiting.
    pause
    exit /b 1
)

where git >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    set "GIT_CMD=git"
) else (
    set "GIT_CMD=C:\Users\LOQ\.gemini\antigravity\scratch\tools\mingit\cmd\git.exe"
)

echo.
echo [1/3] Setting remote origin to %REPO_URL% ...
"%GIT_CMD%" remote remove origin 2>nul
"%GIT_CMD%" remote add origin %REPO_URL%

echo [2/3] Setting branch to main ...
"%GIT_CMD%" branch -M main

echo [3/3] Pushing to GitHub ...
"%GIT_CMD%" push -u origin main

echo.
if %ERRORLEVEL% EQU 0 (
    echo =========================================================================
    echo [SUCCESS] Your FitTrack project has been successfully uploaded to GitHub!
    echo =========================================================================
) else (
    echo [NOTE] If prompted for authentication, enter your GitHub Personal Access Token (PAT).
)
echo.
pause
