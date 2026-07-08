@echo off
setlocal
set "SERVER_URL=%MONITOR_URL%"
if "%SERVER_URL%"=="" set "SERVER_URL=http://192.168.1.50:8080/api/metrics"
set "DEVICE_ID=%MONITOR_DEVICE_ID%"
if "%DEVICE_ID%"=="" set "DEVICE_ID=windows-device"
set "INTERVAL=%MONITOR_INTERVAL%"
if "%INTERVAL%"=="" set "INTERVAL=5"
py -3 -m pip install -r requirements-agent.txt
py -3 monitor_agent.py --server-url "%SERVER_URL%" --interval "%INTERVAL%" --device-id "%DEVICE_ID%"
