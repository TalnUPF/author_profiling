package edu.upf.taln.profiling.author_profiling.commons.pojos.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfilingInput {
    
	private String text = "";
	
    public String getText() {
        return text.trim();
    }

    public void setText(String text) {
        this.text = text;
    }

}
