@echo off
title FitTrack - Online Fitness Tracking System
echo =========================================================================
echo               FITTRACK - Online Fitness Tracking System
echo       Full-Stack Java Web Application (Spring Boot + Thymeleaf + JPA)
echo   Group: Ansh (Leader), Aryan Bhardwaj, Aryaman Singh
echo =========================================================================
echo.
echo Starting Spring Boot Application on http://localhost:8080 ...
echo Preloaded Accounts:
echo   - Admin: admin@fittrack.com / admin123
echo   - User:  ansh@fittrack.com / password123
echo.

where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    set "MAVEN_CMD=mvn"
) else (
    set "MAVEN_CMD=C:\Users\LOQ\.gemini\antigravity\scratch\tools\apache-maven-3.9.9\bin\mvn.cmd"
)

"%MAVEN_CMD%" spring-boot:run
pause
