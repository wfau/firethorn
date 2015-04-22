FROM    andystanton/mysql:5.7
ADD     ./install.sh /tmp/install.sh
RUN chmod u+x /tmp/install.sh
RUN     /tmp/install.sh
EXPOSE  3307
CMD     ["mysqld", "--datadir=/var/lib/mysql", "--user=mysql"]
