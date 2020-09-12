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
 * CodeVersion
 *
 * @author auto generated
 * @date 2020-08-11 22:15:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "code_version")
@ApiModel(value = "CodeVersion")
public class CodeVersion extends BaseEntity {

	/**
	 * 项目id
	 * nullable : false
	 * default  : ''
	 */
	@ApiModelProperty(value = "项目id")
	@Column(name = "project_ID", nullable = true, length = 50)
	private String project_ID;


	@ApiModelProperty(value = "项目版本")
	@Column(name = "version", nullable = true, length = 20)
	private String version;


}
