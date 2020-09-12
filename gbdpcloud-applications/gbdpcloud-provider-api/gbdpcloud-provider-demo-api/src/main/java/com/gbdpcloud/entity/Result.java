package com.gbdpcloud.entity;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 静态测试结果表
 *
 * @author auto generated
 * @date 2020-08-04 22:06:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "result")
@ApiModel(value = "静态测试结果表")
public class Result extends BaseEntity {


	/**
	 * 所属项目名称
	 * nullable : false
	 * default  : ''
	 */
	@ApiModelProperty(value = "所属项目名称")
	@Column(name = "project", nullable = true, length = 50)
	private String project;

	/**
	 * 代码版本
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "代码版本")
	@Column(name = "code_version", nullable = true, length = 20)
	private String code_version;

	/**
	 * 配置方案
	 * nullable : false
	 * default  : ''
	 */
	@ApiModelProperty(value = "配置方案")
	@Column(name = "configuration", nullable = true, length = 255)
	private String configuration;

	/**
	 * 源码工程
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "源码工程")
	@Column(name = "source", nullable = true, length = 255)
	private String source;

	/**
	 * 筛选条件
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "筛选条件")
	@Column(name = "filter", nullable = true, length = 255)
	private String filter;

	/**
	 * 任务执行标识
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "任务执行标识")
	@Column(name = "task_ID", nullable = true)
	private Integer task_ID;


	/**
	 * 代码标识
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "代码标识")
	@Column(name = "code_ID", nullable = true, length = 50)
	private String code_ID;

	/**
	 * 测试计划标识
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "测试计划标识")
	@Column(name = "test_ID", nullable = true, length = 30)
	private String test_ID;

	/**
	 * 代码名称
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "代码名称")
	@Column(name = "code_name", nullable = true, length = 20)
	private String code_name;

	/**
	 * 测试结果
	 * nullable : false
	 * default  : ''
	 */
	@ApiModelProperty(value = "测试结果")
	@Column(name = "result", nullable = true, length = 255)
	private String result;

	/**
	 * 结果是否正确（1.正确，0.错误）
	 * nullable : false
	 * default  : '1'
	 */
	@ApiModelProperty(value = "结果是否正确（1.正确，0.错误）")
	@Column(name = "status", nullable = true, length = 1)
	private String status;
}
