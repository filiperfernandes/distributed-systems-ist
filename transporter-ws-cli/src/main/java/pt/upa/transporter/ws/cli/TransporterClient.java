package pt.upa.transporter.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

public class TransporterClient implements TransporterPortType {

	String uddiURL = "";
	String name="";
	// service, port as members
	TransporterPortType stub;
	TransporterService service;

	// constructor
	public TransporterClient(String uddiURL, String name) throws JAXRException {
		this.uddiURL=uddiURL;
		this.name=name;

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
			service = new TransporterService();
			stub = service.getTransporterPort();

			BindingProvider bindingProvider = (BindingProvider) stub;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);


		}}



	// operations

	public String ping(String name) {
		return stub.ping(name);
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		return stub.requestJob(origin, destination, price);
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		return stub.decideJob(id, accept);
	}

	@Override
	public JobView jobStatus(String id) {
		return stub.jobStatus(id);
	}

	@Override
	public List<JobView> listJobs() {
		return stub.listJobs();
	}

	@Override
	public void clearJobs() {
		stub.clearJobs();
	}
}

