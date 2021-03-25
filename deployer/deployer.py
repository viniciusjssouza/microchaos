import logging

from configuration_repo import ConfigurationRepo
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

    services = ServiceDescriptions(args.file).update_request_targets(kubernetes.format_address)
    logger.info('Services found: %s', services.services_names())

    config_repo = ConfigurationRepo(args.consulHost, args.consulPort)
    config_repo.store_configurations(services)
    logger.info('Configurations stored at Consul with success')

    kubernetes.create_namespace()
    if args.reset is True:
        kubernetes.delete_all_resources()
    kubernetes.apply_resources(services.descriptions)


if __name__ == '__main__':
    main()
