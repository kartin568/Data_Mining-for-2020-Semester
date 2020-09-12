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
 * 配置方案表
 *
 * @author auto generated
 * @date 2020-08-04 22:06:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "configuration")
@ApiModel(value = "配置方案表")
public class Configuration extends BaseEntity {



	/**
	 * 配置方案名称
	 * nullable : false
	 * default  : null
	 */
	@ApiModelProperty(value = "配置方案名称")
	@Column(name = "name", nullable = true, length = 50)
	private String name;

	/**
	 * 包含工具
	 * nullable : false
	 * default  : ''
	 */
	@ApiModelProperty(value = "包含工具")
	@Column(name = "tools", nullable = true, length = 255)
	private String tools;

	/**
	 * 规则配置
	 * nullable : true
	 * default  : ''
	 */
	@ApiModelProperty(value = "规则配置")
	@Column(name = "rule", nullable = true, length = 255)
	private String rule;


	/**
	 * 硬件平台
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "硬件平台")
	@Column(name = "platform", nullable = true, length = 20)
	private String platform;

	/**
	 * 编译器
	 * nullable : true
	 * default  : ''
	 */
	@ApiModelProperty(value = "编译器")
	@Column(name = "translater", nullable = true, length = 20)
	private String translater;

	/**
	 * 引用头文件
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "引用头文件")
	@Column(name = "header", nullable = true, length = 255)
	private String header;

	/**
	 * 宏定义
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "宏定义")
	@Column(name = "define", nullable = true, length = 255)
	private String define;


	/**
	 * 是否为默认字段（1.是，0.否）
	 * nullable : false
	 * default  : '1'
	 */
	@ApiModelProperty(value = "是否为默认字段（1.是，0.否）")
	@Column(name = "is_default", nullable = true, length = 1)
	private String is_default;

	/**
	 * 是否为公共方案（1.是，0否）
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "是否为公共方案（1.是，0否）")
	@Column(name = "is_common", nullable = true, length = 1)
	private String is_common;
}
