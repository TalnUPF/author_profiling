/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upf.taln.profiling.author_profiling.commons.pojos.output;

import java.util.List;

/**
 *
 * @author joan
 */
public class Prediction 
{
    protected String modelName;
    protected String shortModelName;
    protected List<String> labels;
    protected String prediction;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getShortModelName() {
        return shortModelName;
    }

    public void setShortModelName(String shortModelName) {
        this.shortModelName = shortModelName;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }
    
    @Override
    public String toString() {
        return "Prediction{" + "modelName=" + modelName + ", shortModelName=" + shortModelName + ", labels=" + labels + ", prediction=" + prediction + '}';
    }
    
    
}
