package producer;


public class ProducerAPI {
	
	private final static int REPEATITIONS = 10;
	private final static int WAIT = 3000;
	
	public static void main(String[] args) {		
		
		ProducerAPI papi = new ProducerAPI();
		RandomProducerData rpd = new RandomProducerData();
		Producer p = new Producer();		
		rpd.setAllRandom(p);
		papi.run(p);
	}
	
	
	public void run(Producer p){
		
		if (!register(p))System.out.println("Failure to register");
		//successful registration
		heartbeat(p);
		
		
		
		
		}
		
		
		//heart beat every 2 minutes
		
	
	/**Registers producer trying repeating 10 times on failure before
	 * exiting
	 * @param p
	 * @return
	 */
	public boolean registerIndividual(Producer p){
		
		ProducerRegister pr = new ProducerRegister();		
		for (int i = 0; i < REPEATITIONS; i++) {
			int result = pr.registerProducer(p);
			if(result == 200) return true;	
		}		
		return false;
	}
	
	
	/**Loop to try register and repeat 10 times,
	 * before waiting and repeating again.
	 * @param p
	 */
	public boolean register(Producer p){
		
		if(!registerIndividual(p)){
			try{
				Thread.currentThread();
				Thread.sleep(WAIT);
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);
			}				
			boolean status = registerIndividual(p);		
			if(status == false){			
				System.out.println("Failure to Register");
				return false;
			}
		} return true;
	}
	
	
	/**
	 * @param p
	 */
	public void heartbeat(Producer p){
		ProducerRegister pr = new ProducerRegister();		
		Thread t = new Thread(new Heartbeat(p,pr));
		t.start();
		
	}
	

	
}
