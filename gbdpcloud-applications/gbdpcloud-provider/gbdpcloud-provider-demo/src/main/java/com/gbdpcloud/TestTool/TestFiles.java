package com.gbdpcloud.TestTool;



import com.gbdpcloud.entity.Code;
import com.gbdpcloud.entity.Configuration;
import com.gbdpcloud.entity.ToolDeploymentInfo;

import java.util.ArrayList;

public class TestFiles {

    private ArrayList<Code> codes;
    //private Configuration configuration;
    //private ToolDeploymentInfo toolDeploymentInfo;
    private String testSoftware ;
    private String testSetName;
    private String cRule;
    private String cppRule;

    public ArrayList<Code> getCodes() {
        return codes;
    }

    public String getTestSoftware() {
        return testSoftware;
    }

    public String getTestSetName() {
        return testSetName;
    }

    public void setCodes(ArrayList<Code> codes) {
        this.codes = codes;
    }

    public void setTestSoftware(String testSoftware) {
        this.testSoftware = testSoftware;
    }

    public void setTestSetName(String testSetName) {
        this.testSetName = testSetName;
    }

    public String getcRule() {
        return cRule;
    }

    public void setcRule(String cRule) {
        this.cRule = cRule;
    }

    public String getCppRule() {
        return cppRule;
    }

    public void setCppRule(String cppRule) {
        this.cppRule = cppRule;
    }
}
