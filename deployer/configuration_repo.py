import consul
import yaml


class ConfigurationRepo:

    def __init__(self, host, port):
        self.consul = consul.Consul(host=host, port=port, scheme="http")

    def store_configurations(self, service_descriptions):
        for service in service_descriptions.descriptions:
            yaml_content = yaml.dump(service)
            self.consul.kv.put(service['service']['name'], yaml_content)