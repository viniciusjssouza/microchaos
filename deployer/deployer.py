import logging

from input import read_input_args
from kubernetes import Kubernetes


def config_logging():
    log_format = '%(asctime)s %(name)s %(levelname)s :: %(message)s'
    logging.basicConfig(
        level='INFO',
        format=log_format,
        datefmt='%y-%m-%d %H:%M:%S')


def main():
    config_logging()
    args = read_input_args()

    kubernetes = Kubernetes(args.namespace)
    kubernetes.create_namespace()
    if args.reset is True:
        kubernetes.delete_all_resources()


if __name__ == '__main__':
    main()
