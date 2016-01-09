FROM firethorn/ubuntu:14.04
MAINTAINER Stelios Voutsinas <stv@roe.ac.uk>

RUN apt-get -y update
RUN apt-get -y upgrade

# Install apache, PHP, and supplimentary programs.
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install apache2 curl 
# Enable apache mods.
RUN a2enmod rewrite


# Manually set up the apache environment variables
ENV APACHE_RUN_USER www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR /var/log/apache2
ENV APACHE_LOCK_DIR /var/lock/apache2
ENV APACHE_PID_FILE /var/run/apache2.pid

EXPOSE 80

# Update the default apache site with the config we created.
ADD apache-config.conf /etc/apache2/sites-enabled/000-default.conf

# By default, simply start apache.
CMD /usr/sbin/apache2ctl -D FOREGROUND
