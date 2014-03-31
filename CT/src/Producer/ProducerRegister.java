package Producer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import Consumer.APIConnection;

public class ProducerRegister {


	/**Initial Registration of a Producer with the database.
	 * Using API method POST device/add
	 */
	public void registerProducer(Producer p){
		
		APIConnection api = new APIConnection();
		HttpResponse response = api.post(api.register(), p.createJsonNoId());
		try{
			System.out.println(response.toString()); /////////////////remove just to show response for now
			if (api.testResponseOK(response)){	
				HttpEntity entity = response.getEntity();
				if(entity!=null){					
					setDeviceID(entity, p);					
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

		if(p.testRegistered()){

			HttpClient hc = HttpClients.createDefault();
			HttpGet httpget = new HttpGet("http://middleware.nathan.io:8080/device/heartbeat/" + p.device_id);
			try{
				//execute and get response
				HttpResponse response = hc.execute(httpget);
				HttpEntity entity = response.getEntity();
				//tests response ok
				if (entity!=null){
					System.out.println(response.toString());
					int code = response.getStatusLine().getStatusCode();
					if(code==200){
						System.out.println("Heartbeat Registered");
					}else{
						System.out.println("Heartbeat Failed Status code"+ code);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("Device not yet registered");
		}
	}
	
	
	/**Helper method to set id of a device
	 * @param entity
	 * @param p
	 * @throws Exception
	 */
	private void setDeviceID(HttpEntity entity, Producer p) throws Exception {
		
		String r = EntityUtils.toString(entity);
		JSONObject json = new JSONObject(r);
		String id = (String) json.get("DeviceID");
		p.setDeviceId(id);		
	}

}
