package pt.upa.broker.ws.it;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.ws.BindingProvider;


import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.UnknownLocationFault_Exception;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import static org.junit.Assert.*;

import java.util.Map;


/**
 *  Integration Test example
 *  
 *  Invoked by Maven in the "verify" life-cycle phase
 *  Should invoke "live" remote servers 
 */
public class ExampleIT {

	// static members

	private static BrokerPortType port;

	// one-time initialization and clean-up

	@BeforeClass
	public static void oneTimeSetUp() throws Exception{

		String uddiURL = "http://localhost:9090";
		String name="UpaBroker";

		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		String endpointAddress = uddiNaming.lookup(name);

		BrokerService service = new BrokerService();
		port = service.getBrokerPort();

		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}

	@AfterClass
	public static void oneTimeTearDown() {
		port = null;

	}


	// members


	// initialization and clean-up for each test

	@Before
	public void setUp() {
		port.clearTransports();
	}

	@After
	public void tearDown() {
	}


	// tests

	@Test
	public void testPing() {
		
		String actual = "";
		String expected = "Just Pinged test";
		
		actual =  port.ping("test");
		
		assertEquals(expected, actual);
	}

	//Testes do requestTransport
	
	@Test(expected=UnknownLocationFault_Exception.class)
	public void unknownLocationFaultRequestTransport(){
		
	}
}
