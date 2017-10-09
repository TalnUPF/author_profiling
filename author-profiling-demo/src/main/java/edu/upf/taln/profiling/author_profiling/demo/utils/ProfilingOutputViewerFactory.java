package edu.upf.taln.profiling.author_profiling.demo.utils;

import java.util.ArrayList;
import java.util.List;

import edu.upf.taln.demo.base.pojos.views.output.OutputViewerData;
import edu.upf.taln.demo.base.utils.OutputViewerFactory;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingOutput.Data;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingOutput.Data.Dbpedia.All;

public class ProfilingOutputViewerFactory extends OutputViewerFactory{
	
	public static OutputViewerData generateProfilingView(String originalText, String finalText, Data result) {
		OutputViewerData view = new OutputViewerData();
		view.setLevelName("Output");
		view.setCollapsed(false);
	
		String error = null;
		try{
			view.addComponent(getPlainTextComponent(originalText));
			List<BratInput> listAnnotations = new ArrayList<BratInput>();
			for(All ann : result.getDbpedia().getAll()){
				BratInput bi = new BratInput();
				bi.start = ann.getBegin();
				bi.end = ann.getEnd();
				bi.annotation = ann.getUri();
				bi.extra = ann.getUri();
				listAnnotations.add(bi);
			}
			view.addComponent(getBratComponentBratInput(finalText, listAnnotations));
		}catch(Exception e){
			error = e.getMessage();
		}
		
		
		if(error != null){
			view.setError(error);
		}
		
		return view;
	}
}
