FROM firethorn/python:3.4.2
MAINTAINER Stelios Voutsinas <stv@roe.ac.uk>

# Install Python Libs
RUN apt-get update
RUN apt-get install -y python-numpy python-scipy
RUN apt-get install -y pwgen
RUN apt-get install -y python-pycurl
RUN wget https://pyodbc.googlecode.com/files/pyodbc-3.0.7.zip
RUN unzip pyodbc-3.0.7.zip  && cd pyodbc-3.0.7/ && python setup.py install
RUN apt-get -y install python-matplotlib
RUN pip install --no-deps astropy

