package pt.upa.broker.ws;

public class Request {
	
	public Request(String origin, String destination, int maxPrice){
		
		RequestTransport requestTransport = new RequestTransport();
		
		requestTransport.setOrigin(origin);
		requestTransport.setDestination(destination);
		requestTransport.setPrice(maxPrice);
		
	}

}