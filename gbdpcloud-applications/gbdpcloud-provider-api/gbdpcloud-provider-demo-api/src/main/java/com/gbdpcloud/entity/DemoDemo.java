package com.gbdpcloud.entity;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(name = "demo_demo")
public class DemoDemo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "demoStr不能为空")
    private String demoStr;

}
