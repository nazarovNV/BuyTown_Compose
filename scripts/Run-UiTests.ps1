[CmdletBinding()]
param(
    [string]$Device,
    [string]$JavaHome,
    [ValidatePattern('^[A-Za-z0-9_.$#]+$')][string]$TestClass,
    [ValidateRange(0, 2000)][int]$DelayMs = 0,
    [switch]$NoBuild
)

$ErrorActionPreference = 'Stop'
$projectDirectory = Split-Path $PSScriptRoot -Parent
$previousJavaHome = $env:JAVA_HOME
Push-Location $projectDirectory
try {
    if ($JavaHome) {
        $env:JAVA_HOME = $JavaHome
    } elseif (Test-Path '.gradle/config.properties') {
        $javaLine = Get-Content '.gradle/config.properties' | Where-Object { $_ -match '^java\.home=' } | Select-Object -First 1
        if ($javaLine) { $env:JAVA_HOME = ($javaLine -replace '^java\.home=', '').Replace('\:', ':').Replace('\\', '\') }
    }
    if (-not $NoBuild) {
        & '.\gradlew.bat' ':app:assembleDebug' ':app:assembleDebugAndroidTest'
        if ($LASTEXITCODE -ne 0) { throw 'Gradle build failed.' }
    }
    $sdkLine = Get-Content 'local.properties' | Where-Object { $_ -match '^sdk\.dir=' } | Select-Object -First 1
    if (-not $sdkLine) { throw 'Set sdk.dir in local.properties before running UI tests.' }
    $sdkDirectory = ($sdkLine -replace '^sdk\.dir=', '').Replace('\:', ':').Replace('\\', '\')
    $adb = Join-Path $sdkDirectory 'platform-tools/adb.exe'
    if (-not (Test-Path -LiteralPath $adb)) { throw "adb.exe was not found: $adb" }
    $deviceOutput = & $adb devices
    if ($LASTEXITCODE -ne 0) { throw 'Could not list Android devices.' }
    $readyDevices = @($deviceOutput | Where-Object { $_ -match '^\S+\s+device\s*$' } | ForEach-Object { ($_ -split '\s+')[0] })
    if (-not $Device) {
        if ($readyDevices.Count -ne 1) { throw 'Start one emulator, or select a device with -Device emulator-5554.' }
        $Device = $readyDevices[0]
    }
    if ($Device -notin $readyDevices) { throw "Device $Device is not connected or authorized." }
    $applicationApk = 'app/build/outputs/apk/debug/app-debug.apk'
    $testApk = 'app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk'
    foreach ($apk in @($applicationApk, $testApk)) {
        if (-not (Test-Path -LiteralPath $apk)) { throw "Missing APK: $apk. Run without -NoBuild." }
        & $adb -s $Device install -r $apk
        if ($LASTEXITCODE -ne 0) { throw "Could not install $apk" }
    }
    $runnerArguments = @('-s', $Device, 'shell', 'am', 'instrument', '-w', '-e', 'demoDelayMs', "$DelayMs")
    if ($TestClass) {
        if (-not $TestClass.StartsWith('com.')) { $TestClass = "com.example.buytowncompose.ui.$TestClass" }
        $runnerArguments += @('-e', 'class', $TestClass)
    } else {
        $runnerArguments += @('-e', 'package', 'com.example.buytowncompose.ui')
    }
    $runnerArguments += 'com.example.buytowncompose.test/androidx.test.runner.AndroidJUnitRunner'
    $reportDirectory = Join-Path $projectDirectory 'app/build/reports/ui-tests'
    New-Item -ItemType Directory -Force $reportDirectory | Out-Null
    $logPath = Join-Path $reportDirectory 'latest.log'
    Write-Host "Running UI tests on $Device. Watch the emulator. Step delay: $DelayMs ms."
    $result = & $adb @runnerArguments | Tee-Object -FilePath $logPath | ForEach-Object {
        Write-Host $_
        $_
    }
    $runnerExitCode = $LASTEXITCODE
    $log = $result -join [Environment]::NewLine
    $html = '<!doctype html><meta charset="utf-8"><title>BuyTown UI tests</title><h1>BuyTown UI tests</h1><pre>' + [System.Net.WebUtility]::HtmlEncode($log) + '</pre>'
    $html | Set-Content (Join-Path $reportDirectory 'index.html') -Encoding UTF8
    if ($runnerExitCode -ne 0 -or $log -match 'FAILURES!!!|INSTRUMENTATION_FAILED|Process crashed|shortMsg=' -or $log -notmatch 'OK \([1-9][0-9]* tests?\)') {
        throw "UI tests failed or did not complete. Report: $logPath"
    }
    Write-Host "UI tests passed. Report: $reportDirectory/index.html"
} finally {
    $env:JAVA_HOME = $previousJavaHome
    Pop-Location
}
