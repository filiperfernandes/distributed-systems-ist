package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.upa.transporter.ws.TransporterPortType")
public class TransporterPort implements TransporterPortType{

	public static TreeMap<String, Job> jobs = new TreeMap<String, Job>();

	String tid ="";

	public TransporterPort(String tp) {

		this.tid=tp;

	}


	public Integer genRandom(Integer min, Integer max){
		Random r = new Random();
		int result = r.nextInt(max-min) + min;
		return result;
	}

	public String getNextId(){
		Integer id=0;

		for(Map.Entry<String, Job> entry : jobs.entrySet()) {
			int key = Integer.parseInt(entry.getKey());
			if(key>id){
				id=key;
			}
		}
		return String.valueOf(id+1);
	}

	public String getTransp(String origin , String destination){

		//Check if there are sufficient verifications
		String [] norte = {"Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança"};
		//		String [] centro = {"Lisboa" , "Leiria", "Santaré́m", "Castelo Branco", "Coimbra", "Aveiro", 
		//				"Viseu", "Guarda"};
		//		String [] sul = {"Setúbal","É́vora","Portalegre","Beja","Faro"};

		for (String n: norte){
			if ( origin.equals(n) || destination.equals(n) ){
				return "UpaTransporter2";
			}
		}
		return "UpaTransporter1";
	}


	@Override
	public String ping(String name) {
		return "Just Pinged "+name;
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {

		pt.upa.transporter.ws.Job.checkValidLocation(origin);

		if(!(pt.upa.transporter.ws.Job.checkValidLocation(origin))){
			BadLocationFault b = new BadLocationFault();
			b.setLocation(origin);
			throw new BadLocationFault_Exception("A origem está errada.", b);
		}
		if(!(pt.upa.transporter.ws.Job.checkValidLocation(destination))){
			BadLocationFault b = new BadLocationFault();
			b.setLocation(destination);
			throw new BadLocationFault_Exception("O destino está errado.", b);
		}
		if(price<0){
			BadPriceFault p = new BadPriceFault();
			p.setPrice(price);
			throw new BadPriceFault_Exception("Preço menor que zero", p);
		}

		if (tid.equals("1")){
			//Tratamento do UpaTransporter1
			if(getTransp(origin, destination).equals("UpaTransporter2")){
				return null;
			}

			else if(price>100){
				return null;
			}

			else if(price<=10){
				String newId=getNextId();
				Job newJ = new Job(origin, destination, "UpaTransporter1", newId, genRandom(0,price), JobStateView.values()[0]);
				jobs.put(newId, newJ);
				return newJ.getJob();
			}

			else if (price %2 !=0){
				String newId=getNextId();
				Job newJ = new Job(origin, destination, "UpaTransporter1", newId, genRandom(0,price), JobStateView.values()[0]);
				jobs.put(newId, newJ);
				return newJ.getJob();
			}

			else{
				String newId=getNextId();
				Job newJ = new Job(origin, destination, "UpaTransporter1", newId, genRandom(price,101), JobStateView.values()[0]);
				jobs.put(newId, newJ);
				return newJ.getJob();
			}

		}

		else{
			//Tratamento do UpaTransporter2
			if(getTransp(origin, destination).equals("UpaTransporter1")){
				return null;
			}

			else if(price>100){
				return null;
			}

			else if(price<=10){
				String newId=getNextId();
				Job newJ = new Job(origin, destination, "UpaTransporter2", newId, genRandom(0,price), JobStateView.values()[0]);
				jobs.put(newId, newJ);
				return newJ.getJob();
			}

			else if (price %2 ==0){
				String newId=getNextId();
				Job newJ = new Job(origin, destination, "UpaTransporter2", newId, genRandom(0,price), JobStateView.values()[0]);
				jobs.put(newId, newJ);
				return newJ.getJob();
			}

			else{
				String newId=getNextId();
				Job newJ = new Job(origin, destination, "UpaTransporter2", newId, genRandom(price,101), JobStateView.values()[0]);
				jobs.put(newId, newJ);
				return newJ.getJob();
			}

		}

	}



	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {

		for(Map.Entry<String, Job> entry : jobs.entrySet()) {
			String key = entry.getKey();
			Job value = entry.getValue();

			if(id.equals(key) && accept==true){
				value.getJob().setJobState(JobStateView.values()[2]);

				ChangeStateThread p = new ChangeStateThread(value);
				p.start();

				return value.getJob();
			}
			else if(id.equals(key) && accept==false){
				value.getJob().setJobState(JobStateView.values()[1]);
				return value.getJob();
			}
			else{
				BadJobFault j = new BadJobFault();
				j.setId(id);
				throw new BadJobFault_Exception("Invalid Job", j);
			}

		}
		return null;
	}

	@Override
	public JobView jobStatus(String id) {

		for(Map.Entry<String, Job> entry : jobs.entrySet()) {
			String key = entry.getKey();
			Job value = entry.getValue();

			System.out.println(key+value);
			if(id.equals(key)){
				return value.getJob();
			}
		}

		return null;
	}

	@Override
	public List<JobView> listJobs() {
		List<JobView> list = new ArrayList<>();

		for(Map.Entry<String, Job> entry : jobs.entrySet()) {
			JobView value = entry.getValue().getJob();
			list.add(value);
		}
		return list;

	}	

	@Override
	public void clearJobs() {

		jobs.clear();

	}


}
