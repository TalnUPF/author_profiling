package edu.upf.taln.profiling.author_profiling.demo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upf.taln.demo.base.pojos.views.output.OutputViewerData;
import edu.upf.taln.demo.base.pojos.views.output.TableCell;
import edu.upf.taln.demo.base.utils.LevelsMapping;
import edu.upf.taln.demo.base.utils.OutputViewerFactory;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.Feature;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.Prediction;

public class ProfilingOutputViewerFactory extends OutputViewerFactory{

    private static String splitCamelCaseString(String s){
        String result = "";   
        for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            result+=w+" ";
        }    
        return result;
    }

    public static OutputViewerData generateProfilingViewPredictions(List<Prediction> predictions) {
		OutputViewerData view = new OutputViewerData();
		view.setLevelName("Output");
		view.setCollapsed(false);
	
		String error = null;
		try{
			List<String> titles = new ArrayList<String>();
			List<String> results = new ArrayList<String>();
			for(Prediction prediction: predictions){
				String title = "";
				if(prediction.getModelName().equals("LiteraryMerged_book_etreeCLF.pkl")){
					title = "Book Resemblance Prediction";// (eTree)
/*				} else if(prediction.getModelName().equals("LiteraryMerged_author_svmCLF.pkl")){
					title = "Author Resemblance Prediction (SVM)";
				}else if(prediction.getModelName().equals("LiteraryMerged_book_svmCLF.pkl")){
					title = "Book Resemblance Prediction (SVM)"; 
*/				}else if(prediction.getModelName().equals("LiteraryMerged_author_etreeCLF.pkl")){
					title = "Author Resemblance Prediction";// (eTree)
				}else{
				    //title = prediction.getModelName(); 
				    continue;
				}
				titles.add(title);
				results.add(splitCamelCaseString(prediction.getPrediction()));
			}
			view.addComponent(getMultiTitleResultComponent(titles, results, "checkboxesPadding"));
		}catch(Exception e){
			error = e.getMessage();
		}
		
		
		if(error != null){
			view.setError(error);
		}
		
		return view;
	}
	
	public static OutputViewerData generateProfilingViewFeatures(List<Feature> features) {
		OutputViewerData view = new OutputViewerData();
		view.setLevelName("Features");
		view.setCollapsed(true);
	
		String error = null;
		try{
			
			Map<String, Integer> typeNumber = new HashMap<String, Integer>();
			for(Feature feature: features){
				String type = feature.getFeatureType();
				if(typeNumber.containsKey(type)){
					typeNumber.put(type, typeNumber.get(type) + 1);
				}else{
					typeNumber.put(type, 1);
				}
			}
			
			List<List<TableCell>> tableData = new ArrayList<List<TableCell>>();
			
			List<TableCell> headerCols = new ArrayList<TableCell>();
			headerCols.add(new TableCell("Type"));
// Joan Codina remove feature name
//			headerCols.add(new TableCell("Name"));
			headerCols.add(new TableCell("Value"));
			tableData.add(headerCols);
			
			String prev = null;
			for(Feature feature: features){
				List<TableCell> cols = new ArrayList<TableCell>();
				
				TableCell type = new TableCell(splitCamelCaseString(feature.getFeatureType()));
				Integer val = typeNumber.get(feature.getFeatureType());
				type.setRowspan(val);
				
				if(prev == null || !prev.equals(feature.getFeatureType())){
				    cols.add(type);
					
				}
// Joan Codina, remove feature name				
//				cols.add(new TableCell(feature.getFeatureName()));
				cols.add(new TableCell(String.valueOf(feature.getValue())));
				tableData.add(cols);
				
				prev = feature.getFeatureType();
			}
			view.addComponent(getTableComponent(tableData));
		}catch(Exception e){
			error = e.getMessage();
		}
		
		
		if(error != null){
			view.setError(error);
		}
		
		return view;
	}
	
	public static OutputViewerData generateProfilingView(String json) {
		OutputViewerData view = new OutputViewerData();
		view.setLevelName("Syntactic Trees");
		view.setCollapsed(true);
	
		String error = null;
		try{
			view.addComponent(getBratComponent(json));
		}catch(Exception e){
			error = e.getMessage();
		}
		
		if(error != null){
			view.setError(error);
		}
		
		return view;
	}
}
