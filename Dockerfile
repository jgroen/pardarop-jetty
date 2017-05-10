FROM openjdk:9

# In case someone loses the Dockerfile
RUN rm -rf /etc/Dockerfile
ADD Dockerfile /etc/Dockerfile

# Install packages
RUN apt-get update && \ 
    apt-get update --fix-missing && \ 
    apt-get install -y \
    wget \
    iptables \
    iperf3

# Download and install jetty
RUN wget http://central.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.3.v20170317/jetty-distribution-9.4.3.v20170317.tar.gz && \
    tar -xzvf jetty-distribution-9.4.3.v20170317.tar.gz && \
    rm -rf jetty-distribution-9.4.3.v20170317.tar.gzz && \
    mv jetty-distribution-9.4.3.v20170317/ /opt/jetty

# Configure Jetty user and clean up install
RUN useradd jetty && \
    chown -R jetty:jetty /opt/jetty && \
    rm -rf /opt/jetty/webapps.demo

#For websocket, un-comment the following line
EXPOSE 7070
#For HTTP un-comment the following line
#EXPOSE 8080
EXPOSE 5201
EXPOSE 5202

ADD run.sh /usr/local/bin/run.sh
ADD mobJettyServer.jar mobJettyServer.jar

# Add the video
ADD opt2 files/opt2/

CMD ["bash", "/usr/local/bin/run.sh"]



