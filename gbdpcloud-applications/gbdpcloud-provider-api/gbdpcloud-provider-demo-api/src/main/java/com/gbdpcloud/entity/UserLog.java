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
 * log日志表
 *
 * @author auto generated
 * @date 2020-08-04 22:06:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_log")
@ApiModel(value = "log日志表")
public class UserLog extends BaseEntity {

	/**
	 * 用户名
	 * nullable : false
	 * default  : null
	 */
	@ApiModelProperty(value = "用户名")
	@Column(name = "username", nullable = true, length = 30)
	private String username;

	@ApiModelProperty(value = "用户id")
	@Column(name = "user_id", nullable = true, length = 64)
	private String user_id;

	/**
	 * 操作内容
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "操作内容")
	@Column(name = "operate", nullable = true, length = 255)
	private String operate;

}
