FROM firethorn/fedora:20
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

RUN yum -y install perl
RUN yum -y install perl-JSON

COPY bin/  /tester/bin 
COPY meta/ /tester/meta 
WORKDIR /tester
