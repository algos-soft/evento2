@echo OFF

echo Waiting tor Tomcat shutwown...
:LOOP

timeout /t 1

netstat -o -n -a | findstr /i 8080 | findstr /i "listening"

if %ERRORLEVEL% equ 0 goto LOOP

REM wait another second for the other tomcat ports to be closed
timeout /t 1

echo Tomcat is stopped

