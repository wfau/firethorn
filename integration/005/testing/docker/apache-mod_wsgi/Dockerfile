FROM firethorn/pythonlibs
MAINTAINER Stelios Voutsinas <stv@roe.ac.uk>


# Install apache, PHP, and supplimentary programs. curl and lynx-cur are for debugging the container.
RUN apt-get -y install apache2 libapache2-mod-wsgi 
 
# Enable apache mods.
RUN a2enmod rewrite
 
 
# Manually set up the apache environment variables
ENV APACHE_RUN_USER www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR /var/log/apache2
ENV APACHE_LOCK_DIR /var/lock/apache2
ENV APACHE_PID_FILE /var/run/apache2.pid
 
EXPOSE 80
 
# Copy site into place.

#ADD ./osa /var/www/osa
 
# Update the default apache site with the config we created.
#ADD apache-config.conf /etc/apache2/sites-enabled/000-default.conf
 
# By default, simply start apache.
CMD /usr/sbin/apache2ctl -D FOREGROUND 

