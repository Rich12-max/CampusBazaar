if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" }
$srcFiles = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
javac -d bin $srcFiles
if ($LASTEXITCODE -eq 0) {
    Copy-Item -Path "src/main/resources/web/index.html" -Destination "index.html" -Force
    if (!(Test-Path "public")) { New-Item -ItemType Directory -Path "public" }
    Copy-Item -Path "src/main/resources/web/index.html" -Destination "public/index.html" -Force
    Write-Host "[SUCCESS] All Java classes compiled successfully into bin/ and web assets synced."
} else {
    Write-Host "[ERROR] Compilation failed"
}

