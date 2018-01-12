package edu.upf.taln.profiling.author_profiling.commons.pojos.output;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfilingOutput {
    private String text;
    private String brat;
    private List<Prediction> predictions;
    private List<Feature> features;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBrat() {
        return brat;
    }

    public void setBrat(String brat) {
        this.brat = brat;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
    
    
}
