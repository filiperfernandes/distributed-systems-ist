package pt.upa.transporter.ws;

import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.upa.transporter.ws.TransporterPortType")
public class TransporterPort implements TransporterPortType{

	@Override
	public String ping(String name) {
		return "just pinged";
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {

		if(origin==null){
			BadLocationFault b = new BadLocationFault();
			b.setLocation(origin);
			throw new BadLocationFault_Exception("A origem está errada.", b);
		}
		if(destination==null){
			BadLocationFault b = new BadLocationFault();
			b.setLocation(destination);
			throw new BadLocationFault_Exception("O destino está errado.", b);
		}
		if(price<0){
			BadPriceFault p = new BadPriceFault();
			p.setPrice(price);
			throw new BadPriceFault_Exception("Preço menor que zero", p);
		}
		if(price>100){
			return null;
		}

		return null;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobView jobStatus(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JobView> listJobs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearJobs() {
		// TODO Auto-generated method stub

	}


}
