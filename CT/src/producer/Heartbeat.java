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
		pr.producerHeartBeat(p);
		//check status?
	}

}
