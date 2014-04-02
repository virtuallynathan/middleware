package producer;
import junit.framework.TestCase;


/**Tests to make sure constructors are working properly
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
		
		boolean defaultSensor = false;		
		assertEquals(d.getLocation(), -1);
		assertEquals(d.getConnection_limit(),0);
		assertEquals(d.isAccelerometer(), defaultSensor);
		assertEquals(d.isGps(), defaultSensor);
		assertEquals(d.isLight(), defaultSensor);
		assertEquals(d.isOrientation(), defaultSensor);
		assertEquals(d.isTemperature(), defaultSensor);
	}
	
	
	/**Test set state is correctly done. 
	 */
	public void testProducerSet(){

		assertEquals(p.getLocation(), 2);
		assertEquals(p.getConnection_limit(),6);
		assertEquals(p.getIp_addr(),"123.11.11");
		assertEquals(p.getPort(), "12323");
		assertEquals(p.isAccelerometer(), false);
		assertEquals(p.isGps(), false);
		assertEquals(p.isLight(), true);
		assertEquals(p.isOrientation(), true);
		assertEquals(p.isTemperature(), false); 
		
		p.createJsonNoId();
	}
	
	/**Simply makes call to register producer,
	 * output to console describes success, ensures
	 * device id is set in return, and return value
	 * 200.
	 */
	public void testProducerRegistrationHeartBeat(){
		
		ProducerRegister pr = new ProducerRegister();
		int code = pr.registerProducer(p);
		assertNotNull(p.getDevice_id());
		assertEquals(200,code);
		pr.producerHeartBeat(p);
	}
	
	
}
