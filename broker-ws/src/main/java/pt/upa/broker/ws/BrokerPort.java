package pt.upa.broker.ws;

import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.upa.broker.ws.BrokerPortType")
public class BrokerPort implements BrokerPortType{
	
	public BrokerPort(){
		
	}

	@Override
	public String ping(String name) {
		return "Just Pinged";
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		
		if(origin==null){
			UnknownLocationFault b = new UnknownLocationFault();
			throw new UnknownLocationFault_Exception("A origem está errada.", b);
			}
		
		if(price<0){
			InvalidPriceFault p = new InvalidPriceFault();
			p.setPrice(price);
			throw new InvalidPriceFault_Exception("O preço é inválido",p);
			}
		
		/*if(){
			UnavailableTransportFault u = new UnavailableTransportFault();*/
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


	}
