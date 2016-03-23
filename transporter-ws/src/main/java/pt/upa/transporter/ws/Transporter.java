package pt.upa.transporter.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface Transporter {
	
	@WebMethod
	String ping (String name);
	
	@WebMethod
	JobView requestJob (String origin, String destination, int price) throws BadLocationFault_Exception, BadPriceFault_Exception;
	
	@WebMethod
	JobView decideJob (String id, boolean accept) throws BadJobFault_Exception;
	
	@WebMethod
	JobView jobStatus (String id);
	
	@WebMethod
	JobView listJobs ();
	
	@WebMethod
	void clearJobs ();
}
