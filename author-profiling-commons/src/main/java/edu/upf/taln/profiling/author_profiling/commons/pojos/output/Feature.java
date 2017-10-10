/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upf.taln.profiling.author_profiling.commons.pojos.output;

/**
 *
 * @author joan
 */
public class Feature 
{
    protected String featureType;
    protected String featureName;
    protected Double value;

    public String getFeatureType() {
        return featureType;
    }

    public String getFeatureName() {
        return featureName;
    }

    public Double getValue() {
        return value;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Feature{" + "featureType=" + featureType + ", featureName=" + featureName + ", value=" + value + '}';
    }
    
    
    
}
