package com.gbdpcloud.entity;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseEntity;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacOffice;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.OfficeService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;


/**
 * 测试项目表
 *
 * @author auto generated
 * @date 2020-08-04 22:06:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "project")
@ApiModel(value = "测试项目表")
public class Project extends BaseEntity {

	/**
	 * 项目名称
	 * nullable : false
	 * default  : ''
	 */
	@ApiModelProperty(value = "项目名称")
	@Column(name = "name", nullable = true, length = 50)
	private String name;


	/**
	 * 所属型号
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "所属型号")
	@Column(name = "type", nullable = true, length = 20)
	private String type;

	/**
	 * 项目来源
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "硬件平台")
	@Column(name = "platform", nullable = true, length = 20)
	private String platform;

	/**
	 * 项目组长
	 * nullable : false
	 * default  : ''
	 */
	@ApiModelProperty(value = "项目组长")
	@Column(name = "leader", nullable = true, length = 10)
	private String leader;

	@ApiModelProperty(value = "项目来源")
	@Column(name = "source", nullable = true, length = 20)
	private String source;
	/**
	 * 项目成员
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "项目成员")
	@Column(name = "member", nullable = true, length = 255)
	private String member;

	/**
	 * 可见范围
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "可见范围")
	@Column(name = "ranges", nullable = true, length = 2000)
	private String ranges;



	/**
	 * 编译环境
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "编译环境")
	@Column(name = "compiler", nullable = true, length = 255)
	private String compiler;

	@ApiModelProperty(value = "非数据库字段-项目成员列表")
	private List<String> members;

	@ApiModelProperty(value = "非数据库字段-项目成员用户名-用户ID")
	private Map<String,String> memberMap;

	@ApiModelProperty(value = "非数据库字段-历史版本")
	private List<String> versions;

	@ApiModelProperty(value = "非数据库字段-可见部门")
	private List<UacOffice> officeList;

	@ApiModelProperty(value = "是否为新项目")
	private String is_new;
}
