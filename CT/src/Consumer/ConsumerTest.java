package Consumer;

import junit.framework.TestCase;

/**Case to test Consumer states
 */
public class ConsumerTest extends TestCase {
	
	Consumer c;
	RandomConsumerData rcd;
	
	@Override protected void setUp()throws Exception{
		
		c = new Consumer();
		rcd = new RandomConsumerData();
	
	}
	
	
	/**Test that Consumers are set to the default.
	 */
	public void testConsumerDefault(){
		boolean defaultConsumer = false;
		assertEquals(c.location, -1);
		assertEquals(c.accelerometer, defaultConsumer);
		assertEquals(c.gps, defaultConsumer);
		assertEquals(c.light, defaultConsumer);
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
		assertEquals(c.light, true);
		assertEquals(c.orientation, true);
		assertEquals(c.temperature,false);
	}
	
	/**Tests only one value at most is true. 
	 */
	public void testRandomSingleSensor(){
		
		rcd.randomSingleSensor(c);
		assertTrue(c.accelerometer^c.gps^c.light^c.orientation^c.temperature);
	}
	
	public void testProducerRequest(){
		
		//2,6,"123.11.11","12323",false,false,true,true,false
		
		c = new Consumer(2,false, false, true, true, false);
		ConsumerRequests cr = new ConsumerRequests();
		cr.requestProducer(c);
	}

}
