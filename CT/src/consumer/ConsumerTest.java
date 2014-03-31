package consumer;

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
		assertEquals(c.getLocation(), -1);
		assertEquals(c.isAccelerometer(), defaultConsumer);
		assertEquals(c.isGps(), defaultConsumer);
		assertEquals(c.isLight(), defaultConsumer);
		assertEquals(c.isOrientation(), defaultConsumer);
		assertEquals(c.isTemperature(), defaultConsumer);
	}
	
	/**Test all consumer state is set correctly.
	 */
	public void testSetConsumer(){
		c = new Consumer(5,false,false,true,true,false);
		assertEquals(c.getLocation(), 5);
		assertEquals(c.isAccelerometer(), false);
		assertEquals(c.isGps(), false);
		assertEquals(c.isLight(), true);
		assertEquals(c.isOrientation(), true);
		assertEquals(c.isTemperature(),false);
	}
	
	public void testProducerRequest(){
		
		//2,6,"123.11.11","12323",false,false,true,true,false
		
		c = new Consumer(2,false, false, true, true, false);
		ConsumerRequests cr = new ConsumerRequests();
		cr.requestProducer(c);
	}

}
