package Producer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import api.APIConnection;

public class ProducerRegister {


	/**Initial Registration of a Producer with the database.
	 * Using API method POST device/add
	 * returns status of reply.
	 */
	public int registerProducer(Producer p){
		
		int result = 200;
		APIConnection api = new APIConnection();
		HttpResponse response = api.post(api.getRegisterDevice(), p.createJsonNoId());
		try{
			System.out.println(response.toString()); /////////////////remove just to show response for now
			result = api.getResponse(response);
			if (result==200){	
				HttpEntity entity = response.getEntity();
				if(entity!=null){					
					setDeviceID(entity, p);
					return result;
				}	
			}
			return result;
		}catch(Exception e){
			System.out.println("Failed to post using /device/add");
			return result;
		}
	}


	/**Method to send heart beat to the register
	 * returns true if successful, false if not.
	 * @param p
	 */
	public boolean producerHeartBeat(Producer p){

		if(p.testRegistered()){
			try{
				//execute and get response
				APIConnection api = new APIConnection();
				HttpResponse response = api.get(api.getHeartbeat(), p.device_id);
				System.out.println(response.toString()); /////////////////remove just to show response for now
				if(api.getResponse(response)==200){
					return true;
				}else{
					return false;
				}
			}catch(Exception e){
				return false;				
			}
		}else{
			System.out.println("Device not yet registered");
			return false;
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
