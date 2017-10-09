package edu.upf.taln.profiling.author_profiling.demo.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HttpErrorHandler {

	private void generateErrorView(ModelAndView model){
		model.addObject("title", "Error");
		
		List<String> jsHeaders = new ArrayList<String>();
		jsHeaders.add("https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js");
		jsHeaders.add("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js");
		model.addObject("jsHeaders", jsHeaders);
		
		List<String> cssHeaders = new ArrayList<String>();
		cssHeaders.add("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css");
		cssHeaders.add("resources/core/css/base.css");
		cssHeaders.add("resources/core/css/error.css");
		model.addObject("cssHeaders", cssHeaders);
	}
	
	@RequestMapping(value="/404")
	public ModelAndView error404(){
		ModelAndView mav = new ModelAndView();
		generateErrorView(mav);
		mav.addObject("exception", String.valueOf("404"));
		mav.addObject("message", "Page not found");
		mav.setViewName("error");
		return mav;
	}
	
	@RequestMapping(value="/500")
	public ModelAndView error500(){
		ModelAndView mav = new ModelAndView();
		generateErrorView(mav);
		mav.addObject("exception", String.valueOf("500"));
		mav.addObject("message", "Internal server error");
		mav.setViewName("error");
		return mav;
	}
}
