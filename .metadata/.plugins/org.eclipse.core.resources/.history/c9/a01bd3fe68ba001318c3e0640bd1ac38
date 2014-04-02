package api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

/**Class to describe and store the API variables
 *
 */
public class APIConnection {
	
	//api json parameter keys
	private String device_id_key = "DeviceID";
	private String ip_key = "IPAddr";
	private String port_key = "ListenPort";
	private String loc_key = "Location";
	private String connect_key = "ConnectionLimit";
	private String acc_key = "Accelerometer";
	private String gps_key = "GPS";
	private String light_key = "Light";
	private String orien_key = "Orientation";
	private String temp_key = "Temperature";	
	//api connection configuration
	private String server = "http://middleware.nathan.io:8080";	
	private String registerDevice = "/device/add";
	private String heartbeat = "/device/heartbeat/";
	private String consumerRequest = "/device/sensor_location";
	private String consumerConnect = "/device/connect/:";
	private String consumerDisconnect = "/device/disconnect/:";
	private String connection_key = "Message";
	
	public String getConnection_key() {
		return connection_key;
	}

	public String getConsumerConnect() {
		return consumerConnect;
	}

	public String getConsumerDisconnect() {
		return consumerDisconnect;
	}

	public String getDevice_id_key() {
		return device_id_key;
	}

	public String getIp_key() {
		return ip_key;
	}

	public String getPort_key() {
		return port_key;
	}

	public String getLoc_key() {
		return loc_key;
	}

	public String getConnect_key() {
		return connect_key;
	}

	public String getAcc_key() {
		return acc_key;
	}

	public String getGps_key() {
		return gps_key;
	}

	public String getLight_key() {
		return light_key;
	}

	public String getOrien_key() {
		return orien_key;
	}

	public String getTemp_key() {
		return temp_key;
	}

	public String getServer() {
		return server;
	}

	public String getRegisterDevice() {
		return registerDevice;
	}

	public String getHeartbeat() {
		return heartbeat;
	}

	public String getConsumerRequest() {
		return consumerRequest;
	}	
	
	/**Method to send Post to the API
	 * @param method
	 * @param params
	 * @return
	 */
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
	
	/**Confirm Status line of a response is ok
	 * @param r
	 * @return
	 */
	public int getResponse(HttpResponse r){
		
		int code = r.getStatusLine().getStatusCode();		
		return code;		
	}
	
	
	/**Method to send GET to API
	 * @param method
	 * @param params
	 * @return
	 */
	public HttpResponse get(String method, String params){
		
		HttpResponse response = null;
		HttpClient hc = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(server + method + params);
		try{
			//execute and get response
			response = hc.execute(httpget);
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		return response;
		
	}

}
