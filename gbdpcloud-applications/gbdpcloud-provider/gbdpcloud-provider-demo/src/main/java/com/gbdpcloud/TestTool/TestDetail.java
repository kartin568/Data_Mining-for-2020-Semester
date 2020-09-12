package com.gbdpcloud.TestTool;
import com.gbdpcloud.entity.Code;
import com.gbdpcloud.entity.Configuration;
import com.gbdpcloud.entity.Test;
import com.gbdpcloud.entity.ToolDeploymentInfo;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import lombok.Data;

import java.util.ArrayList;
@Data
public class TestDetail {
    private ArrayList<Code> codes;
    private Configuration configuration;
    private ToolDeploymentInfo toolDeploymentInfo;
    private Test test;
    private UacUserDto uacUser;

    private String testSetName;

    private String cRule;
    private String cppRule;
    public  void setConfiguration(Configuration configuration){
        this.configuration = configuration;
        String[] rules = configuration.getRule().split("\\+");
        cRule = rules[0].toString();
        cppRule = rules[1].toString();
    }
}