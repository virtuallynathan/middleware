package Producer;

import java.io.DataOutputStream;
import java.net.*;

public class ProducerRegister {
	
	/**Initial Registration of a Producer with the database.
	 * Using API method POST device/add
	 */
	public void registerProducer(Producer p){
		
		try{
		//set up the HTTP connection
		String url = "server.nathan.io:8080/device/add";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		//add request header
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		//set parameters
		String connection_ip = "192.168.1.100";
		String connection_port = "63532";
		String parameters = "\"IpAddr\":\"" + connection_ip + "\",\"ListenPort\":\"" + connection_port + "\","
							+ "\"Location\":\"St Andrews\", \"ConnectionLimit\":\"10\",\"Sensor\":\"GPS\"";		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(parameters);
		wr.flush();
		wr.close();	
		
		}catch(Exception e){
			System.out.println("Failed to add reciver at post");
		}		
	}
	
	
	
	/**Method to send heartbeat to the register
	 * @param p
	 */
	public void producerHeartBeat(Producer p){
		
	}

}
