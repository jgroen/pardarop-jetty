#!/bin/bash

iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE
iperf3 -s -D
iperf3 -s -p 5202 -D

#cd /opt/jetty
#java -jar start.jar jetty.home=/opt/jetty

java -jar mobJettyServer.jar ./ 8080
    
while true; do
    sleep 300
done

# If execution reaches this point, the chute will stop running.
exit 0
