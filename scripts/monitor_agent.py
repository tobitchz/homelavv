#!/usr/bin/env python3
import json
import os
import socket
import sys
import time
import urllib.request
from urllib.error import URLError

import psutil

TARGET_URL = os.getenv("MONITOR_URL", "http://localhost:8080/api/metrics")
INTERVAL_SECONDS = float(os.getenv("MONITOR_INTERVAL", "5"))


def collect_metrics():
    hostname = socket.gethostname()
    return {
        "deviceId": hostname,
        "cpu": round(psutil.cpu_percent(interval=None), 2),
        "memoryUsedMb": int(psutil.virtual_memory().used / (1024 * 1024)),
        "diskUsedPercent": int(psutil.disk_usage('/').percent),
    }


def send_metrics(payload):
    request = urllib.request.Request(
        TARGET_URL,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    with urllib.request.urlopen(request, timeout=5) as response:
        return response.status


def main():
    while True:
        payload = collect_metrics()
        try:
            status = send_metrics(payload)
            print(f"[{time.strftime('%H:%M:%S')}] sent metrics to {TARGET_URL} -> {status}")
        except URLError as exc:
            print(f"[{time.strftime('%H:%M:%S')}] failed: {exc}", file=sys.stderr)
        time.sleep(INTERVAL_SECONDS)


if __name__ == "__main__":
    main()
