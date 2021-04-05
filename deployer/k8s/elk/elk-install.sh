#!/bin/bash

# guide: https://www.linode.com/docs/guides/how-to-deploy-the-elastic-stack-on-kubernetes/#install-elasticsearch
# doc for Elasticsearch: https://github.com/elastic/helm-charts/blob/master/elasticsearch/README.md
helm repo add elastic https://helm.elastic.co
helm install -f ./elasticsearch-values.yaml elasticsearch elastic/elasticsearch
helm install filebeat elastic/filebeat
helm install -f ./metricbeats-values.yaml metricbeat elastic/metricbeat
helm install -f ./kibana-values.yaml kibana elastic/kibana

# From https://stackoverflow.com/questions/29192835/elasticsearchhow-to-change-cluster-health-from-yellow-to-green
# I suggest updating the replication factor of all the indices to 0
curl -XPUT 'http://localhost:9200/_settings' -H 'Content-Type: application/json' -d '
{
    "index" : {
        "number_of_replicas" : 0
    }
}'