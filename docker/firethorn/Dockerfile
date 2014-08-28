
FROM firethorn/tomcat:8.0.9
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Add our Firethorn war file
#RUN wget -q "http://data.metagrid.co.uk/wfau/maven/firethorn/uk/ac/roe/wfau/firethorn-webapp/1.11.10-zrq-docker-test//firethorn-webapp-1.11.10-zrq-docker-test.war"
#RUN mv "firethorn-webapp-1.11.10-zrq-docker-test.war" "/var/local/tomcat/webapps/firethorn.war"

ADD target/firethorn-webapp-1.11.10-zrq-docker-test.war /tmp/firethorn-webapp-1.11.10-zrq-docker-test.war
RUN unzip -q /tmp/firethorn-webapp-1.11.10-zrq-docker-test.war -d /var/local/tomcat/webapps/firethorn
RUN rm /tmp/firethorn-webapp-1.11.10-zrq-docker-test.war

#
# Configure Firethorn

