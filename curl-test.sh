#!/bin/bash

# Start the web server with `lein run -m random-homework.web --port 9000`
# Requires curl and jsonpp to also be on the PATH

HOST=http://localhost:9000

curl -v -X POST $HOST --data 'A|JSON|M|green|09/09/1909'
curl -v -X POST $HOST --data 'B|JSON|M|green|08/08/1808'
curl -v -X POST $HOST --data 'C|JSON|F|green|07/07/1707'
curl -v -X POST $HOST --data 'D|JSON|F|green|06/06/1606' 
curl -v -X POST $HOST --data 'Dont forget the donuts!' 

curl -v http://localhost:9000/records/gender | jsonpp
curl -v http://localhost:9000/records/birthdate | jsonpp
curl -v http://localhost:9000/records/name | jsonpp
curl -v http://localhost:9000/donuts

