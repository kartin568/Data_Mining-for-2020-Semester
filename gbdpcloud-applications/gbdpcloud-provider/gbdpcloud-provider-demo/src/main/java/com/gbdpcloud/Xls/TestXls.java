package com.gbdpcloud.Xls;

import com.alibaba.excel.annotation.ExcelProperty;
import com.gbdpcloud.entity.ResultErr;
import lombok.Data;

/*
<dependency>
     <groupId>com.alibaba</groupId>
     <artifactId>easyexcel</artifactId>
     <version>1.1.2-beta5</version>
</dependency>
 */

@Data
public class TestXls {
    @ExcelProperty("编号")
    private String id;

    @ExcelProperty("标记")
    private String sta;

    @ExcelProperty("问题类型")
    private String rule_type;


    @ExcelProperty("源文件")
    private String sor;

    @ExcelProperty("函数")
    private String fun;

    @ExcelProperty("代码行号")
    private String line;

    @ExcelProperty("批注")
    private String com;

    @ExcelProperty("规则")
    private String rule;

    @ExcelProperty("编码规则")
    private String code;

    public TestXls(){}

    public TestXls(ResultErr err){
        id = err.getId();
        sta = err.getMark();
        rule_type = err.getRule_type();
        sor = err.getSource();
        fun = err.getErr_function();
        line = err.getErr_line();
        rule = err.getRule();
        code = err.getCode();
    }
    /*
    			"id": "52c1ffe0-b682-4904-bd3d-b91944a4992c",
			"createBy": "123",
			"createDate": 1599719091000,
			"updateBy": "123",
			"updateDate": 1599719091000,
			"remarks": "",
			"test_ID": "48b50d94-f690-420c-9e5c-331a053988ae",
			-"rule": "Expression needs brackets.",
			-"code": "GJB_8114 R-1-2-5",
			-"rule_type": "M",
			-"source": "Source.cpp",
			-"err_function": "lock",
			-"err_line": "19",
			-"mark": "1",
			"compVsersion": "cf6e7646-1bb1-4551-bcba-501e363dcfdd",
			"compLine": "33",
			"compResult": "相同"
     */

}
