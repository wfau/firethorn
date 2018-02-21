FROM ubuntu:16.04
MAINTAINER Stelios Voutsinas <docker-stv@roe.ac.uk>

ENV LANG C.UTF-8
RUN apt-get update && \
    apt-get install -y \
        python \
        python3.5 \
        python-pip \
        python3-pip \
        virtualenv
        
RUN pip install --upgrade setuptools pip
RUN pip install virtualenv
RUN pip3 install --upgrade pip
#RUN git clone https://github.com/stvoutsin/firethorn.py.git /home/firethorn.py
COPY firethorn /home/firethorn-py
WORKDIR /home/firethorn-py

RUN  pip3 install /home/firethorn-py
#CMD  pip3 install firethorn.py/

