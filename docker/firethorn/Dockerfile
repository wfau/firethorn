
FROM firethorn/tomcat:8.0.9
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Install Firethorn war file
ADD target/firethorn-webapp-1.11.18-zrq-docker-devops.war /tmp/firethorn-webapp-1.11.18-zrq-docker-devops.war
RUN unzip -q /tmp/firethorn-webapp-1.11.18-zrq-docker-devops.war -d /var/local/tomcat/webapps/firethorn
RUN rm /tmp/firethorn-webapp-1.11.18-zrq-docker-devops.war

#
# Configure Firethorn

