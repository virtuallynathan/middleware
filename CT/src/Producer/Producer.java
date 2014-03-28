package Producer;
/**Class providing a Producer object and required state
 *
 */
public class Producer {
		
	//attributes
	//location representative
	int location;
	//connections
	int connection_limit;
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
	public Producer(int location, int c_l, boolean accel, boolean gps,
	boolean gravity, boolean orien, boolean temp ){
		this.location = location;
		this.connection_limit = c_l;
		this.accelerometer = accel;
		this.gps = gps;
		this.temperature = temp;
		this.gravity = gravity;
		this.orientation = orien;
	}
	
}
