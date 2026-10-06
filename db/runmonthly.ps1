while ($true) {

Write-Host "========================================="
Write-Host "Running insertmonthlycontributions.php"
Write-Host "Time: $(Get-Date)"
Write-Host "========================================="

$php = "C:\phpx64\php.exe"
$script = "C:\angula17\auth-service\auth-service\db\insertmonthlycontributions.php"

if (Test-Path $php) {
    if (Test-Path $script) {

        & $php $script

        Write-Host ""
        Write-Host "PHP exit code: $LASTEXITCODE"
        Write-Host "Script finished."

    } else {
        Write-Host "ERROR: PHP script not found:"
        Write-Host $script
    }
} else {
    Write-Host "ERROR: PHP executable not found:"
    Write-Host $php
}

Write-Host ""
Write-Host "Waiting 60 seconds..."
Write-Host ""

Start-Sleep -Seconds 60

}
