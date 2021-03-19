import argparse
from dataclasses import dataclass


@dataclass
class InputArgs:
    config_dir: str
    delete_all: bool = False
    namespace: str = 'microchaos'


def read_input_args():
    parser = argparse.ArgumentParser(
        description='Deploys a service topology to a kubernetes cluster using microchaos definitions.')
    parser.add_argument("--file", help="the microchaos yaml files to be processed",
                        type=argparse.FileType('r'), nargs='+')
    parser.add_argument("--namespace", help="the namespace used in kubernetes", required=False, default='microchaos')
    parser.add_argument("--reset", help="remove all existent resources from kubernetes", required=False,
                        action='store_true')

    return parser.parse_args()
