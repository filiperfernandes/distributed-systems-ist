package pt.upa.transporter.ws;

public class Job {

	public Job(String origin, String destination, String companyName, String identifier, int price, JobStateView state){
		JobView jv = new JobView();
		jv.setCompanyName(companyName);
		jv.setJobOrigin(origin);
		jv.setJobDestination(destination);
		jv.setJobPrice(price);
		jv.setJobState(state);
		jv.setJobIdentifier(identifier);
	}
}
