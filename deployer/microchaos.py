from itertools import chain

import yaml
from slugify import slugify


class ServiceDescriptions:

    def __init__(self, files):
        self.descriptions = ServiceDescriptions._read_services_descriptions(files)

    def services_names(self):
        return list(map(lambda svc: svc['service']['name'], self.descriptions))

    def update_request_targets(self, address_formatter):
        for desc in self.descriptions:
            for endpoint in desc['service']['endpoints']:
                for cmd in endpoint['behavior']['commands']:
                    if 'request' in cmd:
                        cmd['request']['target'] = address_formatter(cmd['request']['target'])
        return self

    @staticmethod
    def _read_services_descriptions(files):
        descriptions = []
        for f in files:
            docs = list(yaml.full_load_all(f))
            descriptions.append(docs)
        services = list(chain.from_iterable(descriptions))
        return ServiceDescriptions._slugify_names(services)

    @staticmethod
    def _slugify_names(services):
        for svc in services:
            svc['service']['name'] = slugify(svc['service']['name'])
        return services
