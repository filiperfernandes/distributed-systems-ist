package pt.upa.broker;

import pt.upa.broker.ws.cli.BrokerClient;

public class BrokerClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(BrokerClientApplication.class.getSimpleName() + " starting...");

		String uddiURL = args[0];
		String name = args[1];

		System.out.println(uddiURL+name);
		BrokerClient client = new BrokerClient(uddiURL, name);
		
		
		//client.requestTransport("Lisboa", "Porto", 5);
		
		System.out.println("............");
		//Thread.sleep(1000);
		
		System.out.println(client.listTransports());
		
		System.out.println(client.viewTransport("1").getOrigin()+client.viewTransport("1").getDestination());

	}
}