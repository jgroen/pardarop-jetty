# ParDarop-Jetty
This ParaDrop docker image contains OpenJDK 9 with Jetty 9.4.3.v20170317.

# Usage
To serve your web application, mount a volume at /opt/jetty/webapps. By default, the container will start jetty with java -jar start.jar jetty.home=/opt/jetty.  Changes can be made in the run.sh file.   
