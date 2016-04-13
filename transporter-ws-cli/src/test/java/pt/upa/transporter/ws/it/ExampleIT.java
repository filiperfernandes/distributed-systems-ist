package pt.upa.transporter.ws.it;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

/**
 *  Integration Test example
 *  
 *  Invoked by Maven in the "verify" life-cycle phase
 *  Should invoke "live" remote servers 
 */
public class ExampleIT {

	// static members

	private static TransporterPortType port;
	private static TransporterPortType port2;
	// one-time initialization and clean-up

	@BeforeClass
	public static void oneTimeSetUp() throws Exception {

		String uddiURL = "http://localhost:9090";
		String name="UpaTransporter1";

		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		String endpointAddress = uddiNaming.lookup(name);

		TransporterService service = new TransporterService();
		port = service.getTransporterPort();

		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		
		String name2 ="UpaTransporter2";
		endpointAddress = uddiNaming.lookup(name2);
		service = new TransporterService();
		port2 = service.getTransporterPort();
		BindingProvider bindingProvider2 = (BindingProvider) port2;
		Map<String, Object> requestContext2 = bindingProvider2.getRequestContext();
		requestContext2.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		
		
	}

	@AfterClass
	public static void oneTimeTearDown() {
		port=null;
	}


	// members


	// initialization and clean-up for each test

	@Before
	public void setUp() {
		port.clearJobs();
	}

	@After
	public void tearDown() {
	}


	// tests
	@Test
	public void testPing() {

		String expected = "Just Pinged test";

		String actual =  port.ping("test");

		assertEquals(expected, actual);
	}

	//Testes requestJob

	@Test(expected = BadLocationFault_Exception.class)  
	public void badLocationOrigemRequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		port2.requestJob("Paris", "Porto", 10);
	}

	@Test(expected = BadLocationFault_Exception.class)  
	public void badLocationDestinoRequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		port.requestJob("Lisboa", "Barcelona", 10);
	}

	@Test(expected = BadPriceFault_Exception.class)  
	public void badPriceFaultRequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		port2.requestJob("Lisboa", "Porto", -5);
	}



	@Test
	public void priceHighRequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {


		JobView actual = port2.requestJob("Lisboa", "Porto", 101);

		assertNull(actual);
			
	}

	@Test
	public void priceJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {


		JobView actual = port2.requestJob("Lisboa", "Porto", 50);

		assertTrue(actual instanceof JobView );

	}
	
		//Sucesso
	@Test
	public void priceLowResquestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		JobView actual = port.requestJob("Lisboa", "Beja", 4);
		
		assertTrue( actual instanceof JobView);

	}
		//Sucesso UpaTransporter 1
	@Test
	public void upaTransporter1RequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		String actual =  port.requestJob("Lisboa", "Beja", 5).getCompanyName();

		assertEquals("UpaTransporter1", actual);

	}
		//Sucesso UpaTransporter 2
	@Test
	public void upaTransporter2RequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		String actual =  port2.requestJob("Leiria", "Braga", 5).getCompanyName();

		assertEquals("UpaTransporter2", actual);
	}

	//Testes decideJob

	@Test(expected=BadJobFault_Exception.class)
	public void badJobFaultDecideJobTest() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception {

		port.requestJob("Lisboa", "Beja", 55);

		port.decideJob("100", false);

	}	

	@Test
	public void acceptedDecideJobTest() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{

		port.requestJob("Lisboa", "Beja", 5);

		JobStateView expected = JobStateView.values()[2];
		JobStateView actual = port.decideJob("1", true).getJobState();

		assertEquals(expected, actual);

	}

	@Test
	public void rejectedDecideJobTest() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{

		port.requestJob("Lisboa", "Beja", 5);

		JobStateView expected = JobStateView.values()[1];
		JobStateView actual = port.decideJob("1", false).getJobState();

		assertEquals(expected, actual);

	}

	//Testes jobStatus

	@Test
	public void nullJobStatusTest01(){

		JobView actual = port.jobStatus("---");

		assertNull(actual);
	}
	
	@Test
	public void nullJobStatusTest02(){

		JobView actual = port.jobStatus("a21d");

		assertNull(actual);
	}

		@Test
		public void listJobStatusTest() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
			
			port2.requestJob("Lisboa", "Braga", 5);
			
			JobView actual = port2.jobStatus("1");
			
			assertTrue(actual instanceof JobView);
		}	
}