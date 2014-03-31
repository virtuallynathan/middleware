package Consumer;

import org.json.JSONObject;

import api.APIConnection;

/**Class to hold state for consumers, allows construction with
 * default values, or with given parameters.
 */
public class Consumer {
	
	//state for consumer
	private int location;
	//sensors requested
	private boolean accelerometer;
	private boolean gps;
	private boolean light;
	private boolean orientation;
	private boolean temperature;
	//connection details
	private String connection_ip;
	private String connection_port;
	
	
	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public boolean isAccelerometer() {
		return accelerometer;
	}

	public void setAccelerometer(boolean accelerometer) {
		this.accelerometer = accelerometer;
	}

	public boolean isGps() {
		return gps;
	}

	public void setGps(boolean gps) {
		this.gps = gps;
	}

	public boolean isLight() {
		return light;
	}

	public void setLight(boolean light) {
		this.light = light;
	}

	public boolean isOrientation() {
		return orientation;
	}

	public void setOrientation(boolean orientation) {
		this.orientation = orientation;
	}

	public boolean isTemperature() {
		return temperature;
	}

	public void setTemperature(boolean temperature) {
		this.temperature = temperature;
	}

	public String getConnection_ip() {
		return connection_ip;
	}

	public void setConnection_ip(String connection_ip) {
		this.connection_ip = connection_ip;
	}

	public String getConnection_port() {
		return connection_port;
	}

	public void setConnection_port(String connection_port) {
		this.connection_port = connection_port;
	}

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
