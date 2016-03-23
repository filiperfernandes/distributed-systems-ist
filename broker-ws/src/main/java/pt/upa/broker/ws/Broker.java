package pt.upa.broker.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface Broker 
{
	@WebMethod
	String ping (String name);
	
	@WebMethod
	String requestTransport (String origin, String destination, int price) throws UnknownLocationFault_Exception, InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception;
	
	@WebMethod
	TransportView viewTransport (String id) throws UnknownTransportFault_Exception;
	
	@WebMethod
	TransportView listTransports ();
	
	@WebMethod
	void clearTrasports ();
	
}