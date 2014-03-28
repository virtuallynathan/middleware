package Consumer;

import junit.framework.TestCase;

public class ConsumerTest extends TestCase {
	
	Consumer c = new Consumer();
	RandomConsumerData rcd = new RandomConsumerData();
	
	public void testConsumerDefault(){
		boolean defaultConsumer = false;
		assertEquals(c.location, -1);
		assertEquals(c.accelerometer, defaultConsumer);
		assertEquals(c.gps, defaultConsumer);
		assertEquals(c.gravity, defaultConsumer);
		assertEquals(c.orientation, defaultConsumer);
		assertEquals(c.temperature, defaultConsumer);
	}
	
	public void testSetConsumer(){
		c = new Consumer(5,false,false,true,true,false);
		assertEquals(c.location, 5);
		assertEquals(c.accelerometer, false);
		assertEquals(c.gps, false);
		assertEquals(c.gravity, true);
		assertEquals(c.orientation, true);
		assertEquals(c.temperature,false);
	}

}
