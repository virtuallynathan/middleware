package Producer;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ProducerRegister {
	
	/**Initial Registration of a Producer with the database.
	 * Using API method POST device/add
	 */
	public void registerProducer(Producer p){
			
		HttpClient hc = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://middleware.nathan.io:8080/device/add");
		
		try{		
			
			//set parameters as JSON
			String jsonstring = p.createJsonNoId();
			System.out.println(jsonstring);
			StringEntity params = new StringEntity(jsonstring);
	        httppost.setEntity(params);	
			//Execute and get the response.
			HttpResponse response = hc.execute(httppost);
			//HttpEntity entity = response.getEntity();
			System.out.println(response.toString());
			String r = EntityUtils.toString(response.getEntity());
			System.out.println(r);
			
//			if (entity != null) {
//			    InputStream instream = entity.getContent();
//			    try {
//			    	//print response
//			        System.out.println(response.toString());
//			        // get json from response string
//			        System.out.println(instream.read());
//			        //get the device id field from the json
//			        //set the device id.
//			    } finally {
//			        instream.close();
//			    }
//			}
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
