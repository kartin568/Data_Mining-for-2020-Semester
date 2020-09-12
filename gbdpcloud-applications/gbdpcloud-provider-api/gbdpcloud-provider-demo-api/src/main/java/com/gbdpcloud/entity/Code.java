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
 * 代码工程表
 *
 * @author auto generated
 * @date 2020-08-05 16:57:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "code")
@ApiModel(value = "代码工程表")
public class Code extends BaseEntity {

	/**
	 * 所属项目名称
	 * nullable : false
	 * default  : ''
	 */
	@ApiModelProperty(value = "所属项目ID")
	@Column(name = "project_ID", nullable = true, length = 50)
	private String project_ID;

	@ApiModelProperty(value = "代码工程ID")
	@Column(name = "code_version_ID", nullable = true, length = 64)
	private String code_version_ID;

	/**
	 * 代码名称
	 * nullable : true
	 * default  : ''
	 */
	@ApiModelProperty(value = "代码名称")
	@Column(name = "name", nullable = true, length = 50)
	private String name;


	/**
	 * 源码工程
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "源码工程")
	@Column(name = "source_code", nullable = true, length = 50)
	private String source_code;

	/**
	 * 源码工程
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "代码版本")
	@Column(name = "version", nullable = true, length = 20)
	private String version;


	/**
	 * 配置文件
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "配置文件")
	@Column(name = "configuration", nullable = true, length = 255)
	private String configuration;


	/**
	 * 代码文件
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "代码文件")
	@Column(name = "path", nullable = true, length = 255)
	private String path;

	private String content;
}
