@echo off
echo ===================================================
echo Compiling Campus Peer-to-Peer Marketplace (Java)
echo ===================================================
if not exist bin mkdir bin

echo Gathering source files...
dir /s /B src\main\java\*.java > sources.txt

echo Running javac compiler...
javac -d bin @sources.txt
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Build completed successfully into bin/ directory!
) else (
    echo [ERROR] Compilation failed. Please check Java errors above.
)
del sources.txt

