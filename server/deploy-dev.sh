#!/usr/bin/env bash
VERSION=1.0.0-SNAPSHOT.jar

mvn clean install
scp -p 42069 ./target/friend-scheduler-server-$VERSION.jar postserver@216.171.5.211:/home/postserver/Desktop/
ssh -p 42069 postserver@216.171.5.211

cd Desktop
./start-server.sh $VERSION
exit