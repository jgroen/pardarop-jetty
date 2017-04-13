#!/bin/bash


iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE



java -jar start.jar --module=http jetty.http.port=8080
    
while true; do
    sleep 300
done

# If execution reaches this point, the chute will stop running.
exit 0



CMD ["java", "-jar", "start.jar", "jetty.home=/opt/jetty"]
