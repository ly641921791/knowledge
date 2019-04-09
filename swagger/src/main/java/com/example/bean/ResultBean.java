package com.example.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ly
 */
@Data
@ApiModel("结果Bean")
public class ResultBean {

	@ApiModelProperty("id")
	private Integer id;

	@ApiModelProperty("名字")
	private String name;

}
