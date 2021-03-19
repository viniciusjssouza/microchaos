from itertools import chain

import yaml


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
        return list(chain.from_iterable(descriptions))

# result = read_services_descriptions(['../examples/two-connected-services/two-connected-services.yml'])
# print(result)
