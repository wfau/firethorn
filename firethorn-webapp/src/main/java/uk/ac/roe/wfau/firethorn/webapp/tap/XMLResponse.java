package uk.ac.roe.wfau.firethorn.webapp.tap;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement(name = "xml")
public class XMLResponse {
	 
	 
		String name;
		
		public String getName() {
			return name;
		}
	 
		@XmlElement
		public void setName(String name) {
			this.name = name;
		}
	
		
	 
		public XMLResponse(String name) {
			this.name = name;
		
		}
	 
		public XMLResponse() {
		}
	 
	
}
