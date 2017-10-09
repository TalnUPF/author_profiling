package edu.upf.taln.profiling.author_profiling.clients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingOutput;

public class ExampleClient {

	private final WebTarget target;

	public ExampleClient(String serviceUrl) {
		Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
		target = client.target(serviceUrl).path("MTdemo");
	}
	
	public ProfilingOutput analyze(String text) throws Exception {
		WebTarget synthTarget = target.path("arabic").path("analyze");
		
        Response response = synthTarget
        						.queryParam("text", text)
				                .request(MediaType.APPLICATION_JSON_TYPE)
				                .post(Entity.entity(null, MediaType.APPLICATION_FORM_URLENCODED));
        
        if (response.getStatus() == 200) {
            ProfilingOutput result = response.readEntity(new GenericType<ProfilingOutput>() {});
    		return result;
            
        } else {
            int status = response.getStatus();
            String content = response.readEntity(String.class);
            System.out.println("Status: " + status);
            System.out.println(content);
            throw new Exception("Server error " + status + "!\n" + content);
        }
	}

}
