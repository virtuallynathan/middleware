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
		c.setLocation(rand.nextInt(MAX_NUMBER_LOCATIONS-1));
		return c;
	}
	
	/**Randomise whether each sensor is true or false, can result
	 * in multiple sensors requested.
	 * @param c
	 * @return
	 */
	public Consumer randomSensors(Consumer c){		
		Random rand = new Random();
		c.setAccelerometer(rand.nextBoolean());
		c.setGps(rand.nextBoolean());
		c.setLight(rand.nextBoolean());
		c.setOrientation(rand.nextBoolean());
		c.setTemperature(rand.nextBoolean());		
		return c;		
	}
	
	public Consumer randomSingleSensor(Consumer c){
		Random rand = new Random();
		int choice = rand.nextInt(5);
		c.setAllSensors(false);
		switch (choice) {
		case 1:
			c.setAccelerometer(true);
			break;
		case 2:
			c.setGps(true);
			break;
		case 3:
			c.setLight(true);
			break;
		case 4:
			c.setOrientation(true);
			break;
		case 5:
			c.setTemperature(true);			
			break;
		}	return c;
	}

}
