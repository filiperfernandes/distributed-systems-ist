package pt.upa.broker.ws;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.upa.broker.ws.Broker")
public class BrokerImpl implements Broker{
	
	@Override
	public String ping(String name) {
		return null;
	}

	@Override
	public String requestTransport(String origin, String destination, int price) throws UnknownLocationFault_Exception, InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransportView listTransports() {
		return null;
	}

	@Override
	public void clearTrasports() {
	}

}
