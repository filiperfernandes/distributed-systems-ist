package pt.upa.transporter.ws;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ChangeStateThread extends Thread{
	
	private Job job;
	
	public ChangeStateThread(Job job) {
		this.job = job;
	}
	
	public void run(){
		
		Random r = new Random();
		int time = r.nextInt(6-1) + 1;
		
		try {
		    TimeUnit.SECONDS.sleep(time);
		    job.getJob().setJobState(JobStateView.values()[3]);
		
		
		    TimeUnit.SECONDS.sleep(time);
		    job.getJob().setJobState(JobStateView.values()[4]);
		
		    TimeUnit.SECONDS.sleep(time);
		    job.getJob().setJobState(JobStateView.values()[5]);
		} catch (InterruptedException e) {
		}
	}
}
