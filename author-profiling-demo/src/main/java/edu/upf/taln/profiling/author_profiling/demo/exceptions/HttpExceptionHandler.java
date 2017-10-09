package edu.upf.taln.profiling.author_profiling.demo.exceptions;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class HttpExceptionHandler {
	
	private void generateExceptionView(ModelAndView model){
		model.addObject("title", "Error");
		
		List<String> jsHeaders = new ArrayList<String>();
		jsHeaders.add("https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js");
		jsHeaders.add("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js");
		model.addObject("jsHeaders", jsHeaders);
		
		List<String> cssHeaders = new ArrayList<String>();
		cssHeaders.add("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css");
		cssHeaders.add("../resources/core/css/base.css");
		cssHeaders.add("../resources/core/css/error.css");
		model.addObject("cssHeaders", cssHeaders);
	}

	@ExceptionHandler({BadRequestException.class, 
						NotAuthorizedException.class,
						ForbiddenException.class,
						NotAllowedException.class,
						NotAcceptableException.class,
						NotSupportedException.class,
						InternalServerErrorException.class,
						ServiceUnavailableException.class,
						NotFoundException.class,
						ClientErrorException.class,
						ServerErrorException.class})
	public ModelAndView defaultErrorHandler(BadRequestException ex) {

		ModelAndView mav = new ModelAndView();
		generateExceptionView(mav);
		mav.addObject("exception", String.valueOf(ex.getResponse().getStatus()) + " - " + ex.getResponse().getStatusInfo().getReasonPhrase());
		mav.addObject("message", ex.getMessage());
		mav.setViewName("error");
		return mav;
	}
	
}
