package consumer;


public class ConsumerAPI {
	
	private final static int REPEATITIONS = 10;
	private final static int WAIT = 12000;

	
	public static void main(String[] args) {
		
		ConsumerAPI capi = new ConsumerAPI();
		ConsumerRequests cr = new ConsumerRequests();
		Consumer c  = new Consumer(2,false, false, true, true, false);
		capi.register(c);
		int result = cr.connectProducer(c);
		System.out.println(result);
		result = cr.disconnectProducer(c);
		System.out.println(result);
	}
	
	
	
	
	public boolean requestIndividual(Consumer c){
		
		ConsumerRequests cr = new ConsumerRequests();		
		for (int i = 0; i < REPEATITIONS; i++) {
			int result = cr.requestProducer(c);
			if(result == 200) return true;	
		}		
		return false;
	}
	
	public boolean register(Consumer c){
		
		if(!requestIndividual(c)){
			try{
				Thread.currentThread();
				Thread.sleep(WAIT);
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);
			}				
			boolean status = requestIndividual(c);		
			if(status == false){			
				System.out.println("Failure to Request");
				return false;
			}
		} return true;
	}
}
