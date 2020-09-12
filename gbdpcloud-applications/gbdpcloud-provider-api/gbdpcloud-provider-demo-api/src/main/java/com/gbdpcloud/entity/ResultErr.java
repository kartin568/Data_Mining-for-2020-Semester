package com.gbdpcloud.entity;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * ResultErr
 *
 * @author auto generated
 * @date 2020-08-04 22:06:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "result_err")
@ApiModel(value = "ResultErr")
public class ResultErr extends BaseEntity {

	/**
	 * 测试结果标识
	 * nullable : false
	 * default  : null
	 */
	@ApiModelProperty(value = "测试计划标识")
	@Column(name = "test_ID", nullable = true, length = 50)
	private String test_ID;

	/**
	 * 违反的规则
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "违反的规则")
	@Column(name = "rule", nullable = true, length = 255)
	private String rule;

	/**
	 * 规则的编码
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "规则的编码")
	@Column(name = "code", nullable = true, length = 64)
	private String code;

	/**
	 * 问题类型
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "问题类型")
	@Column(name = "rule_type", nullable = true, length = 50)
	private String rule_type;

	/**
	 * 源文件
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "源文件")
	@Column(name = "source", nullable = true, length = 255)
	private String source;

	/**
	 * 函数
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "函数")
	@Column(name = "err_function", nullable = true, length = 255)
	private String err_function;

	/**
	 * 代码行号
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "代码行号")
	@Column(name = "err_line", nullable = true, length = 255)
	private String err_line;


	/**
	 * 结果标识（1.正确，2.错误)
	 * nullable : false
	 * default  : '1'
	 */
	@ApiModelProperty(value = "结果标识（Y.正确，N.错误)")
	@Column(name = "mark", nullable = true, length = 32)
	private String mark;


	/*
	  0813 add by ld
	 */
	@ApiModelProperty(value = "对比版本")
	private String compVsersion;

	@ApiModelProperty(value = "对比行号")
	private String compLine;

	@ApiModelProperty(value = "对比结果")
	private String compResult;
}
