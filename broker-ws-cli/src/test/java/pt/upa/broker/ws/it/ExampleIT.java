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
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

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
	}

	@After
	public void tearDown() {
	}


	// tests



	@Test
	public void test() {
		
		String actual = "";
		String expected = "Just Pinged test";
		
		actual =  port.ping("test");
		
		assertEquals(expected, actual);
		// if the assert fails, the test fails
	}

	/*
	@Test
	public void test01() {

		String actual = "";
		String expected = "A origem está errada.";

		try {
			actual =  port.requestTransport("Barcelona", "Leiria", 5);
		} catch (InvalidPriceFault_Exception | UnavailableTransportFault_Exception
				| UnavailableTransportPriceFault_Exception | UnknownLocationFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(expected, actual);
	}*/
	
	/*
	@Test
	public void test02() {
		
		TransportView actual
		Class<UnknownTransportFault_Exception> expected = UnknownTransportFault_Exception.class;
	
		try {
			actual = port.viewTransport(null);
		} catch (UnknownTransportFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		assertEquals(expected, actual);
	}
	*/
}