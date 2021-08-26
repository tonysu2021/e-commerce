#!/bin/sh

docker exec -it $1 psql -U postgres
# exit
# command \q