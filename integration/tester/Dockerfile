FROM firethorn/fedora:20
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

RUN yum -y install perl
RUN yum -y install perl-JSON
ENV a 3
COPY bin/  /tester/bin 
COPY meta/ /tester/meta 

ADD https://github.com/micha/resty/raw/master/pp    /tester/bin/
ADD https://github.com/micha/resty/raw/master/resty /tester/bin/

RUN chmod a+x /tester/bin/pp
RUN chmod a+x /tester/bin/resty

WORKDIR /tester
