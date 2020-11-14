# Microchaos

A framework/utility for building synthetic microservices with builtin fault injection and simulation.

## Restrictions

Currently, it only works on unix-family OS, because it uses some native utilities to emulate failures, such as `netstat`.  

## Setup

### Ubuntu

```
sudo apt install -y net-tools
```

### Docker

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

#### Using local file

To run a service configured by a local file, use the `CONFIG_PATH` environment variable, containing the path
of the model file. Example:

```bash 
CONFIG_PATH=./src/test/resources/simpleService.yaml ./gradlew run
``` 

#### Using Consul

To run a service configured by a local file, use the `CONFIG_PATH` environment variable, containing the path
of the model file. Example:

```bash 
CONFIG_PATH=./src/test/resources/simpleService.yaml ./gradlew run
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

TODO

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### Missing features

- Model validation: the model resulting from parsing the YAML files are not validated.  

## License
[MIT](https://choosealicense.com/licenses/mit/)