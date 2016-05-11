package pt.upa.broker;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPort;

public class BrokerApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(BrokerApplication.class.getSimpleName() + " starting...");
		// Check arguments
		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName wsURL%n", BrokerApplication.class.getName());
			return;
		}

		String uddiURL = args[0];
		String name = args[1];
		String url = args[2];


		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		try {
			
			if (name.equals("UpaBroker0")){
				name="UpaBroker";
				endpoint = Endpoint.create(new BrokerPort("0"));
			}
			else if (name.equals("UpaBroker3")){
				endpoint = Endpoint.create(new BrokerPort("3"));
			}

			// publish endpoint
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);
			
			// publish to UDDI
			System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(name, url);

			
//			endpoint2 = Endpoint.create(new BrokerPort());
//			// publish endpoint2
//			System.out.printf("S2: Starting %s%n", url2);
//			endpoint2.publish(url2);
//			
//			
//			// publish to UDDI S2
//			System.out.printf("S2: Publishing '%s' to UDDI at %s%n", name2, uddiURL);
//			uddiNaming2 = new UDDINaming(uddiURL);
//			uddiNaming2.rebind(name2, url2);



			// wait
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();

		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} finally {
			try {
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}

			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind(name);
					System.out.printf("Deleted '%s' from UDDI%n", name);
				}

			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}

	}

}