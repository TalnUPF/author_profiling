package edu.upf.taln.profiling.author_profiling.demo.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.upf.taln.demo.base.pojos.views.input.FormData;
import edu.upf.taln.profiling.author_profiling.commons.pojos.input.ProfilingInput;

@Component
public class ProfilingValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ProfilingInput.class.isAssignableFrom(clazz) || FormData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		//AnalysisInput analysisData = (AnalysisInput) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "NotEmpty.analysisInput.text");
		
	}

}
