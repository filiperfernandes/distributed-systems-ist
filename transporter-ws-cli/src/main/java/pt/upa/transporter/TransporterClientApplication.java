package pt.upa.transporter;

import pt.upa.transporter.ws.cli.TransporterClient;

public class TransporterClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(TransporterClientApplication.class.getSimpleName() + " starting...");

		
		
		String uddiURL = args[0];
		String name = args[1];
		
		System.out.println(uddiURL+name);
		TransporterClient client = new TransporterClient(uddiURL, name);

		System.out.println(client.ping("123"));
		
		
//		// Check arguments
//		if (args.length < 2) {
//			System.err.println("Argument(s) missing!");
//			System.err.printf("Usage: java %s uddiURL name%n", TransporterClientApplication.class.getName());
//			return;
//		}
//
//		System.out.printf("Contacting UDDI at %s%n", uddiURL);
//		UDDINaming uddiNaming = new UDDINaming(uddiURL);
//
//		System.out.printf("Looking for '%s'%n", name);
//		String endpointAddress = uddiNaming.lookup(name);
//
//		if (endpointAddress == null) {
//			System.out.println("Not found!");
//			return;
//		} else {
//			System.out.printf("Found %s%n", endpointAddress);
//		}
		

//		System.out.println("Creating stub ...");
//		TransporterService service = new TransporterService();
//		TransporterPortType port = service.getTransporterPort();
//
//		System.out.println("Setting endpoint address ...");
//		BindingProvider bindingProvider = (BindingProvider) port;
//		Map<String, Object> requestContext = bindingProvider.getRequestContext();
//		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
//
//		System.out.println("Remote call ...");
//		port.clearJobs();
//		System.out.println(port.listJobs());
//		System.out.println(port.requestJob("Porto", "Braga", 10));
//		System.out.println(port.jobStatus("1").getJobOrigin()+ port.jobStatus("1").getJobDestination()+ port.jobStatus("1").getJobPrice()+port.jobStatus("1").getCompanyName()+port.jobStatus("1").getJobState() );
//		port.decideJob("1", true);
//		TimeUnit.SECONDS.sleep(9);
//		System.out.println(port.jobStatus("1").getJobState());

//		
//		port.clearJobs();
		
	//	System.out.println(port.listJobs());
		//		port.clearJobs();
//		System.out.println(port.listJobs());

	}
}

