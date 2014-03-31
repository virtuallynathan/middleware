package consumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import api.APIConnection;

public class ConsumerRequests {
	
	/**Requests mobile device for the producer
	 * returns the HTTP response code and 0 if
	 * failed to execute post.
	 * @param c
	 * @return
	 */
	public int requestProducer(Consumer c){		
		int result =0;
		APIConnection api = new APIConnection();
		String param = c.consumerToJSON();
		System.out.println(param);
		HttpResponse response = api.post(api.getConsumerRequest(), param);
		//HttpResponse response = api.post(api.getConsumerRequest(), c.consumerToJSON());
		try{
			System.out.println(response.toString()); /////////////////remove just to show response for now
			result = api.getResponse(response);
			if (result == 200){					
				JSONArray jsonArray = getJSONArray(response);				
				if (jsonArray!=null){				
				//currently takes first response and uses that port/ip
				JSONObject json = jsonArray.getJSONObject(0);
				String port = (String) json.get(api.getPort_key());
				String ip = (String) json.get(api.getIp_key());
				c.setConnection_ip(ip);
				c.setConnection_port(port);				
				System.out.println(c.getConnection_port() + " and " + c.getConnection_ip());	////////////////////////////////////////// remove			
				return result;				
				}				
			}
		}catch(Exception e){
			return result;
		}
		return result;
	}

	/**Read in the response content and create JSONArray
	 * returns null on failure
	 * @param response
	 * @return
	 */
	public JSONArray getJSONArray(HttpResponse response){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
			JSONTokener tokener = new JSONTokener(builder.toString());
			JSONArray jsonArray = new JSONArray(tokener);
			return jsonArray;
			
		}catch(Exception e){
			return null;
		}

	}

}
