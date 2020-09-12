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
 * 测试工具部署信息表
 *
 * @author auto generated
 * @date 2020-08-04 22:06:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tool_deployment_info")
@ApiModel(value = "测试工具部署信息表")
public class ToolDeploymentInfo extends BaseEntity {


	/**
	 * 测试工具名
	 * nullable : false
	 * default  : null
	 */
	@ApiModelProperty(value = "测试工具名")
	@Column(name = "tool", nullable = true, length = 20)
	private String tool;

	/**
	 * 服务器地址
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "服务器地址")
	@Column(name = "host", nullable = true, length = 20)
	private String host;

	/**
	 * 服务器接口
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "服务器端口")
	@Column(name = "port", nullable = true)
	private Integer port;

	/**
	 * 连接状态
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "连接状态")
	@Column(name = "state", nullable = true, length = 5)
	private String state;

}
