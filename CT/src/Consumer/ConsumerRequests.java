package Consumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import api.APIConnection;



public class ConsumerRequests {
	
	public void requestProducer(Consumer c){
	
		APIConnection api = new APIConnection();
		String param = c.consumerToJSON();
		System.out.println(param);
		HttpResponse response = api.post(api.getConsumerRequest(), param);
		//HttpResponse response = api.post(api.getConsumerRequest(), c.consumerToJSON());
		try{
			System.out.println(response.toString()); /////////////////remove just to show response for now
			if (api.getResponse(response)==200){	
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
				    builder.append(line).append("\n");
				}
				JSONTokener tokener = new JSONTokener(builder.toString());
				JSONArray finalResult = new JSONArray(tokener);
				
				//prints first JSON String 
				System.out.println(finalResult.get(0).toString());
				//currently takes first response and uses that port/ip
				JSONObject json1 = finalResult.getJSONObject(0);
				System.out.println(json1.toString());
				String port = (String) json1.get(api.getPort_key());
				String ip = (String) json1.get(api.getIp_key());
				c.setConnection_ip(ip);
				c.setConnection_ip(port);
				
				System.out.println(c.getConnection_port() + " and " + c.getConnection_ip());
				
				
			}
		}catch(Exception e){
			System.out.println("Failed to request prodcuer");
			e.printStackTrace();
		}
	}

}
