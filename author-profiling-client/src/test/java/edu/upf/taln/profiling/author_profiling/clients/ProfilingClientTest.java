package edu.upf.taln.profiling.author_profiling.clients;

import edu.upf.taln.profiling.author_profiling.commons.pojos.input.ProfilingInput;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingException;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingOutput;
import org.junit.Test;

/**
 *
 * @author rcarlini
 */
public class ProfilingClientTest {
    
    String SERVICE_URL = "http://10.80.28.153:5000/predict";
    
    public ProfilingClientTest() {
    }

    /**
     * Test of predict method of class ProfilingClient.
     * 
     */
    @Test
    public void testPredict() throws ProfilingException {
        ProfilingClient client = new ProfilingClient(SERVICE_URL);
        
        ProfilingInput data = new ProfilingInput();
        data.setText("Este es el test de español.");
        data.setConll("plain");
        data.setTokens("es");
        
        ProfilingOutput result = client.analyze(data);
        System.out.println(result);
    }
}
