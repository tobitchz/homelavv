# Agente portable para monitor

## Requisitos
- Python 3
- Windows PowerShell o CMD

## Instalación rápida en Windows
1. Copia los archivos `monitor_agent.py`, `requirements-agent.txt`, `run_agent.bat` y `install_agent.ps1` a la PC que quieras monitorear.
2. Edita `run_agent.bat` y cambia la URL del servidor si hace falta.
3. Ejecuta:

```powershell
powershell -ExecutionPolicy Bypass -File .\install_agent.ps1
```

## Ejecución
```powershell
powershell -ExecutionPolicy Bypass -File .\install_agent.ps1
```

O simplemente:

```cmd
run_agent.bat
```
