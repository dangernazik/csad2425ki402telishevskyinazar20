param(
    [string]$ComPort = "COM3",
    [int]$BaudRate = 9600
)

# Зупиняємо виконання скрипту при виникненні помилок
$ErrorActionPreference = "Stop"

Write-Host "Starting CI process for Java and Arduino..."

# Встановлюємо кореневу директорію на поточний каталог, в якому запускається скрипт
$ROOT_PROJECT_PATH = (Get-Location).Path
$CLIENT_PROJECT_PATH = Join-Path -Path $ROOT_PROJECT_PATH -ChildPath "client\RPS-client"
$SERVER_PROJECT_PATH = Join-Path -Path $ROOT_PROJECT_PATH -ChildPath "server\RPS-server"
$DEPLOY_DIR = Join-Path -Path $ROOT_PROJECT_PATH -ChildPath "deploy"

# Створюємо директорію для розгортання, якщо вона не існує
if (-Not (Test-Path $DEPLOY_DIR)) {
    New-Item -ItemType Directory -Path $DEPLOY_DIR
}

Write-Host "Current directory: $(Get-Location)"
Write-Host "Deploy directory exists: $(Test-Path $DEPLOY_DIR)"

Write-Host "=== Client Application Build and Test ==="

# Переходимо в директорію клієнтського проекту
Set-Location $CLIENT_PROJECT_PATH

Write-Host "Compiling Client project..."
& mvn clean compile -B

Write-Host "Running tests..."
& mvn test

# Копіюємо результати тестів у директорію для розгортання
$TEST_RESULTS_DIR = "target\surefire-reports"
Write-Host "Copying test results to $DEPLOY_DIR\test-results..."
Copy-Item -Path $TEST_RESULTS_DIR -Destination "$DEPLOY_DIR\tests" -Recurse -Force

Write-Host "Packaging JAR..."
& mvn package

Write-Host "Copying JAR artifact to $DEPLOY_DIR..."
Copy-Item -Path "target\*.jar" -Destination $DEPLOY_DIR

# Повертаємось у кореневу директорію проекту
Set-Location $ROOT_PROJECT_PATH

Write-Host "=== Server Application Build ==="

# Компілюємо проект Arduino
Write-Host "Compiling Arduino project..."
& arduino-cli compile --fqbn arduino:avr:uno "$SERVER_PROJECT_PATH\RPS-server.ino"

# Якщо вказаний порт COM, завантажуємо програму на плату
if ($ComPort -ne "") {
    Write-Host "Uploading program to the board on port $ComPort with baud rate $BaudRate..."
    & arduino-cli upload -p $ComPort --fqbn arduino:avr:uno "$SERVER_PROJECT_PATH\RPS-server.ino"
} else {
    Write-Host "Skipping Arduino upload as COM port is not specified (GitHub CI environment)"
}

Write-Host "CI completed successfully."