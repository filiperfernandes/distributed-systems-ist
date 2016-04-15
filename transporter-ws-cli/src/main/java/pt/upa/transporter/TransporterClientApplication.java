package pt.upa.transporter;

import pt.upa.transporter.ws.cli.TransporterClient;

public class TransporterClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(TransporterClientApplication.class.getSimpleName() + " starting...");

		
		
		String uddiURL = args[0];
		String name = args[1];
		
		System.out.println(uddiURL+name);
		TransporterClient client = new TransporterClient(uddiURL, name);


	}
}

