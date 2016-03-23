package pt.upa.broker.ws;

import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.upa.broker.ws.BrokerPortType")
public class BrokerPort implements BrokerPortType{

	@Override
	public String ping(String name) {
		// TODO Auto-generated method stub
		return "Just Pinged";
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		if(id==null){
			UnknownTransportFault b = new UnknownTransportFault();
			b.setId(id);
			throw new UnknownTransportFault_Exception("O id é inválido", b);
		}		return null;
	}

	@Override
	public List<TransportView> listTransports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearTransports() {
		// TODO Auto-generated method stub
		
	}

	// TODO

}
