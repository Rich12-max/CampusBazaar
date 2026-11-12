@echo off
echo =========================================================================
echo   CAMPUSBAZAAR: CAMPUS PEER-TO-PEER MARKETPLACE (100%% JAVA & JAVAFX)
echo =========================================================================
if not exist bin (
    call build.bat
)
echo.
echo Launching Native JavaFX Campus Marketplace Application...
java --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp bin com.campusbazaar.CampusBazaarApp
pause
