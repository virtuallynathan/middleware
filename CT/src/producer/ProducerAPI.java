package producer;

public class ProducerAPI {
	
	private final static int REPEATITIONS = 10;
	
	public static void main(String[] args) {		
		
		ProducerAPI papi = new ProducerAPI();
		RandomProducerData rpd = new RandomProducerData();
		Producer p = new Producer();		
		rpd.setAllRandom(p);
		papi.run(p);
	}
	
	
	public void run(Producer p){
		
		boolean result = register(p);
		
		//check success
		
		// if failure repeat until tried 10 times then exit
		
		//heart beat every 2 minutes
		
	}
	
	/**Registers producer trying repeating 10 times on failure before
	 * exiting
	 * @param p
	 * @return
	 */
	public boolean register(Producer p){
		
		ProducerRegister pr = new ProducerRegister();		
		for (int i = 0; i < REPEATITIONS; i++) {
			int result = pr.registerProducer(p);
			if(result == 200) return true;	
		}		
		return false;
	}

	
}
