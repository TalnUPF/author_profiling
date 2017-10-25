package edu.upf.taln.profiling.author_profiling.demo.utils;

import java.util.ArrayList;
import java.util.List;

import edu.upf.taln.demo.base.pojos.views.output.OutputViewerData;
import edu.upf.taln.demo.base.utils.LevelsMapping;
import edu.upf.taln.demo.base.utils.OutputViewerFactory;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.Prediction;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingOutput;

public class ProfilingOutputViewerFactory extends OutputViewerFactory{
	
	public static OutputViewerData generateProfilingView(ProfilingOutput result) {
		OutputViewerData view = new OutputViewerData();
		view.setLevelName("Output");
		view.setCollapsed(false);
	
		String error = null;
		try{
			//view.addComponent(getPlainTextComponent(result.getText()));
			view.addComponent(getBratComponent(result.getText(), result.getConll(), LevelsMapping.PARSED_TEXT));
			for(Prediction prediction: result.getPredictions()){
				List<String> checkboxesChecked = new ArrayList<String>();
				checkboxesChecked.add(prediction.getPrediction());
				view.addComponent(getCheckboxesComponent(prediction.getModelName(), prediction.getLabels(), checkboxesChecked, "checkboxesPadding"));
			}
		}catch(Exception e){
			error = e.getMessage();
		}
		
		
		if(error != null){
			view.setError(error);
		}
		
		return view;
	}
}
