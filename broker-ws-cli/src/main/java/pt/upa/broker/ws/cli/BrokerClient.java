package pt.upa.broker.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class BrokerClient implements BrokerPortType{

	String uddiURL = "";
	String name="";
	// service, port as members
	BrokerPortType stub;
	BrokerService service;

	Map<String, Object> requestContext;

	int retries = 10;

	// constructor
	public BrokerClient(String uddiURL, String name) throws JAXRException {
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
		}

		System.out.println("Creating stub ...");
		service = new BrokerService();
		stub = service.getBrokerPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) stub;
		requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

	}


	private void autoSet() {

		UDDINaming uddiNaming = null;
		String endpointAddress = null;
		int tries = 0;

		while (tries++ < retries) {
			try {
				uddiNaming = new UDDINaming(uddiURL);

				endpointAddress = uddiNaming.lookup(name);
				break;
			} catch (JAXRException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
				System.out.println("Attempting to reconnect");
			}
		}


		if (endpointAddress == null) {
			System.out.println("Service off-line");
			System.exit(-1);
		}
		service = new BrokerService();
		stub = service.getBrokerPort();
		BindingProvider bindingProvider = (BindingProvider) stub;
		requestContext = bindingProvider.getRequestContext();

		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

	}



	@Override
	public List<TransportView> update(TransportView info) {
		return stub.update(info);
	}

	@Override
	public int imAlive() {
		return stub.imAlive();
	}

	@Override
	public String ping(String name) {
		int tries = 0;
		while (tries++ < retries) {
			try {
				String p = stub.ping(name);
				return p;
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
					autoSet();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("Attempting to reconnect");
			}
		}
		return null;
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {


		int tries = 0;
		while (tries++ < retries) {
			try {
				String p = stub.requestTransport(origin, destination, price);
				return p;
			} catch (Exception e) {

				if (e instanceof InvalidPriceFault_Exception
						|| e instanceof UnavailableTransportFault_Exception
						|| e instanceof UnavailableTransportPriceFault_Exception
						|| e instanceof UnknownLocationFault_Exception) {
					throw e;
				}

				try {
					Thread.sleep(1000);
					autoSet();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("Attempting to reconnect");
			}
		}
		return null;
	}


	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {

		int tries = 0;
		while (tries++ < retries) {
			try {
				TransportView p = stub.viewTransport(id);
				return p;
			} catch (Exception e) {

				if (e instanceof UnknownTransportFault_Exception) {
					throw e;
				}

				try {
					Thread.sleep(1000);
					autoSet();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("Attempting to reconnect");
			}
		}
		return null;
	}

	@Override
	public List<TransportView> listTransports() {
		int tries = 0;
		while (tries++ < retries) {
			try {
				List<TransportView> p = stub.listTransports();
				return p;
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
					autoSet();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("Attempting to reconnect");
			}
		}
		return null;
	}



	@Override
	public void clearTransports() {
		int tries = 0;
		while (tries++ < retries) {
			try {
				stub.clearTransports();
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
					autoSet();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("Attempting to reconnect");
			}
		}
	}


}
