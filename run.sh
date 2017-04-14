#!/bin/bash

iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE

cd /opt/jetty
java -jar start.jar jetty.home=/opt/jetty
    
while true; do
    sleep 300
done

# If execution reaches this point, the chute will stop running.
exit 0
