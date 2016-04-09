package pt.upa.broker.ws;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.jws.WebService;




@WebService(endpointInterface = "pt.upa.broker.ws.BrokerPortType")
public class BrokerPort implements BrokerPortType{

	TreeMap<String, Transport> transporters = new TreeMap<String, Transport>();

	public BrokerPort(){

		transporters.put("1", new Transport("1","Lisboa","Porto",2, "UpaTransporter1",TransportStateView.values()[0]));
		transporters.put("2", new Transport("2","Bragança", "Leiria",0, "UpaTransporter1",TransportStateView.values()[0]));

	}

	public boolean checkValidLocation(String location){
		String [] valid = {"Lisboa" , "Leiria", "Santaré́m", "Castelo Branco", "Coimbra", "Aveiro", 
				"Viseu", "Guarda" , "Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança",
				"Setúbal","É́vora","Portalegre","Beja","Faro"};

		for (String l: valid){
			if (location.equals(l)){
				return true;
			}
		}
		return false;
	}


	@Override
	public String ping(String name) {
		return "Just Pinged" + name;
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {


		if(!checkValidLocation(origin)){
			UnknownLocationFault b = new UnknownLocationFault();
			throw new UnknownLocationFault_Exception("A origem está errada.", b);
		}
		if(!checkValidLocation(destination)){
			UnknownLocationFault b = new UnknownLocationFault();
			throw new UnknownLocationFault_Exception("O destino está errado.", b);
		}

		if(price<0){
			InvalidPriceFault p = new InvalidPriceFault();
			p.setPrice(price);
			throw new InvalidPriceFault_Exception("O preço é inválido",p);
		}


		for(Map.Entry<String, Transport> entry : transporters.entrySet()) {
			Transport value = entry.getValue();
			if(origin.equals(value.getJob().getOrigin())  && (destination.equals( value.getJob().getDestination()) ) && 
					price==value.getJob().getPrice()) {
				value.getJob().setState(TransportStateView.values()[0]);
				return "State changed to budgeted";
			}
			else if (origin.equals(value.getJob().getOrigin())  && (destination.equals( value.getJob().getDestination()) ) && price!=value.getJob().getPrice()){
				UnavailableTransportPriceFault b = new UnavailableTransportPriceFault();
				b.setBestPriceFound(price);
				throw new UnavailableTransportPriceFault_Exception("O preço está errado", b);
			}
			else{
				UnavailableTransportFault b = new UnavailableTransportFault();
				b.setOrigin(origin);
				b.setDestination(destination);
				throw new UnavailableTransportFault_Exception("Viagem indisponivel", b);
			}
		}
		return "Viagem indisponivel";
	}




	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		if(id==null){
			UnknownTransportFault b = new UnknownTransportFault();
			b.setId(id);
			throw new UnknownTransportFault_Exception("O id é inválido", b);
		}
		else{


			for(Map.Entry<String,Transport > entry : transporters.entrySet()) {
				String key = entry.getKey();
				Transport value = entry.getValue();
				if(id.equals(key)){
					return value.getJob();
				}
			}

		}
		return null;
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
