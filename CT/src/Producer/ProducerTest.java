package Producer;
import junit.framework.TestCase;


/**Tests to make sure constructors are working properley
 *
 */
public class ProducerTest extends TestCase {
	
	RandomProducerData rpd = new RandomProducerData();	
	Producer p = new Producer();
	
	/**Test set default state is correct
	 */
	public void testProducerDefaults(){
		
		boolean defaultSensor = false;
		
		assertEquals(p.location, -1);
		assertEquals(p.connection_limit,0);
		assertEquals(p.accelerometer, defaultSensor);
		assertEquals(p.gps, defaultSensor);
		assertEquals(p.gravity, defaultSensor);
		assertEquals(p.orientation, defaultSensor);
		assertEquals(p.temperature, defaultSensor);
	}
	
	
	/**Test set state is correctly done. 
	 */
	public void testProducerSet(){
		
		p = new Producer(2,6,false,false,true,true,false);
		
		assertEquals(p.location, 2);
		assertEquals(p.connection_limit,6);
		assertEquals(p.accelerometer, false);
		assertEquals(p.gps, false);
		assertEquals(p.gravity, true);
		assertEquals(p.orientation, true);
		assertEquals(p.temperature, false); 
	}
	
}