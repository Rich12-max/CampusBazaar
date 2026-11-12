if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" }
$srcFiles = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
javac -d bin $srcFiles
if ($LASTEXITCODE -eq 0) {
    Write-Host "[SUCCESS] All Java classes compiled successfully into bin/"
} else {
    Write-Host "[ERROR] Compilation failed"
}

