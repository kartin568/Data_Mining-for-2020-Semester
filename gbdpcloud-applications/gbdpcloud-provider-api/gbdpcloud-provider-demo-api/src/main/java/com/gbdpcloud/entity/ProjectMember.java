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
 * ProjectMember
 *
 * @author auto generated
 * @date 2020-08-09 21:51:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "project_member")
@ApiModel(value = "ProjectMember")
public class ProjectMember extends BaseEntity {

	/**
	 * 项目名
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "项目ID")
	@Column(name = "project_ID", nullable = true, length = 50)
	private String project_ID;

	/**
	 * 项目成员
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "项目成员ID")
	@Column(name = "member_ID", nullable = true, length = 50)
	private String member_ID;
}
