package pt.upa.transporter.ws.it;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import static org.junit.Assert.assertEquals;

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
	}

	@After
	public void tearDown() {
	}}


	// tests

//	@Test
//	public void test01() {
//
//		String actual = "";
//		String expected = "UpaTransporter1";
//
//		try {
//			actual =  port.requestJob("Lisboa", "Leiria", 5).getCompanyName();
//		} catch (BadLocationFault_Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BadPriceFault_Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(expected, actual);
//		// if the assert fails, the test fails
//	}
//
//	@Test
//	public void test02() {
//		JobStateView actual = JobStateView.values()[0];
//		JobStateView expected = JobStateView.values()[1];
//
//		try {
//			port.decideJob("1", false);
//			actual = port.jobStatus("1").getJobState();
//		} catch (BadJobFault_Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		assertEquals(expected, actual);
//	}
//
//	@Test
//	public void test03() {
//		JobStateView actual = JobStateView.values()[0];
//		JobStateView expected = JobStateView.values()[2];
//
//		try {
//			port.decideJob("1", true);
//			actual = port.jobStatus("1").getJobState();
//		} catch (BadJobFault_Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		assertEquals(expected, actual);
//	}
//
//	@Test
//	public void test04() {
//		JobView actual;
//		JobView expected = null;
//
//		actual=port.jobStatus("20");
//
//		assertEquals(expected, actual);
//	}
//	
//	
//}