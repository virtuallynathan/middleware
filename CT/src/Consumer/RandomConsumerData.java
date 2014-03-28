package Consumer;
import java.util.Random;

/**Generates random state for all consumers
 */
public class RandomConsumerData {
	
	//manifest constants
	int MAX_NUMBER_LOCATIONS = 10;
	
	public Consumer setAllRandom(Consumer c){		
		c = randomLocation(c);
		c = randomSensors(c);		
		return c;
	}
	
	/**Sets a random value for requested location
	 * @param c
	 * @return c
	 */
	public Consumer randomLocation(Consumer c){		
		Random rand = new Random();
		c.location = rand.nextInt(MAX_NUMBER_LOCATIONS-1);
		return c;
	}
	
	/**Randomise whether each sensor is true or false, can result
	 * in multiple sensors requested.
	 * @param c
	 * @return
	 */
	public Consumer randomSensors(Consumer c){		
		Random rand = new Random();
		c.accelerometer = rand.nextBoolean();
		c.gps = rand.nextBoolean();
		c.gravity = rand.nextBoolean();
		c.orientation = rand.nextBoolean();
		c.temperature = rand.nextBoolean();		
		return c;		
	}
	
	public Consumer randomSingleSensor(Consumer c){
		Random rand = new Random();
		int choice = rand.nextInt(5);
		c.setAllSensors(false);
		switch (choice) {
		case 1:
			c.accelerometer = true;
			break;
		case 2:
			c.gps = true;
			break;
		case 3:
			c.gravity = true;
			break;
		case 4:
			c.orientation = true;
			break;
		case 5:
			c.temperature = true;			
			break;
		}	return c;
	}

}
