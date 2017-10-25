package edu.upf.taln.profiling.author_profiling.clients;

import edu.upf.taln.profiling.author_profiling.commons.pojos.input.ProfilingInput;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingOutput;

public class ProfilingClient {

    private static final Logger logger = LoggerFactory.getLogger(ProfilingClient.class);

	private final WebTarget target;

	public ProfilingClient(String serviceUrl) {
        Client client = ClientBuilder.newClient();
        target = client.target(serviceUrl);
	}
	
	public ProfilingOutput predict(ProfilingInput input) throws ProfilingException {
    
        WebTarget methodTarget = target.path("predict");
        Response response = methodTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(input, MediaType.APPLICATION_JSON_TYPE));
    
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            ProfilingOutput result = response.readEntity(new GenericType<ProfilingOutput>() {});
            return result;

        } else if (response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            logger.error("Status: " + response.getStatus());
            ProfilingException lse = response.readEntity(ProfilingException.class);
            logger.error(lse.getMessage());

            throw lse;

        } else {
            logger.error("Status: " + response.getStatus());
            logger.error(response.readEntity(String.class));

            throw new ProfilingException("Server error!\nStatus: " + response.getStatus());
        }
	}
}
