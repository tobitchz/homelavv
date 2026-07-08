#!/usr/bin/env python3
import argparse
import json
import os
import socket
import sys
import time
import urllib.request
from urllib.error import URLError

import psutil


def parse_args(argv=None):
    parser = argparse.ArgumentParser(description="Send host metrics to the monitor backend")
    parser.add_argument(
        "--server-url",
        default=os.getenv("MONITOR_URL", "http://localhost:8080/api/metrics"),
        help="Target URL for the metrics endpoint",
    )
    parser.add_argument(
        "--interval",
        type=float,
        default=float(os.getenv("MONITOR_INTERVAL", "5")),
        help="Seconds between metric submissions",
    )
    parser.add_argument(
        "--device-id",
        default=os.getenv("MONITOR_DEVICE_ID"),
        help="Optional device identifier to send instead of the hostname",
    )
    return parser.parse_args(argv)


def collect_metrics(device_id=None):
    hostname = device_id or socket.gethostname()
    return {
        "deviceId": hostname,
        "cpu": round(psutil.cpu_percent(interval=None), 2),
        "memoryUsedMb": int(psutil.virtual_memory().used / (1024 * 1024)),
        "diskUsedPercent": int(psutil.disk_usage('/').percent),
    }


def send_metrics(target_url, payload):
    request = urllib.request.Request(
        target_url,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    with urllib.request.urlopen(request, timeout=5) as response:
        return response.status


def main(argv=None):
    args = parse_args(argv)
    while True:
        payload = collect_metrics(args.device_id)
        try:
            status = send_metrics(args.server_url, payload)
            print(f"[{time.strftime('%H:%M:%S')}] sent metrics to {args.server_url} -> {status}")
        except URLError as exc:
            print(f"[{time.strftime('%H:%M:%S')}] failed: {exc}", file=sys.stderr)
        time.sleep(args.interval)


if __name__ == "__main__":
    main()
