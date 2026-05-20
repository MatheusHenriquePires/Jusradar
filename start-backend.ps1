$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

$envFile = Join-Path $PSScriptRoot '..\.env'

if (Test-Path $envFile) {
  Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()

    if (-not $line -or $line.StartsWith('#') -or -not $line.Contains('=')) {
      return
    }

    $key, $value = $line.Split('=', 2)
    [Environment]::SetEnvironmentVariable($key.Trim(), $value.Trim(), 'Process')
  }
}

Set-Location $PSScriptRoot
.\mvnw.cmd spring-boot:run
