package pt.upa.broker.ws;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class AliveThread extends Thread{

	String uddiURL = "http://localhost:9090";
	String name="UpaBroker";
	String url = "http://localhost:8083/broker-ws/endpoint";
	// service, port as members
	BrokerPortType stub;
	BrokerService service;
	

	public AliveThread() throws JAXRException{
		

		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			throw new IllegalArgumentException("Não encontrei no uddi");
		} else {
			System.out.printf("Found %s%n", endpointAddress);

			System.out.println("Creating stub ...");
			service = new BrokerService();
			stub = service.getBrokerPort();

			BindingProvider bindingProvider = (BindingProvider) stub;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		}
	}


	public void run(){

		while(true){
			try {
				stub.imAlive();
				System.out.println("Im alive!");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.exit(1);
			} catch (Exception e) {
				try {
					System.out.printf("Renaming '%s' to UDDI at %s%n", name, uddiURL);
					UDDINaming uddiNaming = new UDDINaming(uddiURL);
					uddiNaming.rebind(name, url);
				} catch (JAXRException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			}
		}




	}
}
