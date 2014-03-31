package Consumer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

/**Class to describe and store the API variables
 *
 */
public class APIConnection {
	
	String device_id_key = "DeviceID";
	String ip_key = "IPAddr";
	String port_key = "ListenPort";
	String loc_key = "Location";
	String connect_key = "ConnectionLimit";
	String acc_key = "Accelerometer";
	String gps_key = "GPS";
	String light_key = "Light";
	String orien_key = "Orientation";
	String temp_key = "Temperature";
	
	String server = "http://middleware.nathan.io:8080";
	
	String registerDevice = "/device/add";
	String heartbeat = "/device/heartbeat/";
	String consumerRequest = "/device/sensor_location/";
	
	public String register(){
		return registerDevice;
	}
	
	public HttpResponse post(String method, String params){
		
		HttpResponse response = null;
		HttpClient hc = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(server+method);		
		try{			
			//set parameters as JSON
			StringEntity paramstring = new StringEntity(params);
			httppost.setEntity(paramstring);	
			//Execute and get the response.
			response = hc.execute(httppost);
		}catch(Exception e){
			//do something here?
		}			
			return response;	
	}
	
	public int testResponseOK(HttpResponse r){
		
		int code = 0;
		
		return code;		
	}
	
	
	public void get(){
		
	}

}
