@echo off
echo =========================================================================
echo Starting Campus Peer-to-Peer Marketplace on http://localhost:8080
echo =========================================================================
if not exist bin (
    call build.bat
)
start http://localhost:8080
java -cp bin com.campusbazaar.Main
pause

