package Producer;

import org.json.JSONObject;

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
	boolean gravity;
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
		this.gravity = false;
		this.orientation = false;
		this.temperature = false;
	}

	/**Sets Producer state to given parameters
	 * @param location
	 * @param c_l
	 * @param accel
	 * @param gps
	 * @param temp
	 * @param gravity
	 * @param orien
	 */
	public Producer(int location, int c_l, String ip, String port, boolean accel, boolean gps,
	boolean gravity, boolean orien, boolean temp ){
		this.location = location;
		this.connection_limit = c_l;
		this.ip_addr = ip;
		this.port = port;
		this.accelerometer = accel;
		this.gps = gps;
		this.temperature = temp;
		this.gravity = gravity;
		this.orientation = orien;
	}
	
	
	public String createJsonNoId(){
		
		//define key string names
		String ip_key = "IPAddr";
		String port_key = "ListenPort";
		String loc_key = "Location";
		String connect_key = "ConnectionLimit";
		String sen_key = "Sensor";
		String acc_key = "Accelerometer ";
		String gps_key = "GPS";
		String gravity_key = "Gravity";
		String orien_key = "Orientation";
		String temp_key = "Temparture";
		
		
		JSONObject json = new JSONObject();
		try{
			
			json.put(ip_key,ip_addr);
			json.put(port_key, port);
			json.put(loc_key, String.valueOf(location));
			json.put(connect_key, String.valueOf(connection_limit));
			json.put(sen_key, "GPS");
			
			//when using the real method
//			json.put(acc_key, accelerometer);
//			json.put(gps_key, gps);
//			json.put(gravity_key, gravity);
//			json.put(orien_key, orientation);
//			json.put(temp_key, temperature);
//			
			
		}catch(Exception e){
			System.out.println("Json Creation Failed");
			e.printStackTrace();
		}		
		return json.toString();
	}
	
}
