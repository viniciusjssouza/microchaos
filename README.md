# MicroChaos

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

TODO

## Architecture

TODO

## Supported failure modes

TODO

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### Missing features

- Model validation: the model resulting from parsing the YAML files are not validated.  

## License
[MIT](https://choosealicense.com/licenses/mit/)