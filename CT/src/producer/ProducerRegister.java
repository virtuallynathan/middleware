package producer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import api.APIConnection;

public class ProducerRegister {


	/**Initial Registration of a Producer with the database.
	 * Using API method POST device/add
	 * returns status of request 0 if failed
	 */
	public int registerProducer(Producer p){
		int result = 0;
		APIConnection api = new APIConnection();
		HttpResponse response = api.post(api.getRegisterDevice(), p.createJsonNoId());
		if (response!=null){
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
		} return result;
	}


	/**Method to send heart beat to the register
	 * returns status of request 0 if failed
	 * @param p
	 */
	public int producerHeartBeat(Producer p){
		int code = 0;
		if(p.testRegistered()){
			try{
				//execute and get response
				APIConnection api = new APIConnection();
				HttpResponse response = api.get(api.getHeartbeat(), p.getDevice_id());
				code = api.getResponse(response);
				return code;
			}catch(Exception e){
				return code;				
			}
		}else{
			System.out.println("Device not yet registered");
			return code;
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
		p.setDevice_id(id);		
	}

}
