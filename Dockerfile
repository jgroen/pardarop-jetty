FROM java:8-jdk
MAINTAINER Nicholas Iaquinto <nickiaq@gmail.com>

# In case someone loses the Dockerfile
RUN rm -rf /etc/Dockerfile
ADD Dockerfile /etc/Dockerfile

# Install packages
RUN apt-get update && \ 
    apt-get update --fix-missing && \ 
    apt-get install -y wget

# Download and install jetty
RUN wget http://central.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.3.v20170317/jetty-distribution-9.4.3.v20170317.tar.gz && \
    tar -xzvf jetty-distribution-9.4.3.v20170317.tar.gz && \
    rm -rf jetty-distribution-9.4.3.v20170317.tar.gzz && \
    mv jetty-distribution-9.4.3.v20170317/ /opt/jetty

# Configure Jetty user and clean up install
RUN useradd jetty && \
    chown -R jetty:jetty /opt/jetty && \
    rm -rf /opt/jetty/webapps.demo

# Set defaults for docker run
WORKDIR /opt/jetty
CMD ["java", "-jar", "start.jar", "jetty.home=/opt/jetty"]

