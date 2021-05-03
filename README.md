# Microchaos

A framework/utility for building synthetic microservices with builtin fault injection and simulation.

The general idea is to raise "dummy" microservices with programmable behavior. You can control the
service behavior through external configurations, being able to change it on runtime,
without any restart. 

Microchaos relies on YAML documents to describe how a microservice should behave and configure 
its REST/HTTP interfaces.  

## Restrictions

Currently, it only works on unix-family OS, because it uses some native utilities to emulate failures, such as `netstat`.  

## Setup

### Ubuntu
To run a microchaos microservice on ubuntu you will need to install:
```
sudo apt install -y net-tools
```

### Docker
The project include a Dockerfile and a docker compose file to facilitate running it without installing
additional libraries on your OS. Using docker-compose, just declare your topology inside 
`docker-compose.yml` and raise the cluster with:
```
docker-compose up -d <services-containers>
```
The compose file already declares consul, so if you want to use it to store the services configurations,
just declare the environment variables as described [here](#using-consul).


For running the tests using docker, run:
```
docker-compose up tests
```

## Usage

### Build
To build the application using Gradle, run in the project root folder:

```bash
./gradlew build
```

### Test

```bash
./gradlew test
```

### Running
```bash
./gradlew run
```

### Configuring service behavior

Microchaos supports two sources of configurations: **local YAML** file or **Consul**.

#### Using local file

To run a service configured by a local file, use the `CONFIG_PATH` environment variable, containing the path
of the model file. Example:

```bash 
CONFIG_PATH=./src/test/resources/simpleService.yaml ./gradlew run
``` 

#### Using Consul
[Hashicorp Consul](https://www.consul.io/) is service capable of storing key-value pairs and providing
a REST and Web interfaces to access it. Microchaos is able to use it to retrieve the services configurations
and watch for changes. After starting a microchaos cluster, you can access the consul UI to 
change the services' behavior, whenever you need it.

To use consul, microchaos requires a name to **uniquely identify** the service and the consul address:

```bash 
CONSUL_URL=http://localhost:8500;SERVICE_NAME=svc-1 ./gradlew run
``` 

## Architecture

TODO

### Adding new commands

1. Write a new class extending the `Command` class and implementing the `run` method. This method is called upon 
receiving a new request (for services with type `web`)
2. Register the new command class in the `microchaos.model.command.CommandMapping` file. The key added to the hash map 
will be used to represent the command on the model file. Example:
```
"memoryAllocation" to MemoryAllocationCommand::class.java,
``` 
In the model file, this command can be declared in this way:
```
behavior:
  commands:
    - memoryAllocation:
```

## Supported failure modes

- Resources exhaustion
    
  - CPU
  - Memory
    
- Network failure
- Application shutdown
- Request Timeouts
- Unexpected responses

## Deployer

The project includes a utility for deploying microservices topologies based on microchaos to a Kubernetes cluster,
under the `deployer` folder. It is a Python project. For further details on how to use it, read the
[docs here](./deployer/README.md).


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### Missing features

- Model validation: the model resulting from parsing the YAML files are not validated.  
- Support configurable request timeouts (today is fixed on 3 seconds)
- Support configurable amount of worker threads (today is fixed on 8 threads)
- Support asynchronous request command
- Support asynchronous I/O bound command

## License
[MIT](https://choosealicense.com/licenses/mit/)