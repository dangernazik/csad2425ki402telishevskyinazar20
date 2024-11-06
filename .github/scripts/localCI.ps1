param(
    [string]$ComPort = "COM3",  # Значення за замовчуванням для локального запуску
    [int]$BaudRate = 9600       # Типова швидкість, яка може використовуватися при потребі
)

# Set the error action preference to stop on errors
$ErrorActionPreference = "Stop"

Write-Host "Starting CI process for Java and Arduino..."

$ROOT_PROJECT_PATH = "D:\4 kurs\APKS\csad2425ki402telishevskyinazar20"
$CLIENT_PROJECT_PATH = "$ROOT_PROJECT_PATH\client\RPS-client"
$SERVER_PROJECT_PATH = "$ROOT_PROJECT_PATH\server\RPS-server"
$DEPLOY_DIR = "$ROOT_PROJECT_PATH\deploy"

# Create deploy directory if it doesn't exist
if (-Not (Test-Path $DEPLOY_DIR)) {
    New-Item -ItemType Directory -Path $DEPLOY_DIR
}

Write-Host "Current directory: $(Get-Location)"
Write-Host "Deploy directory exists: $(Test-Path $DEPLOY_DIR)"

Write-Host "=== Client Application Build and Test ==="

Set-Location $CLIENT_PROJECT_PATH

Write-Host "Compiling Client project..."
& mvn clean compile -B

Write-Host "Running tests..."
& mvn test

$TEST_RESULTS_DIR = "target\surefire-reports"
Write-Host "Copying test results to $DEPLOY_DIR\test-results..."
Copy-Item -Path $TEST_RESULTS_DIR -Destination "$DEPLOY_DIR\tests" -Recurse -Force

Write-Host "Packaging JAR..."
& mvn package

Write-Host "Copying JAR artifact... to $DEPLOY_DIR..."
Copy-Item -Path "target\*.jar" -Destination $DEPLOY_DIR

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