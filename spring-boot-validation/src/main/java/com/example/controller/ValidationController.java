package com.example.controller;

import com.example.form.ValidationParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidationController {

	@RequestMapping("/validation1")
	public Object validation1(@Validated ValidationParam param) {
		return param;
	}

	@RequestMapping("/validation2")
	public Object validation2(@Validated ValidationParam param, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return bindingResult.getFieldErrors();
		}
		return param;
	}

}
