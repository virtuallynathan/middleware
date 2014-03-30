package Producer;
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
		p = randomSensors(p);		
		return p;
	}	
	
	/**Set a random location
	 * @param p
	 * @return
	 */
	public Producer randomLocation(Producer p){
		Random rand = new Random();		
		p.location = rand.nextInt(MAX_NUMBER_LOCATIONS-1);
		return p;
	}
	
	/**Set a random connection limit 
	 * @param p
	 * @return
	 */
	public Producer randomConnections(Producer p){
		Random rand = new Random();		
		p.location = rand.nextInt(MAX_NUMBER_CONNECTIONS-1);
		return p;
	}
	
	/**Set Random sensor values
	 * @param p
	 * @return
	 */
	public Producer randomSensors(Producer p){
		Random rand = new Random();
		p.accelerometer = rand.nextBoolean();
		p.gps = rand.nextBoolean();
		p.light = rand.nextBoolean();
		p.temperature = rand.nextBoolean();
		p.orientation = rand.nextBoolean();		
		return p;
	}

}
