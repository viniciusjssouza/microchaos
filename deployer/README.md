## Microchaos Deployer

This projects facilitates the deployment of a microchaos topology to a kubernetes cluster.
You just need to create the services configuration files and provide it to the deployer.
It will take the responsability for generating the kubernetes manifests and deploy it to the cluster.

### Requirements
We suggest you use python virtual environment for running it:

```bash
sudo apt install python3-venv
```

Also, you will need helm, kubectl and a proper configured connection to a kubernetes cluster.

Install Helm:
```bash
curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3
chmod 700 get_helm.sh
./get_helm.sh
```

Kubectl installation guide: https://kubernetes.io/docs/tasks/tools/

The  project was developed and tested using GKE - Google Kubernetes Engine.

Also, you will need to install Hashicorp Consul on your cluster. Having helm installed, just run:
```bash
./k8s/consul/consul-install.sh
```

### How to use
**From the microchaos root folder**, (not inside the deployer folder) just run: 
```bash
./deployer/bin/deploy <list of microchaos yaml files>  
```
The list of files should be separated by spaces, like this:
```bash
./deployer/bin/deploy $HOME/microchaos/svc-a.yml $HOME/microchaos/svc-b.yml $HOME/microchaos/svc-c.yml 
```

If you need to configure some variables, like the pod ports or the consul location inside the cluster, just modify
the `helm/service-chart/values.yaml` file.