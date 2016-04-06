package pt.upa.transporter.ws;

public class Job {
	
	JobView jv;
	
	public Job(String origin, String destination, String companyName, String identifier, int price, JobStateView state){
		jv = new JobView();
		jv.setCompanyName(companyName);
		jv.setJobOrigin(origin);
		jv.setJobDestination(destination);
		jv.setJobPrice(price);
		jv.setJobState(state);
		jv.setJobIdentifier(identifier);
		
	}
	
	public JobView getJob(){
		return this.jv;
	}

	public static boolean checkValidLocation(String location){
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


}
