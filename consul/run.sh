#!/bin/bash 
docker run  --net=host -e 'CONSUL_LOCAL_CONFIG={"skip_leave_on_interrupt": true}' consul agent -server -data-dir=/consul/data -bind=0.0.0.0 -advertise=127.0.0.1 -bootstrap-expect=1 -ui

