package producer;
import java.util.Random;


/**Class to produce random data for producer state
 *
 */
public class RandomProducerData {
	
	//manifest constants
	int MAX_NUMBER_LOCATIONS = 10;
	int MAX_NUMBER_CONNECTIONS = 10;
		
	/**Fill all state with random values
	 * @param p
	 * @return
	 */
	public Producer setAllRandom(Producer p){		
		p = randomLocation(p);
		p = randomConnections(p);
		p = randomPortAndIp(p);
		p = randomSensors(p);		
		return p;
	}	
	
	/**Set a random location
	 * @param p
	 * @return
	 */
	public Producer randomLocation(Producer p){
		Random rand = new Random();		
		p.setLocation(rand.nextInt(MAX_NUMBER_LOCATIONS-1));
		return p;
	}
	
	/**Set a random connection limit 
	 * @param p
	 * @return
	 */
	public Producer randomConnections(Producer p){
		Random rand = new Random();		
		p.setLocation(rand.nextInt(MAX_NUMBER_CONNECTIONS-1));
		return p;
	}
	
	public Producer randomPortAndIp(Producer p){		
		Random r = new Random();
		String ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
		p.setIp_addr(ip);
		String port = String.valueOf(r.nextInt(65536));
		p.setPort(port);		
		return p;
	}
	
	/**Set Random sensor values
	 * @param p
	 * @return
	 */
	public Producer randomSensors(Producer p){
		Random rand = new Random();
		p.setAccelerometer(rand.nextBoolean());
		p.setGps(rand.nextBoolean());
		p.setLight(rand.nextBoolean());
		p.setTemperature(rand.nextBoolean());
		p.setOrientation(rand.nextBoolean());		
		return p;
	}

}
