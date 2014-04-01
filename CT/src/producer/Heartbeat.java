package producer;

public class Heartbeat implements Runnable {

	Producer p = null;
	ProducerRegister pr = null;
	
	public Heartbeat(Producer p, ProducerRegister pr){
		this.p = p;
		this.pr = pr;
	}
	
	@Override
	public void run() {	
		while(true){
			int status = pr.producerHeartBeat(p);
			System.out.println("Status is" + status);
			Thread.currentThread();
			try{
				Thread.sleep(1200);
			}catch(Exception e){ System.out.println("Failed to wait");}
		}
		//check status?
	}

}
