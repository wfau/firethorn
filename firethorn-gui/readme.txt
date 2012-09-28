Instructions:

1) Navigate to directory where the "firethorn-webpy-gui" folder is located.

2) In config.py change the lines:

 	a) web_services_hostname = 'localhost:8080'
	b) base_location = '/home/stelios/Desktop/workspace/firethorn-webpy-gui'

(a) refers to the base URL for the back-end where the Java web services are located

(b) refers to the base location of the "firethorn-webpy-gui" folder, which contains the app.py file and all the source for the GUI.
	

3) In the command line, run:
	>> python app.py 8090 
