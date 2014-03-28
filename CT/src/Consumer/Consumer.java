package Consumer;

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
	boolean gravity;
	boolean orientation;
	boolean temperature;
	
	
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
	 * @param gravity
	 * @param orien
	 * @param temp
	 */
	public Consumer(int location, boolean accel, boolean gps, boolean gravity,
	boolean orien, boolean temp){
		this.location = location;
		this.accelerometer = accel;
		this.gps = gps;
		this.gravity = gravity;
		this.orientation = orien;
		this.temperature = temp;
	}

	
	/**Sets all sensor values to the same boolean parameter
	 */
	public void setAllSensors(boolean b){
		this.accelerometer = b;
		this.gps = b;
		this.gravity = b;
		this.orientation = b;
		this.temperature = b;		
	}
}
