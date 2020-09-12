package com.gbdpcloud.controller;

import com.gbdpcloud.Xls.TestXls;
import com.gbdpcloud.entity.ResultErr;
import com.gbdpcloud.service.ResultErrService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.excel.EasyExcel;
@Api(value = "ResultErrController")
@RequestMapping("/ResultErrController")
@Controller
@Validated
@RestController
public class ResultErrController extends BaseController {

    @Resource
    private ResultErrService resultErrService;

    @ApiOperation(value = "修改备注")
    @PutMapping("/update")
    public Result remark(@RequestBody @Valid ResultErr resultErr){
        log.info("ResultErrController update [{}]", resultErr);
        int i=resultErrService.update(resultErr);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("添加备注失败！");

    }

    @PostMapping("/testAdd")
    public void testAdd(){

        
        ResultErr err = new ResultErr();
        err.setTest_ID("1");
        err.setErr_function("1");
        err.setRule_type("1");
        err.setErr_line("1");
        err.setSource("1");
        err.setCode("1");
        err.setRule("1");
        err.setMark("1");
        resultErrService.save(err);
    }
    /*
  0813 add by ld
 */
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @ApiOperation(value = "问题排序 order:0从小到大,1从大到小")
    @GetMapping("/sort")
    public Result sort(@RequestParam("order") int order,@RequestParam("sortBy") int sortBy,
                       @RequestParam("testId") String testId){

        List<ResultErr> list = resultErrService.getByTest(testId);
        resultErrService.sort(order,sortBy,list);
        return ResultGenerator.genSuccessResult(list);
    }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/Testsort")
    public Result testSort(){
        ResultErr r1 = new ResultErr();
        ResultErr r2 = new ResultErr();
        ResultErr r3 = new ResultErr();
        r1.setRule_type("a");
        r2.setRule_type("b");
        r2.setRule_type("c");
        List<ResultErr> list = new ArrayList<ResultErr>();
        list.add(r2);
        list.add(r1);
        list.add(r3);
        resultErrService.sort(1,1,list);
        return ResultGenerator.genSuccessResult(list);
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @ApiOperation(value = "下载xls")
    @PostMapping("/xls-one")
    public void download( @RequestParam("testId") String testId,HttpServletResponse response) throws IOException {
        List<ResultErr> list = resultErrService.getByTest(testId);
        List<TestXls> xlslist = new ArrayList<TestXls>();
        for (int i =0;i<list.size();i++){
            xlslist.add(new TestXls(list.get(i)));
        }
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String name = ""+testId;
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TestXls.class).sheet("模板").doWrite(xlslist);
    }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/test-xls")
    public void testDownload(HttpServletResponse response) throws IOException {
        //List<ResultErr> list = resultErrService.getByTest(testId);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TestXls.class).sheet("模板").doWrite(getList());
    }

    public List<TestXls> getList(){
        List<TestXls> list = new ArrayList<TestXls>();
        for (int i = 0;i<10;i++){
            String s = "hhhhh"+i;
            ResultErr r = new ResultErr();
            r.setCompResult(s);
            r.setCompLine(s);
            r.setCompVsersion(s);
            r.setCode(s);
            r.setCompLine(s);
            r.setErr_function(s);
            r.setId(s);
            r.setMark(s);
            r.setRule_type(s);
            r.setSource(s);
            r.setErr_line(s);
            TestXls t = new TestXls(r);
            list.add(t);
        }
        System.out.print(list);
        return list;
    }
}
