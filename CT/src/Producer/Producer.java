package Producer;

import org.json.JSONObject;

import api.APIConnection;

/**Class providing a Producer object and required state
 *
 */
public class Producer {
		
	//attributes
	int location;
	int connection_limit;
	String ip_addr;
	String port;
	String device_id;
	//sensors supported
	boolean accelerometer;
	boolean gps;
	boolean light;
	boolean orientation;
	boolean temperature;
	
	/**Sets Values to defaults
	 * location -1
	 * connections 0
	 * sensors all false
	 */
	public Producer(){
		this.location = -1;
		this.connection_limit = 0;
		this.ip_addr = null;
		this.port = null;
		this.device_id = null;
		this.accelerometer = false;
		this.gps = false;
		this.light = false;
		this.orientation = false;
		this.temperature = false;
	}

	/**Sets Producer state to given parameters
	 * @param location
	 * @param c_l
	 * @param accel
	 * @param gps
	 * @param temp
	 * @param light
	 * @param orien
	 */
	public Producer(int location, int c_l, String ip, String port, boolean accel, boolean gps,
	boolean light, boolean orien, boolean temp ){
		this.location = location;
		this.connection_limit = c_l;
		this.ip_addr = ip;
		this.port = port;
		this.accelerometer = accel;
		this.gps = gps;
		this.temperature = temp;
		this.light = light;
		this.orientation = orien;
	}
	
	/**Setter for device id
	 * @param id
	 */
	public void setDeviceId(String id){
		this.device_id=id;
	}
	
	/**Check if device is registered by testing device id field
	 * @return
	 */
	public boolean testRegistered(){
		return (device_id!=null);
	}
	
	/**Constructs JSON representation for the given producer.
	 * @return
	 */
	public String createJsonNoId() {
		APIConnection api = new APIConnection();
		JSONObject json = new JSONObject();
		try {
			json.put(api.getIp_key(), ip_addr);
			json.put(api.getPort_key(), port);
			json.put(api.getLoc_key(), String.valueOf(location));
			json.put(api.getConnect_key(), String.valueOf(connection_limit));
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
