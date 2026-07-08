import importlib.util
import pathlib
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
MODULE_PATH = ROOT / "scripts" / "monitor_agent.py"

spec = importlib.util.spec_from_file_location("monitor_agent", MODULE_PATH)
monitor_agent = importlib.util.module_from_spec(spec)
spec.loader.exec_module(monitor_agent)


class MonitorAgentArgsTest(unittest.TestCase):
    def test_parse_args_supports_remote_server_url(self):
        args = monitor_agent.parse_args(
            [
                "--server-url",
                "http://192.168.1.50:8080/api/metrics",
                "--interval",
                "3",
                "--device-id",
                "raspberry-pi",
            ]
        )

        self.assertEqual(args.server_url, "http://192.168.1.50:8080/api/metrics")
        self.assertEqual(args.interval, 3.0)
        self.assertEqual(args.device_id, "raspberry-pi")


if __name__ == "__main__":
    unittest.main()
