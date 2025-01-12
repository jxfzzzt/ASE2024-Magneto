#!/bin/bash

HOST=localhost
PORT=4567

curl -vX POST http://${HOST}:${PORT}/validate -d @../resources/data.json --header "Content-Type: application/json"
