helm repo add hashicorp https://helm.releases.hashicorp.com
helm install -f helm-consul-values.yaml hashicorp hashicorp/consul
