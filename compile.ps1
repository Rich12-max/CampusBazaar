if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" }
$srcFiles = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
javac -d bin -cp "lib/*" $srcFiles
if ($LASTEXITCODE -eq 0) {
    if (!(Test-Path "bin/styles")) { New-Item -ItemType Directory -Path "bin/styles" }
    Copy-Item -Path "src/main/resources/styles/*" -Destination "bin/styles/" -Force
    Copy-Item -Path "src/main/resources/web/index.html" -Destination "index.html" -Force
    if (!(Test-Path "public")) { New-Item -ItemType Directory -Path "public" }
    Copy-Item -Path "src/main/resources/web/index.html" -Destination "public/index.html" -Force
    Write-Host "[SUCCESS] All Java & JavaFX classes compiled successfully into bin/ and resources synchronized."
} else {
    Write-Host "[ERROR] Compilation failed."
}
