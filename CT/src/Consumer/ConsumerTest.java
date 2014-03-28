package Consumer;

import junit.framework.TestCase;

/**Case to test Consumer states
 */
public class ConsumerTest extends TestCase {
	
	Consumer c = new Consumer();
	RandomConsumerData rcd = new RandomConsumerData();
	
	/**Test that Consumers are set to the default.
	 */
	public void testConsumerDefault(){
		boolean defaultConsumer = false;
		assertEquals(c.location, -1);
		assertEquals(c.accelerometer, defaultConsumer);
		assertEquals(c.gps, defaultConsumer);
		assertEquals(c.gravity, defaultConsumer);
		assertEquals(c.orientation, defaultConsumer);
		assertEquals(c.temperature, defaultConsumer);
	}
	
	/**Test all consumer state is set correctly.
	 */
	public void testSetConsumer(){
		c = new Consumer(5,false,false,true,true,false);
		assertEquals(c.location, 5);
		assertEquals(c.accelerometer, false);
		assertEquals(c.gps, false);
		assertEquals(c.gravity, true);
		assertEquals(c.orientation, true);
		assertEquals(c.temperature,false);
	}
	
	/**Tests only one value at most is true. 
	 */
	public void testRandomSingleSensor(){
		
		rcd.randomSingleSensor(c);
		assertTrue(c.accelerometer^c.gps^c.gravity^c.orientation^c.temperature);
	}

}