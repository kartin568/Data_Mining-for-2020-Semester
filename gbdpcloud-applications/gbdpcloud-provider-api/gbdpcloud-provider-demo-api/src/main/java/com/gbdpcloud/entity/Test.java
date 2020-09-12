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
 * 测试计划表
 *
 * @author auto generated
 * @date 2020-08-04 22:06:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "test")
@ApiModel(value = "测试计划表")
public class Test extends BaseEntity {

	/**
	 * 所属项目名称
	 * nullable : false
	 * default  : ''
	 */
	@ApiModelProperty(value = "所属项目名称")
	@Column(name = "project", nullable = true, length = 30)
	private String project;

	/**
	 * 测试项目标识
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "测试项目标识")
	@Column(name = "project_ID", nullable = true, length = 30)
	private String project_ID;

	/**
	 * 代码版本
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "代码版本")
	@Column(name = "code_version", nullable = true, length = 20)
	private String code_version;

	/**
	 * 源码工程
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "源码工程")
	@Column(name = "source", nullable = true, length = 255)
	private String source;

	/**
	 * 配置方案
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "配置方案")
	@Column(name = "configuration", nullable = true, length = 255)
	private String configuration;

	@ApiModelProperty(value = "配置方案id")
	@Column(name = "configuration_ID", nullable = true, length = 64)
	private String configuration_ID;


	/**
	 * 代码标识
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "代码标识")
	@Column(name = "code_ID", nullable = true, length = 50)
	private String code_ID;

	/**
	 * 代码名称
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "代码名称")
	@Column(name = "code_name", nullable = true, length = 20)
	private String code_name;

	/**
	 * 计划执行状态码
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "计划执行状态码")
	@Column(name = "status", nullable = true, length = 255)
	private String status;
}
