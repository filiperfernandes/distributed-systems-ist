package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.xml.registry.JAXRException;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;

@WebService(endpointInterface = "pt.upa.broker.ws.BrokerPortType")
public class BrokerPort implements BrokerPortType {

	//TreeMap<String, Transport> transporters = new TreeMap<String, Transport>();

	List<TransportView> transporters = new ArrayList<TransportView>();

	TransporterClient t1 ;
	TransporterClient t2;


	String tmp="";

	String tid="";
	String bid="";

	public BrokerPort(String bid) throws JAXRException {
		this.bid=bid;
		t1 = new TransporterClient("http://localhost:9090", "UpaTransporter1");
		t2= new TransporterClient("http://localhost:9090", "UpaTransporter2");
		if(bid.equals("3")){
			AliveThread aliveT = new AliveThread();
			aliveT.start();
		}
	}


	public TransporterClient getTransp(String origin , String destination) throws UnavailableTransportFault_Exception, JAXRException{

		//Check if there are sufficient verifications
		String [] norte = {"Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança"};
		//		String [] centro = {"Lisboa" , "Leiria", "Santaré́m", "Castelo Branco", "Coimbra", "Aveiro", 
		//				"Viseu", "Guarda"};
		String [] sul = {"Setúbal","É́vora","Portalegre","Beja","Faro"};

		for (String n: norte){
			for (String s :sul){

				if (  (origin.equals(n)&&destination.equals(s)) || (origin.equals(s)&&destination.equals(n))  ){
					return null;
				}
			}
			if ( origin.equals(n) || destination.equals(n) ){
				tid="2";
				return new TransporterClient("http://localhost:9090", "UpaTransporter2");
			}
		}
		tid="1";	
		return new TransporterClient("http://localhost:9090", "UpaTransporter1");
	}

	public TransportStateView convertState(JobStateView state){
		if (state==JobStateView.values()[3]){
			return TransportStateView.values()[4];
		}
		else if (state==JobStateView.values()[4]){
			return TransportStateView.values()[5];
		}
		else if (state==JobStateView.values()[5]){
			return TransportStateView.values()[6];
		}
		return TransportStateView.BOOKED;
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

		for (TransportView element : transporters) {
			int key = Integer.parseInt(element.getId());
			if(key>id){
				id=key;
			}
		}
		return String.valueOf(id+1);
	}

	//		for(Map.Entry<String, Transport> entry : transporters.entrySet()) {
	//			int key = Integer.parseInt(entry.getKey());
	//			if(key>id){
	//				id=key;
	//			}
	//		}
	//		return String.valueOf(id+1);
	//	}

	@Override
	public String ping(String name) {

		return "Just Pinged " + name;
	}


	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {


		if(origin==null||destination==null){
			UnknownLocationFault b = new UnknownLocationFault();
			throw new UnknownLocationFault_Exception("Invalid Transport", b);
		}

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

		try {
			if(getTransp(origin, destination)==null){
				UnavailableTransportFault b = new UnavailableTransportFault();
				b.setOrigin(origin);
				b.setDestination(destination);
				throw new UnavailableTransportFault_Exception("Viagem indisponivel", b);
			}
		} catch (JAXRException e1) {
			e1.printStackTrace();
		}
		String id = getNextId();

		//Requested
		Transport t = new Transport(id,origin,destination ,price, null,TransportStateView.values()[0]);

		JobView j1 = null;
		JobView j2 = null;

		//Check transporters for jobs
		try {
			j1 = t1.requestJob(origin, destination, price);
			j2 = t2.requestJob(origin, destination, price);
		} catch (BadLocationFault_Exception e) {
			e.printStackTrace();
		} catch (BadPriceFault_Exception e) {
			e.printStackTrace();
		}

		if (j1==null&&j2==null){
			UnavailableTransportFault b = new UnavailableTransportFault();
			b.setOrigin(origin);
			b.setDestination(destination);
			throw new UnavailableTransportFault_Exception("Viagem indisponivel", b);
		}

		//Por a budgeted
		t.getJob().setState(TransportStateView.values()[1]);


		if ( j1==null && j2.getJobPrice()<=price ){
			try {
				t2.decideJob(j2.getJobIdentifier(), true);
				t.getJob().setTransporterCompany("2");
				t.getJob().setPrice(j2.getJobPrice());
			} catch (BadJobFault_Exception e) {
				e.printStackTrace();
			}
		}

		else if (j2==null && j1.getJobPrice()<=price){
			try {
				t1.decideJob(j1.getJobIdentifier(), true);
				t.getJob().setTransporterCompany("1");
				t.getJob().setPrice(j1.getJobPrice());
			} catch (BadJobFault_Exception e) {
				e.printStackTrace();
			}
		}

		else if (j1==null && j2.getJobPrice()>price){
			t.getJob().setState(TransportStateView.values()[2]);
			UnavailableTransportPriceFault b = new UnavailableTransportPriceFault();
			b.setBestPriceFound(price);
			throw new UnavailableTransportPriceFault_Exception("There's no transport with the desired price, E:1", b);
		}

		else if (j2==null && j1.getJobPrice()>price){
			t.getJob().setState(TransportStateView.values()[2]);
			UnavailableTransportPriceFault b = new UnavailableTransportPriceFault();
			b.setBestPriceFound(price);
			throw new UnavailableTransportPriceFault_Exception("There's no transport with the desired price, E:2", b);
		}


		else if(j1.getJobPrice()>price&&j2.getJobPrice()>price){
			t.getJob().setState(TransportStateView.values()[2]);
			UnavailableTransportPriceFault b = new UnavailableTransportPriceFault();
			b.setBestPriceFound(price);
			throw new UnavailableTransportPriceFault_Exception("There's no transport with the desired price, E:3", b);
		}

		else{

			//DecideJob
			//Will accept the job with lower price and reject the other
			if(j1.getJobPrice()<j2.getJobPrice()){
				try {
					t1.decideJob(j1.getJobIdentifier(), true);
					t.getJob().setTransporterCompany("1");
					t.getJob().setPrice(j1.getJobPrice());
					t2.decideJob(j2.getJobIdentifier(), false);
				} catch (BadJobFault_Exception e) {
					e.printStackTrace();
				}
			}
			else if (j1.getJobPrice()>j2.getJobPrice()){
				try {
					t1.decideJob(j1.getJobIdentifier(), false);
					t2.decideJob(j2.getJobIdentifier(), true);
					t.getJob().setTransporterCompany("2");
					t.getJob().setPrice(j2.getJobPrice());
				} catch (BadJobFault_Exception e) {
					e.printStackTrace();
				}
			}

			else{
				try {
					t1.decideJob(j1.getJobIdentifier(), true);
					t.getJob().setTransporterCompany("1");
					t.getJob().setPrice(j1.getJobPrice());
					t2.decideJob(j2.getJobIdentifier(), false);
				} catch (BadJobFault_Exception e) {
					e.printStackTrace();
				}

			}

		}


		//Booked
		t.getJob().setState(TransportStateView.values()[3]);

		transporters.add(t.getJob());
		//transporters.put(id, t);

		if (bid.equals("0")){

			try {
				System.out.println("Updating secundary server");
				UpdateS2 u = new UpdateS2();
				u.pushUpdate(t.getJob());
			} catch (JAXRException e2) {
				e2.printStackTrace();
			}
		}
		return t.getJob().getId();
	}






	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {

		int i = 0;
		int j = 0;

		if (id==null){
			UnknownTransportFault b = new UnknownTransportFault();
			b.setId(id);
			throw new UnknownTransportFault_Exception("Invalid id", b);
		}
		TransportStateView tsv = null;

		for (TransportView element : transporters) {
			String key = element.getId();
			if(element.getTransporterCompany().equals("1")){
				i++;
			}
			if (element.getTransporterCompany().equals("2")){
				j++;
			}
			if(id.equals(key)){
				if (element.getTransporterCompany().equals("1")&& t1.jobStatus(Integer.toString(i))!=null){

					if ( t1.jobStatus(Integer.toString(i)).getJobState()!=null){
						tsv = convertState(t1.jobStatus(Integer.toString(i)).getJobState());
					}
				}
				else if (element.getTransporterCompany().equals("2")&& t2.jobStatus(Integer.toString(i))!=null){

					if (t1.jobStatus(Integer.toString(j)).getJobState()!=null){

						tsv = convertState(t2.jobStatus(Integer.toString(j)).getJobState());
					}

				}

				if (tsv==null){

					return element;
				}

				element.setState(tsv);
				return element;

			}
		}
		UnknownTransportFault b = new UnknownTransportFault();
		b.setId(id);
		throw new UnknownTransportFault_Exception("O id é inválido", b);


	}

	@Override
	public List<TransportView> listTransports() {

		return transporters;

		//		List<TransportView> list = new ArrayList<>();
		//		for(Map.Entry<String, Transport> entry : transporters.entrySet()) {
		//			TransportView value = entry.getValue().getJob();
		//			list.add(value);
		//		}
		//		return list;
	}

	@Override
	public void clearTransports() {

		if (bid.equals("0")){

			try {
				UpdateS2 s2 = new UpdateS2();
				s2.pushClearTransports();
			} catch (JAXRException e) {
				e.printStackTrace();
			}

		}


		transporters.clear();
		t1.clearJobs();
		t2.clearJobs();

	}


	@Override
	public int imAlive() {
		return 1;
	}


	@Override
	public List<TransportView> update(TransportView info) {

		if (info!=null){
			transporters.add(info);
		}
		return transporters;
	}


}
