package Consumer;

import org.json.JSONObject;

import api.APIConnection;

/**Class to hold state for consumers, allows construction with
 * default values, or with given parameters.
 */
public class Consumer {
	
	//state for consumer
	//location
	int location;
	//sensors requested
	boolean accelerometer;
	boolean gps;
	boolean light;
	boolean orientation;
	boolean temperature;
	
	String connection_ip;
	String connection_port;
	
	
	/**Sets up a consumer with default values.
	 */
	public Consumer(){		
		this.location = -1;
		this.setAllSensors(false);
	}
	
	/**Sets up a consumer with parameters as state.
	 * @param location
	 * @param accel
	 * @param gps
	 * @param light
	 * @param orien
	 * @param temp
	 */
	public Consumer(int location, boolean accel, boolean gps, boolean light,
	boolean orien, boolean temp){
		this.location = location;
		this.accelerometer = accel;
		this.gps = gps;
		this.light = light;
		this.orientation = orien;
		this.temperature = temp;
	}

	public void setConnectionPort(String cp){
		connection_port = cp;
	}
	
	public void setConnectionIP(String cip){
		connection_ip = cip;		
	}
	
	/**Sets all sensor values to the same boolean parameter
	 */
	public void setAllSensors(boolean b){
		this.accelerometer = b;
		this.gps = b;
		this.light = b;
		this.orientation = b;
		this.temperature = b;		
	}
	
	/**Convert to the JSON 
	 * @return
	 */
	public String consumerToJSON(){		
		APIConnection api = new APIConnection();
		JSONObject json = new JSONObject();
		try {
			
			json.put(api.getLoc_key(), String.valueOf(location));
			json.put(api.getAcc_key(), String.valueOf(accelerometer));
			json.put(api.getGps_key(), String.valueOf(gps));
			json.put(api.getLight_key(), String.valueOf(light));
			json.put(api.getOrien_key(), String.valueOf(orientation));
			json.put(api.getTemp_key(), String.valueOf(temperature));
		} catch (Exception e) {
			
			System.out.println("Json Creation Failed");
			e.printStackTrace();
		}
		return json.toString();
	}
	
}
