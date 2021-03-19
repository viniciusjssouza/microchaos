import yaml


def _read_consul_address():
    with open('./helm/service-chart/values.yaml', 'r') as file:
        values = dict(yaml.full_load(file))
        return values['consulLocation']


def _store_service_config(desc, consul_location):
    pass


def store_configurations(service_descriptions):
    consul_location = _read_consul_address()

    for desc in service_descriptions.descriptions:
        _store_service_config(desc, consul_location)
