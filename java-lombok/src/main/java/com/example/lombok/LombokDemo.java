package com.example.lombok;

import lombok.Data;
import lombok.experimental.Accessors;

public class LombokDemo {

	public static void main(String[] args) {

	}
}

@Data
@Accessors(prefix = "f")
class User {
	private Integer fId;
	private String fName;
}