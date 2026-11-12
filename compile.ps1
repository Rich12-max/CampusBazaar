if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
$srcFiles = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { '"' + $_.FullName.Replace('\', '/') + '"' }
$srcFiles | Out-File -FilePath "sources.txt" -Encoding ascii
javac -d bin -cp "lib/*" "@sources.txt"
$exitCode = $LASTEXITCODE
Remove-Item -Path "sources.txt" -ErrorAction SilentlyContinue
if ($exitCode -eq 0) {
    if (!(Test-Path "bin/styles")) { New-Item -ItemType Directory -Path "bin/styles" | Out-Null }
    Copy-Item -Path "src/main/resources/styles/*" -Destination "bin/styles/" -Force
    Write-Host "[SUCCESS] All Java & JavaFX classes compiled successfully into bin/."
} else {
    Write-Host "[ERROR] Compilation failed with exit code $exitCode."
}
