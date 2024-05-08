#!/bin/bash

authNetwork=`docker network ls | grep auth-net | wc -l`
authNetwork=$(($authNetwork + 0))

if [[ $authNetwork = "0" ]]
then
  docker network create --driver bridge auth-net
fi

docker run -dit --rm -p 8080:8080 app/jwt-auth

