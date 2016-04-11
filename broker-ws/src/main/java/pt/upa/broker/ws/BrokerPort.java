package pt.upa.broker.ws;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.jws.WebService;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.Job;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPort;

@WebService(endpointInterface = "pt.upa.broker.ws.BrokerPortType")
public class BrokerPort implements BrokerPortType {

	TreeMap<String, Transport> transporters = new TreeMap<String, Transport>();

	//List<JobView> list = new TransporterPort().listJobs();
	
	String tid="";

	public BrokerPort() {


//		transporters.put("1", new Transport("1","Lisboa","Porto",2, "UpaTransporter1",TransportStateView.values()[0]));
//		transporters.put("2", new Transport("2","Bragança", "Leiria",0, "UpaTransporter1",TransportStateView.values()[0]));

	}
	//	public TransporterPort getTransporter(TransporterPort port){
	//		if(port==null){
	//			return new TransporterPort();
	//		}
	//		
	//		return port;
	//	}

	
public TransporterPort getTransp(String origin , String destination){
		
		//Check if there are sufficient verifications
		String [] norte = {"Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança"};
//		String [] centro = {"Lisboa" , "Leiria", "Santaré́m", "Castelo Branco", "Coimbra", "Aveiro", 
//				"Viseu", "Guarda"};
//		String [] sul = {"Setúbal","É́vora","Portalegre","Beja","Faro"};
		
		for (String n: norte){
			if ( origin.equals(n) || destination.equals(n) ){
				tid="2";
				return new TransporterPort("2");
			}
		}
		tid="1";	
		return new TransporterPort("1");
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


	public String getNextId(){
		Integer id=0;

		for(Map.Entry<String, Transport> entry : transporters.entrySet()) {
			int key = Integer.parseInt(entry.getKey());
			if(key>id){
				id=key;
			}
		}
		return String.valueOf(id+1);
	}
	
	@Override
	public String ping(String name) {
//		list=new TransporterPort().listJobs();
//
//		for (JobView temp: list){
//			if(temp.getJobOrigin().equals(name)){
//				return "O FIlipe é muito macho";
//			}
//		}
		return "O André bard";
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
		String id = getNextId();
		
		TransporterPort port = getTransp(origin, destination);

		transporters.put(id, new Transport(id,origin,destination ,price, null,TransportStateView.values()[0]));
		JobView j=null;


		try {
			j = port.requestJob(origin, destination, price);
		} catch (BadLocationFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPriceFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (j==null){
			UnavailableTransportFault b = new UnavailableTransportFault();
			b.setOrigin(origin);
			b.setDestination(destination);
			throw new UnavailableTransportFault_Exception("Viagem indisponivel", b);
		}

		else if(j.getJobPrice()>price){
			UnavailableTransportPriceFault b = new UnavailableTransportPriceFault();
			b.setBestPriceFound(price);
			throw new UnavailableTransportPriceFault_Exception("O preço está errado", b);
		}

		else{


			//Por a budgeted
			Transport t = transporters.get(id);
			t.getJob().setState(TransportStateView.values()[1]);
			t.getJob().setTransporterCompany(tid);


			//DecideJob

			if(j.getJobPrice()<=t.getJob().getPrice()){
				try {
					j=port.decideJob(id, true);
					t.getJob().setPrice(j.getJobPrice());
				} catch (BadJobFault_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					j=port.decideJob(id, false);
				} catch (BadJobFault_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//Mudar estado para booked ou failes
			if(j.getJobState().equals(JobStateView.values()[2])){
				t.getJob().setState(TransportStateView.values()[3]);
			}
			else if(j.getJobState().equals(JobStateView.values()[1])){
				t.getJob().setState(TransportStateView.values()[2]);	

			}
		}
		return "Success";
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
