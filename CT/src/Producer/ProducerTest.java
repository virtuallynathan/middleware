package Producer;
import junit.framework.TestCase;


/**Tests to make sure constructors are working properley
 *
 */
public class ProducerTest extends TestCase {
		
		Producer p;
		Producer d;
		
	/* Set up a non-default and default producer
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override protected void setUp()throws Exception{
		
		p = new Producer(2,6,"123.11.11","12323",false,false,true,true,false);
		d = new Producer();		
	}
	
	
	/**Test set default state is correct
	 */
	public void testProducerDefaults(){
		
		System.out.println("this test 1");		
		boolean defaultSensor = false;
		
		assertEquals(d.location, -1);
		assertEquals(d.connection_limit,0);
		assertEquals(d.accelerometer, defaultSensor);
		assertEquals(d.gps, defaultSensor);
		assertEquals(d.light, defaultSensor);
		assertEquals(d.orientation, defaultSensor);
		assertEquals(d.temperature, defaultSensor);
	}
	
	
	/**Test set state is correctly done. 
	 */
	public void testProducerSet(){

		System.out.println("this test 2");

		//Producer p = new Producer(2,6,"123.11.11","12323",false,false,true,true,false);
		
		assertEquals(p.location, 2);
		assertEquals(p.connection_limit,6);
		assertEquals(p.ip_addr,"123.11.11");
		assertEquals(p.port, "12323");
		assertEquals(p.accelerometer, false);
		assertEquals(p.gps, false);
		assertEquals(p.light, true);
		assertEquals(p.orientation, true);
		assertEquals(p.temperature, false); 
		
		p.createJsonNoId();
	}
	
	/**Simply makes call to register producer,
	 * output to console describes success 
	 */
	public void testProducerRegistration(){
		
		System.out.println("this test 3");
		
		ProducerRegister pr = new ProducerRegister();
		pr.registerProducer(p);
	}
	
}
