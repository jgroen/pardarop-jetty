#!/bin/bash

iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE
iperf3 -s -D
iperf3 -s -p 5202 -D

mkdir files/opt2
cd files/opt2
wget -r -np -nd http://52.37.177.82/opt2/
cd

#Uncomment the next line for http version
#java -jar mobJettyServer.jar ./ 8080 HTTP

#Uncomment the next line for web socket
java -jar mobJettyServer.jar ./ 7070

while true; do
    sleep 300
done

# If execution reaches this point, the chute will stop running.
exit 0
