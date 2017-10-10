package edu.upf.taln.profiling.author_profiling.commons.pojos.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfilingInput {
    
    private String text;
    private String conll;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getConll() {
        return conll;
    }

    public void setConll(String conll) {
        this.conll = conll;
    }

}
