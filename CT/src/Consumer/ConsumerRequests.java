package Consumer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ConsumerRequests {
	
	public void requestProducer(){
		
//		//choose params required for now hardwired
//
//		HttpClient hc = HttpClients.createDefault();
//		HttpPost httppost = new HttpPost("http://middleware.nathan.io:8080/device/add");		
//		try{			
//			//set parameters as JSON
//			String jsonstring = p.createJsonNoId();
//			StringEntity params = new StringEntity(jsonstring);
//			httppost.setEntity(params);	
//			//Execute and get the response.
//			HttpResponse response = hc.execute(httppost);
//			HttpEntity entity = response.getEntity();
//
//			//tests if response is null, if not sets device id
//			if(entity!=null){
//				System.out.println(response.toString());
//				int code = response.getStatusLine().getStatusCode();
//				//tests ok status
//				if(code == 200){
//
//					String r = EntityUtils.toString(entity);
//					JSONObject json = new JSONObject(r);
//					String id = (String) json.get("DeviceID");
//					p.setDeviceId(id);
//				}
//			}	
//		}catch(Exception e){
//			System.out.println("Failed to post using /device/add");
//			e.printStackTrace();
//		}
		
	}

}
