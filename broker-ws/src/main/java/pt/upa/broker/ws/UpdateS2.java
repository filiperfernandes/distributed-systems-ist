package pt.upa.broker.ws;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class UpdateS2 {

	String uddiURL = "http://localhost:9090";
	String name="UpaBroker3";
	// service, port as members
	BrokerPortType stub;
	BrokerService service;
	
	// constructor
	public UpdateS2() throws JAXRException {

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

	public void pushClearTransports() {
		stub.clearTransports();

	}

	
	public void pushUpdate(TransportView info){
		stub.update(info);
	}


}
