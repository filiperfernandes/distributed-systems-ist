package pt.upa.transporter.ws.it;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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


	//Testes requestJob

	@Test(expected = BadLocationFault_Exception.class)  
	public void badLocationOrigemRequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		port.requestJob("Paris", "Porto", 10);
	}

	@Test(expected = BadLocationFault_Exception.class)  
	public void badLocationDestinoRequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		port.requestJob("Lisboa", "Barcelona", 10);
	}

	@Test(expected = BadPriceFault_Exception.class)  
	public void badPriceFaultRequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		port.requestJob("Lisboa", "Porto", -5);
	}



	@Test
	public void priceHighRequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {


		JobView actual = port.requestJob("Lisboa", "Porto", 101);

		assertNull(actual);

	}
	
	
	/* Teste da proposta de job entre os 10 e 100 euros
	@Test
	public void priceJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {


		JobView actual = port.requestJob("Lisboa", "Porto", 50);

		assertNull(actual);

	}*/

	@Test
	public void priceLowResquestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		JobView actual = port.requestJob("Lisboa", "Porto", 4);
		assertEquals("Lisboa", actual.getJobOrigin());
		assertEquals("Porto", actual.getJobDestination());
		//assertEquals("1", actual.getJobIdentifier());
		//assertEquals(4, actual.getJobPrice());
		assertEquals(JobStateView.values()[0], actual.getJobState());

	}

	@Test
	public void upaTransporter1RequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		String expected = "UpaTransporter1";

		String actual =  port.requestJob("Lisboa", "Beja", 5).getCompanyName();

		assertEquals(expected, actual);
		// if the assert fails, the test fails
	}
	
	@Test
	public void upaTransporter2RequestJobTest() throws BadLocationFault_Exception, BadPriceFault_Exception {

		String expected = "UpaTransporter2";

		String actual =  port.requestJob("Leiria", "Braga", 5).getCompanyName();

		assertEquals(expected, actual);
		// if the assert fails, the test fails
	}

	//Testes decideJob

	@Test(expected=BadJobFault_Exception.class)
	public void badJobFaultDecideJobTest() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception {
		
		port.requestJob("Lisboa", "Porto", 55);
		
		port.decideJob("100", false);

	}
	
	@Test
	public void acceptedDecideJobTest() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
		
		port.requestJob("Lisboa", "Porto", 5);

		JobStateView expected = JobStateView.values()[2];
		JobStateView actual = port.decideJob("1", true).getJobState();
		
		assertEquals(expected, actual);
		
	}
		
	@Test
	public void rejectedDecideJobTest() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
		
		port.requestJob("Lisboa", "Porto", 5);

		JobStateView expected = JobStateView.values()[1];
		JobStateView actual = port.decideJob("1", false).getJobState();
		
		assertEquals(expected, actual);
		
	}
	
	//Testes jobStatus
	
	@Test
	public void nullJobStatusTest(){
		
		JobView actual = port.jobStatus("1234");
		
		assertNull(actual);
	}
	
	@Test
	public void listJobStatusTest() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
		
		port.requestJob("Lisboa", "Porto", 5);
		JobView actual = port.jobStatus("1");
		
		
		//jobs.put("1", new Job("Lisboa", "Porto", "UpaTransporter1", "1", 10, JobStateView.values()[0]));
		
		
		assertEquals("UpaTransporter2", actual.getCompanyName());
		assertEquals("1", actual.getJobIdentifier());
		assertEquals("Lisboa", actual.getJobOrigin());
		assertEquals("Porto", actual.getJobDestination());
		assertEquals(JobStateView.values()[0], actual.getJobState()); /// verificar depois de fazer a limpeza
		
	}	
}