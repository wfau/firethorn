package asov.tap;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//ZRQ
//import org.apache.catalina.connector.ClientAbortException;

import asov.DemoASOV;

import tap.resource.TAP;

import uws.UWSException;

//ZRQ
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoTAP_Servlet extends HttpServlet {

    //ZRQ
    private static Logger logger = LoggerFactory.getLogger(DemoTAP_Servlet.class);

	private static final long serialVersionUID = 1L;

	private TAP tap = null;

	@Override
	public void init() throws ServletException {
		super.init();
logger.debug("init()");
		try {
			// 1. Create a TAP instance with the written ServiceConnection:
			tap = new TAP(new DemoTAP_ServiceConnection());

			// Customize the TAP home page:
			//ZRQ
			//tap.setHomePageURI("http://"+DemoASOV.SERVER_NAME+"/asov/tapHome.html");
			tap.setHomePageURI("src/main/webapp/demo/tapHome.html");

		} catch (UWSException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//ZRQ
logger.debug("service(HttpServletRequest, HttpServletResponse)");

		try {
			// 2. Forward all requests to the TAP instance:
			tap.executeRequest(request, response);

		}catch(Throwable t){
			System.err.println("Request aborted !");
			t.printStackTrace(System.err);
/*
 * ZRQ
			try{
				if (!(t instanceof ClientAbortException))
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, t.getMessage());
			}catch(ClientAbortException cae){
				System.out.println("Send error aborted !");
			}
*/
		}
	}


}
