import logging

from configuration_repo import store_configurations
from input import read_input_args
from kubernetes import Kubernetes
from microchaos import ServiceDescriptions

logger = logging.getLogger('Deployer')


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
    # kubernetes.create_namespace()
    # if args.reset is True:
    #     kubernetes.delete_all_resources()

    services = ServiceDescriptions(args.file).update_request_targets(kubernetes.format_address)
    logger.info('Services found: %s', services.services_names())
    print(services.descriptions)
    store_configurations(services)

if __name__ == '__main__':
    main()
