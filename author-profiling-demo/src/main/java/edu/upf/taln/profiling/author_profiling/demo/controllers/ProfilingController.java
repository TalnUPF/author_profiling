package edu.upf.taln.profiling.author_profiling.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.upf.taln.demo.base.pojos.views.input.FormData;
import edu.upf.taln.demo.base.pojos.views.output.OutputViewerData;
import edu.upf.taln.profiling.author_profiling.clients.ExampleClient;
import edu.upf.taln.profiling.author_profiling.commons.pojos.input.ProfilingInput;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingOutput;
import edu.upf.taln.profiling.author_profiling.demo.utils.ProfilingOutputViewerFactory;
import edu.upf.taln.profiling.author_profiling.demo.validators.ProfilingValidator;
import edu.upf.taln.demo.base.pojos.views.input.TextAreaData;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Component
@Controller
@RequestMapping("/profiling")
public class ProfilingController {
	
	@Value("${profilingUrl}")
	private String serviceUrl;
	
    @Autowired
	ProfilingValidator profilingValidator;

	//Set a form validator
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(profilingValidator);
	}
    
	//===============================================================================
	//                             VIEWS GENERATION
	//===============================================================================
	
	private void generateInputView(ModelMap model, HttpServletRequest req){
		//String path = req.getContextPath();
		
		model.addAttribute("title", "Author Profiling Demo");
		model.addAttribute("formAction", "parse");
		model.addAttribute("formMethod", "post");
		
		List<String> jsHeaders = new ArrayList<>();
		jsHeaders.add("https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js");
		jsHeaders.add("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js");
		model.addAttribute("jsHeaders", jsHeaders);
		
		List<String> cssHeaders = new ArrayList<>();
		cssHeaders.add("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css");
		cssHeaders.add("../resources/core/css/base.css");
		cssHeaders.add("../resources/core/css/input.css");
		model.addAttribute("cssHeaders", cssHeaders);
		
		List<String> jsEmbeddeds = new ArrayList<>();
		jsEmbeddeds.add("input.js.jsp");
		model.addAttribute("jsEmbeddeds", jsEmbeddeds);
		
		FormData formData = new FormData();
		
		TextAreaData text = new TextAreaData("text");
		text.setLabel("Please insert text");
		text.setPlaceholder("Enter text here");
		text.setRows("10");
		text.setMandatory(true);
		formData.addComponent(text);
		
		model.addAttribute("form", formData);
	}
	
	private void generateOutputView(ModelMap model, HttpServletRequest req){
		//String path = req.getContextPath();
		
		model.addAttribute("title", "Result Visualizer");
		
		List<String> jsHeaders = new ArrayList<>();
		jsHeaders.add("http://weaver.nlplab.org/~brat/demo/v1.3/client/lib/head.load.min.js");
		model.addAttribute("jsHeaders", jsHeaders);
		
		List<String> cssHeaders = new ArrayList<>();
		cssHeaders.add("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css");
		cssHeaders.add("../resources/core/css/style-vis.css");
		cssHeaders.add("../resources/core/css/base.css");
		cssHeaders.add("../resources/core/css/output.css");
		model.addAttribute("cssHeaders", cssHeaders);
		
		List<String> jsEmbeddeds = new ArrayList<>();
		jsEmbeddeds.add("output.js.jsp");
		model.addAttribute("jsEmbeddeds", jsEmbeddeds);
	}
	
	//===============================================================================
	//                             DATA GENERATION
	//===============================================================================
	
	private List<OutputViewerData> getOutputViewerData(ProfilingOutput result){
		// Initialization of the view empty list
		List<OutputViewerData> listOutputViewerData = new ArrayList<>(); 
		
		//We fill the view with the response data
		if(result.getData() != null){
            OutputViewerData viewData = ProfilingOutputViewerFactory.generateProfilingView(result.getData().getOriginal(), result.getData().getTranslation(), result.getData());
			listOutputViewerData.add(viewData);
		}
		return listOutputViewerData;
	}
	
	
	
	//===============================================================================
	//                             REQUESTS HANDLERS
	//===============================================================================

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap model, HttpServletRequest req) {
		
		generateInputView(model, req);
		model.addAttribute("formModel", new ProfilingInput());
		return "baseInput";
	}
	
	@RequestMapping(value = "/*", method = RequestMethod.GET)
	public View redirect2index() {
        RedirectView redirect = new RedirectView("/profiling/", true);
		return redirect;
	}
	
	@RequestMapping(value = "/parse", method = RequestMethod.POST)

	public String parse(@ModelAttribute("formModel") @Validated ProfilingInput data, BindingResult result, ModelMap model, HttpServletRequest req) {

		if (result.hasErrors()) {
			generateInputView(model, req);
			return "baseInput";
		}else{
			try {
			
				ExampleClient client = new ExampleClient(serviceUrl);
					
				ProfilingOutput mtResult = client.analyze(data.getText());
				
	            List<OutputViewerData> layers = getOutputViewerData(mtResult);
	            			
	            generateOutputView(model, req);
	            model.addAttribute("layers", layers);
	            return "baseOutput";
	
			} catch(Exception e) {
				generateInputView(model, req);
				model.addAttribute("error", e.getMessage());
				return "baseInput";
			}
		}

	}
	
}
