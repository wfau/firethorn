
FROM firethorn/java:1.8.0
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

ENV TOMCAT_VERSION 8.0.14

ENV TOMCAT_USER tomcat
ENV TOMCAT_ROOT /var/local/tomcat

#
# Expose Tomcat port
EXPOSE :8080

#
# Install the Tomcat Native Library
RUN yum -y install tomcat-native

#
# Create the Tomcat user.
RUN useradd --system --home "$TOMCAT_ROOT" "$TOMCAT_USER"

#
# Set our download directory.
WORKDIR /tmp

#
# Download the tar file
ADD https://archive.apache.org/dist/tomcat/tomcat-8/v$TOMCAT_VERSION/bin//apache-tomcat-$TOMCAT_VERSION.tar.gz     /tmp/
ADD https://archive.apache.org/dist/tomcat/tomcat-8/v$TOMCAT_VERSION/bin//apache-tomcat-$TOMCAT_VERSION.tar.gz.md5 /tmp/

#
# Check the checksum.
RUN md5sum -c "apache-tomcat-$TOMCAT_VERSION.tar.gz.md5"

#
# Unpack the tar file.
RUN tar -zxf  apache-tomcat-$TOMCAT_VERSION.tar.gz
RUN rm apache-tomcat-$TOMCAT_VERSION.tar.gz
RUN rm apache-tomcat-$TOMCAT_VERSION.tar.gz.md5

#
# Relocate the unpacked directory
RUN mv apache-tomcat-$TOMCAT_VERSION $TOMCAT_ROOT

#
# Remove the default webapps.
RUN rm -rf $TOMCAT_ROOT/webapps/docs
RUN rm -rf $TOMCAT_ROOT/webapps/ROOT
RUN rm -rf $TOMCAT_ROOT/webapps/examples
RUN rm -rf $TOMCAT_ROOT/webapps/manager
RUN rm -rf $TOMCAT_ROOT/webapps/host-manager

#
# Allow read access to the config files.
RUN chmod a+r $TOMCAT_ROOT/conf/*

#
# Allow write access to the working directories.
RUN chown -R $TOMCAT_USER:$TOMCAT_USER $TOMCAT_ROOT/logs
RUN chown -R $TOMCAT_USER:$TOMCAT_USER $TOMCAT_ROOT/work
RUN chown -R $TOMCAT_USER:$TOMCAT_USER $TOMCAT_ROOT/temp

#
# Create our deployment directory.
RUN mkdir $TOMCAT_ROOT/conf/Catalina
RUN mkdir $TOMCAT_ROOT/conf/Catalina/localhost
RUN chown $TOMCAT_USER:$TOMCAT_USER $TOMCAT_ROOT/conf/Catalina/localhost

#
# Expose our data volumes.
# BUG -- ENV variable not resolved
VOLUME /var/local/tomcat/logs
VOLUME /var/local/tomcat/work
VOLUME /var/local/tomcat/temp

#
# Run Tomcat
# BUG -- ENV variable not resolved
USER    tomcat
WORKDIR /var/local/tomcat
CMD /var/local/tomcat/bin/catalina.sh run

