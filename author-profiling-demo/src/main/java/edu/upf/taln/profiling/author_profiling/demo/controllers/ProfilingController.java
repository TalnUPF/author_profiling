package edu.upf.taln.profiling.author_profiling.demo.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import org.apache.catalina.Pipeline;
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
import edu.upf.taln.profiling.author_profiling.clients.ProfilingClient;
import edu.upf.taln.profiling.author_profiling.commons.pojos.input.ProfilingInput;
import edu.upf.taln.profiling.author_profiling.commons.pojos.output.ProfilingOutput;
import edu.upf.taln.profiling.author_profiling.demo.utils.ProfilingOutputViewerFactory;
import edu.upf.taln.profiling.author_profiling.demo.validators.ProfilingValidator;
import edu.upf.taln.demo.base.pojos.views.input.TextAreaData;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;


import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.io.brat.BratWriter;
import de.tudarmstadt.ukp.dkpro.core.io.conll.Conll2009Writer;
import de.tudarmstadt.ukp.dkpro.core.matetools.MateLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.matetools.MateParser;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;



@Component
@Controller
@RequestMapping("/profiling")
public class ProfilingController {
	
	@Value("${profilingUrl}")
	private String serviceUrl;
	
	@Value("${transitionUrl}")
	private String transitionUrl;
	
    @Autowired
	ProfilingValidator profilingValidator;
    AnalysisEngine pipeline;
    AnalysisEngine pipelineBratOut;
    AnalysisEngine pipelineConllOut;
    
	//Set a form validator
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(profilingValidator);
	}
	
	@PostConstruct
	public void init() throws ResourceInitializationException {
	    
	  // Bratt mapings  
	    final String[] spanTypes = { 
	            "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token:lemma|value",
	            "de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS:coarseValue",
	            "de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity" // it takes the 

	      };
	    
	      Set<String> relationTypes= new HashSet<>();
	           relationTypes.add("de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency:Governor:Dependent:DependencyType");
	      
	      Set<String> excludedTypes = new HashSet<>(); 
	           excludedTypes.add("de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS_PUNCT");
	           excludedTypes.add("de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.PUNCT");
	           
	       final String[] typeMappings= {
	                      "de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.(\\w+) -> $1",
	                      "de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS_(\\w+) -> $1",
	                      "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.(\\w+) -> $1",
	                      "de.tudarmstadt.ukp.dkpro.core.api.ner.type.(\\w+) -> $1",
	                      "de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.(\\w+) -> $1."
	              };
	   // create the uima Pipeline
	   pipeline= createEngine(createEngineDescription(
                createEngineDescription(OpenNlpSegmenter.class),
                createEngineDescription(OpenNlpPosTagger.class), 
                createEngineDescription(MateLemmatizer.class,
                        //this is the location where the docker copies it!
                        MateLemmatizer.PARAM_MODEL_LOCATION, new File("/home/", "CoNLL2009-ST-English-ALL.anna-3.3.lemmatizer.model")),
                createEngineDescription(MateParser.class,
                            MateParser.PARAM_LANGUAGE,"en"),
                createEngineDescription(StanfordNamedEntityRecognizer.class)));
 
    pipelineBratOut = createEngine(
                createEngineDescription(BratWriter.class,
                        BratWriter.PARAM_TARGET_LOCATION,null, // null for extract to string
                        BratWriter.PARAM_FILENAME_EXTENSION,".json",
                        BratWriter.PARAM_OVERWRITE,true,
                        BratWriter.PARAM_ENABLE_TYPE_MAPPINGS,true,
                        BratWriter.PARAM_SHORT_ATTRIBUTE_NAMES,false,
                        BratWriter.PARAM_EXCLUDE_TYPES,excludedTypes,
                        BratWriter.PARAM_STRIP_EXTENSION,true,
                        BratWriter.PARAM_TYPE_MAPPINGS,typeMappings,
                        BratWriter.PARAM_TEXT_ANNOTATION_TYPES,spanTypes,
                        BratWriter.PARAM_RELATION_TYPES,relationTypes,
                        BratWriter.PARAM_WRITE_RELATION_ATTRIBUTES,false
                        ) );
    
    pipelineConllOut = createEngine(
            createEngineDescription(Conll2009Writer.class,
                    Conll2009Writer.PARAM_TARGET_LOCATION,null // null for extract to string
                    ) );

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
		
		formData.setTopText("TALN Natural Language Processing Group");
		formData.setBottomText("This demo takes as input a fragment of English text and generates as ouput a guess of the author with a more similar style."
		        +  "The system manages a list of 32 authors and 106 books, analizes the text and indicates witch of the authors and books has a style more simlar to the one of the given text <br> "
				+ " To perform the gess, the text is processed using different NLP techniques (Some of the results of this processing can be observed in the otuput)."
				+ " From the NLP processing a set of numeric features are computed using different data: some features are based on words, lexical or syntanctic information. The features vector is "
				+ "introduced to claddifier trained using machine learning thechniques.<br>"
				+ " The demo shows the classification result (the author and book with a style more similar to the introduced text, the features vector and some of the linguistic processing, "
				+ "in particular: lemma of the word, part of speech, dependecies and Named Entities "
				+ " (Persons, Organizations, Places)<br>"
				+ " The system could be trained to classify texts in other cathegories like gender of the writer, age or even target public. ");
		
		model.addAttribute("form", formData);
	}
	
	private void generateOutputView(ModelMap model, HttpServletRequest req){
		//String path = req.getContextPath();
		
		model.addAttribute("title", "Result Visualizer");
		
		List<String> jsHeaders = new ArrayList<>();
		jsHeaders.add("../resources/core/js/head.load.min.js");
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
		if(result != null){
			OutputViewerData ouput = ProfilingOutputViewerFactory.generateProfilingViewPredictions(result.getPredictions());
			listOutputViewerData.add(ouput);
            OutputViewerData syntacticTree = ProfilingOutputViewerFactory.generateProfilingView( result.getBrat());
			listOutputViewerData.add(syntacticTree);
			OutputViewerData features = ProfilingOutputViewerFactory.generateProfilingViewFeatures(result.getFeatures());
			listOutputViewerData.add(features);
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
				data.getText();
			    JCas jcas = JCasFactory.createJCas();
		        jcas.setDocumentText(data.getText());
		        jcas.setDocumentLanguage("en"); 
		        DocumentMetaData docData=new DocumentMetaData(jcas);
		        docData.setDocumentTitle("prova");
		        docData.setDocumentId("prova");
		        docData.addToIndexes();
		        pipeline.process(jcas);
		        
		        // extract BRAT
		        PrintStream old_out = System.out;
		        ByteArrayOutputStream pipeOut = new ByteArrayOutputStream();
		        System.setOut(new PrintStream(pipeOut));
                pipelineBratOut.process(jcas);
	            System.setOut(old_out);
	            String brat = new String(pipeOut.toByteArray());
	            pipeOut.close();
                pipeOut = new ByteArrayOutputStream();
                System.setOut(new PrintStream(pipeOut));                
                pipelineConllOut.process(jcas);
                System.setOut(old_out);
                String parsedConll = new String(pipeOut.toByteArray());

				
				
				ProfilingInput inputProf = new ProfilingInput();
				inputProf.setText(data.getText());
				inputProf.setConll(parsedConll);
				
				ProfilingClient clientProf = new ProfilingClient(serviceUrl);
				ProfilingOutput mtResult = clientProf.predict(inputProf);
				mtResult.setBrat(brat);
	            List<OutputViewerData> layers = getOutputViewerData(mtResult);
	            			
	            generateOutputView(model, req);
	            model.addAttribute("layers", layers);
	            return "baseOutput";
	
			} catch(Exception e) {
			    e.printStackTrace();
				generateInputView(model, req);
				model.addAttribute("error", e.getMessage());
				return "baseInput";
			}
		}

	}
	
}
