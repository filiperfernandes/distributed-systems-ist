package pt.upa.broker.ws;



public class Transport {
	
	TransportView tv;
	
	public Transport(String id, String origin, String destination, int price, String transporterCompany,TransportStateView state){
		
		tv = new TransportView();
		
		tv.setDestination(destination);
		tv.setId(id);
		tv.setOrigin(origin);
		tv.setPrice(price);
		tv.setState(state);
		
		
	}
	public TransportView getJob(){
		return this.tv;
	}
}