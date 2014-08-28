
FROM firethorn/java:1.8.0
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Install Tomcat
RUN wget -q "https://archive.apache.org/dist/tomcat/tomcat-8/v8.0.9/bin//apache-tomcat-8.0.9.tar.gz"
RUN wget -q "https://archive.apache.org/dist/tomcat/tomcat-8/v8.0.9/bin//apache-tomcat-8.0.9.tar.gz.md5"
RUN md5sum -c apache-tomcat-*.md5
RUN tar -zxf apache-tomcat-*.tar.gz
RUN rm apache-tomcat-*.tar.gz
RUN rm apache-tomcat-*.tar.gz.md5
RUN mv apache-tomcat* /var/local/tomcat

#
# Configure Tomcat
EXPOSE :8080
#
# Run Tomcat
WORKDIR /var/local/tomcat
CMD bin/catalina.sh run

