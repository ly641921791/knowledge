package com.example.json;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/json")
public class JsonController {

	@RequestMapping("/now")
	public Date now() {
		return new Date();
	}

}
