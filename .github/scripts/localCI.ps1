param(
    [string]$ComPort = "COM3",
    [int]$BaudRate = 9600
)

$ErrorActionPreference = "Stop"
Write-Host "Starting CI process for Java and Arduino..."

# Визначаємо кореневий шлях проекту
if ($env:GITHUB_WORKSPACE) {
    # Якщо скрипт виконується на GitHub Actions
    $ROOT_PROJECT_PATH = $env:GITHUB_WORKSPACE
} else {
    # Локально піднімаємось на два рівні вище
    $ROOT_PROJECT_PATH = (Get-Location).Path | Split-Path -Parent | Split-Path -Parent
}

$CLIENT_PROJECT_PATH = Join-Path -Path $ROOT_PROJECT_PATH -ChildPath "client\RPS-client"
$SERVER_PROJECT_PATH = Join-Path -Path $ROOT_PROJECT_PATH -ChildPath "server\RPS-server"
$DEPLOY_DIR = Join-Path -Path $ROOT_PROJECT_PATH -ChildPath "deploy"

if (-Not (Test-Path $DEPLOY_DIR)) {
    New-Item -ItemType Directory -Path $DEPLOY_DIR
}

Write-Host "Current directory: $(Get-Location)"
Write-Host "Deploy directory exists: $(Test-Path $DEPLOY_DIR)"
Write-Host "=== Client Application Build and Test ==="

Set-Location $CLIENT_PROJECT_PATH
Write-Host "Compiling Client project..."
& mvn clean compile -q

Write-Host "Running tests..."
& mvn test

$TEST_RESULTS_DIR = "target\surefire-reports"
$TEST_RESULTS_DEST = Join-Path -Path $DEPLOY_DIR -ChildPath "tests"
Write-Host "Copying test results to $TEST_RESULTS_DEST..."
Copy-Item -Path $TEST_RESULTS_DIR -Destination $TEST_RESULTS_DEST -Recurse -Force

Write-Host "=== Generating Tests Statistics ==="
Write-Host "Running JaCoCo report generation..."
& mvn jacoco:report

Write-Host "Copying JaCoCo test statistics to $DEPLOY_DIR..."
$JACOCO_REPORT_PATH = "target/site/jacoco/index.html"
$JACOCO_DEST_PATH = Join-Path -Path $DEPLOY_DIR -ChildPath "test-results"
if (-Not (Test-Path $JACOCO_DEST_PATH)) {
    New-Item -ItemType Directory -Path $JACOCO_DEST_PATH
}
Copy-Item -Path $JACOCO_REPORT_PATH -Destination $JACOCO_DEST_PATH -Force

Set-Location (Join-Path -Path $CLIENT_PROJECT_PATH -ChildPath "../..")
Write-Host "Returned to root directory: $(Get-Location)"

Write-Host "=== Uploading Test Results ==="
if ($env:GITHUB_WORKSPACE) {
    Write-Host "Uploading artifact for GitHub Actions..."
    # Завантаження результатів через GitHub Actions
    Write-Output "Uploading test results artifact..."
    Write-Output "::set-output name=test_results::$TEST_RESULTS_DEST"
    Write-Output "::group::Uploading test-results"
    Write-Host "Uploading $TEST_RESULTS_DEST as test-results artifact..."
    $UploadCommand = "actions/upload-artifact@v3 --name test-results --path $TEST_RESULTS_DEST"
    & bash -c $UploadCommand
    Write-Output "::endgroup::"
} else {
    Write-Host "Local environment detected. Skipping artifact upload."
}
Set-Location $CLIENT_PROJECT_PATH
Write-Host "Packaging JAR..."
& mvn package -q -B

Write-Host "Copying JAR artifact to $DEPLOY_DIR..."
Copy-Item -Path "target\*.jar" -Destination $DEPLOY_DIR

Set-Location -Path "..\.."

if ($env:GITHUB_ACTIONS) {
    Write-Host "Running in GitHub Actions environment. Skipping Doxygen documentation generation."
} else {
    Write-Host "=== Doxygen documentation generation ==="
    Write-Host "Generating documentation..."
    doxygen Doxyfile
}

Set-Location $ROOT_PROJECT_PATH
Write-Host "=== Server Application Build ==="
Write-Host "Compiling Arduino project..."
& arduino-cli compile --fqbn arduino:avr:uno "$SERVER_PROJECT_PATH\RPS-server.ino"

if ($ComPort -ne "") {
    Write-Host "Uploading program to the board on port $ComPort with baud rate $BaudRate..."
    & arduino-cli upload -p $ComPort --fqbn arduino:avr:uno "$SERVER_PROJECT_PATH\RPS-server.ino"
} else {
    Write-Host "Skipping Arduino upload as COM port is not specified (GitHub CI environment)"
}

Write-Host "CI completed successfully."