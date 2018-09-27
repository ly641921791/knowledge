package com.example.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ValidationParam {
	@NotBlank
	private String name;
}
