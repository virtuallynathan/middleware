package Producer;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;


public class ProducerRegister {
	
	/**Initial Registration of a Producer with the database.
	 * Using API method POST device/add
	 */
	public void registerProducer(Producer p){
			
		HttpClient hc = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://middleware.nathan.io:8080/device/add");
		
		try{
			//set parameters as JSON
			StringEntity params = new StringEntity("{\"DeviceId\":\"myname\",\"IPAddr\":\"192.168.1.123\",\"ListenPort\""
				+ ":\"3123\",\"Location\":\"ere\", \"ConnectionLimit\":\"12\",\"Sensor\":\"GPS\"}");
	        httppost.setEntity(params);	
			//Execute and get the response.
			HttpResponse response = hc.execute(httppost);
			HttpEntity entity = response.getEntity();
			///////////////////////////////////////////////
			//todo check if 200 otherwise fail - possibly get id?
			///////////////////////////////////////////////
			if (entity != null) {
			    InputStream instream = entity.getContent();
			    try {
			    	//print response
			        System.out.println(response.toString());
			    } finally {
			        instream.close();
			    }
			}
		}catch(Exception e){
			System.out.println("Failed to post using /device/add");
			e.printStackTrace();
		}
	}
	
	/**Method to send heart beat to the register
	 * @param p
	 */
	public void producerHeartBeat(Producer p){
		
	}

}
