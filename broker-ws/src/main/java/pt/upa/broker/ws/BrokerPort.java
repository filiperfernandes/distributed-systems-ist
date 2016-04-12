package pt.upa.broker.ws;

import java.util.ArrayList;
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
		return "O André bard"+name;
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
		
		//TransporterPort port = getTransp(origin, destination);
		
		TransporterPort t1 = new TransporterPort("1");
		TransporterPort t2 = new TransporterPort("2");
		
		transporters.put(id, new Transport(id,origin,destination ,price, null,TransportStateView.values()[0]));
		
		JobView j1 = null;
		JobView j2 = null;
		
		
		try {
			j1 = t1.requestJob(origin, destination, price);
		} catch (BadLocationFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPriceFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			j2 = t2.requestJob(origin, destination, price);
		} catch (BadLocationFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPriceFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		if (j1==null&&j2==null){
			UnavailableTransportFault b = new UnavailableTransportFault();
			b.setOrigin(origin);
			b.setDestination(destination);
			throw new UnavailableTransportFault_Exception("Viagem indisponivel", b);
		}
		
		//Por a budgeted
		Transport t = transporters.get(id);
		t.getJob().setState(TransportStateView.values()[1]);
		
		
		if ( j1==null  ){
			try {
				t2.decideJob(j2.getJobIdentifier(), true);
				t.getJob().setTransporterCompany("2");
			} catch (BadJobFault_Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (j2==null){
			try {
				t1.decideJob(j1.getJobIdentifier(), true);
				t.getJob().setTransporterCompany("1");
			} catch (BadJobFault_Exception e) {
				e.printStackTrace();
			}
		}
		
		
		else if(j1.getJobPrice()>price&&j2.getJobPrice()>price){
			t.getJob().setState(TransportStateView.values()[2]);
			UnavailableTransportPriceFault b = new UnavailableTransportPriceFault();
			b.setBestPriceFound(price);
			throw new UnavailableTransportPriceFault_Exception("O preço está errado", b);
		}

		else{

			//DecideJob

			if(j1.getJobPrice()<j2.getJobPrice()){
				try {
					t1.decideJob(j1.getJobIdentifier(), true);
					t.getJob().setTransporterCompany("1");
				} catch (BadJobFault_Exception e) {
					e.printStackTrace();
				}
				try {
					t2.decideJob(j2.getJobIdentifier(), false);
				} catch (BadJobFault_Exception e) {
					e.printStackTrace();
				}
			}
			else if (j1.getJobPrice()>j2.getJobPrice()){
				try {
					t1.decideJob(j1.getJobIdentifier(), false);
				} catch (BadJobFault_Exception e) {
					e.printStackTrace();
				}
				try {
					t2.decideJob(j2.getJobIdentifier(), true);
					t.getJob().setTransporterCompany("2");
				} catch (BadJobFault_Exception e) {
					e.printStackTrace();
				}
			}
			
			else{
				try {
					t1.decideJob(j1.getJobIdentifier(), true);
					t.getJob().setTransporterCompany("1");
				} catch (BadJobFault_Exception e) {
					e.printStackTrace();
				}
				try {
					t2.decideJob(j2.getJobIdentifier(), false);
				} catch (BadJobFault_Exception e) {
					e.printStackTrace();
				}
			}

			t.getJob().setState(TransportStateView.values()[3]);
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
		List<TransportView> list = new ArrayList<>();

		for(Map.Entry<String, Transport> entry : transporters.entrySet()) {
			TransportView value = entry.getValue().getJob();
			list.add(value);
		}
		return list;
	}

	@Override
	public void clearTransports() {
		transporters.clear();
		new TransporterPort("1").clearJobs();
		new TransporterPort("2").clearJobs();

	}


}
