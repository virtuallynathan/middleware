package Producer;

import java.util.List;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


public class ProducerRegister {
	
	/**Initial Registration of a Producer with the database.
	 * Using API method POST device/add
	 */
	public void registerProducer(Producer p){
			
		HttpClient hc = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://middleware.nathan.io/device/add");
		
		try{		
			// Request parameters and other properties.
			List <NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("IPAddr", "192.168.1.123"));
			params.add(new BasicNameValuePair("ListenPort", "3132"));
			params.add(new BasicNameValuePair("Location", "2"));
			params.add(new BasicNameValuePair("ConnectionLimit", "10"));
			params.add(new BasicNameValuePair("Sensor", "gps"));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
	
			//Execute and get the response.
			HttpResponse response = hc.execute(httppost);
			HttpEntity entity = response.getEntity();
	
			if (entity != null) {
			    InputStream instream = entity.getContent();
			    try {
			        System.out.println("here");
			    } finally {
			        instream.close();
			    }
			}
		}catch(Exception e){
			System.out.println("failed to post");
			e.printStackTrace();
		}
	}
	
	/**Method to send heart beat to the register
	 * @param p
	 */
	public void producerHeartBeat(Producer p){
		
	}

}
