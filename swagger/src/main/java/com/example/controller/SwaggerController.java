package com.example.controller;

import com.example.bean.ResultBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ly
 */
@RestController
public class SwaggerController {

	@RequestMapping("/resultBean")
	public ResultBean resultBean(ResultBean resultBean) {
		return resultBean;
	}

}
