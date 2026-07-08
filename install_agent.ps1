param(
    [string]$ServerUrl = "http://192.168.1.50:8080/api/metrics",
    [string]$DeviceId = "windows-device",
    [float]$Interval = 5
)

$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

python -m pip install -r requirements-agent.txt
python .\monitor_agent.py --server-url $ServerUrl --interval $Interval --device-id $DeviceId
