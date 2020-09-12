package com.gbdpcloud.controller;

import com.alibaba.excel.EasyExcel;
import com.gbdpcloud.Xls.TestCompXls;
import com.gbdpcloud.entity.*;
import com.gbdpcloud.service.*;
import com.github.pagehelper.PageHelper;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.page.PageRequest;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.*;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.*;

@Api(value = "MasterController")
@RequestMapping("")
@Validated
@RestController
public class MasterController extends BaseController {

    public String CODE_BASE_PATH="D:/Project/";
    @Resource
    private CodeService codeService;

    @Resource
    private ConfigurationService configurationService;

    @Resource
    private ProjectService projectService;

    @Resource
    private ResultService resultService;

    @Resource
    private ResultErrService resultErrService;

    @Resource
    private TestService testService;

    @Resource
    private ToolDeploymentInfoService toolDeploymentInfoService;

    @Resource
    private UserLogService userLogService;

    @Resource
    private UacUserService uacUserService;

    @Resource
    private UacOfficeService uacOfficeService;

    @Resource
    private UacUserOfficeService uacUserOfficeService;

    @Resource
    private ProjectMemberService projectMemberService;

    @Resource
    private CodeVersionService codeVersionService;

    @Resource
    private OfficeService officeService;

    @Resource
    private CodeController codeController;

    @Resource
    private UserService userService;



    @ApiOperation(value = "我的项目-首页")
    @GetMapping("/project/index")
    public Result project_index() {
        UacUserDto user= UacUserUtils.getUserInfoFromRequest();
        List<ProjectMember> list=projectMemberService.getByMember(user.getId());
        System.out.println(list.size());

        String ids="";
        for(int i=0;i<list.size();i++){
            String project_ID=list.get(i).getProject_ID();
           ids+=project_ID+",";
        }
        ids=ids.substring(0,ids.length()-1);
        List<Project> list1=projectService.selectByIds(ids);

        System.out.println("项目个数为："+list1.size());

        List<String> office_ids= UacUserUtils.getOfficeId(user);

        List<Project> deleteList = new ArrayList<Project>();
        for(int i=0;i<list1.size();i++)
        {
            Project project=list1.get(i);
            int delete_mark=0;
           String range=project.getRanges();
           String [] strs=range.split(",");
           for(int j=0;j<strs.length;j++)
           {
               if(office_ids.contains(strs[j]) ||strs[j].equals("project")){
                   delete_mark=1;
                   break;
               }
           }
           if(delete_mark==0){
               deleteList.add(project);
           }
        }
        list1.removeAll(deleteList);
        for(int i=0;i<list1.size();i++)
        {
            Project p=list1.get(i);
            List<String> version=new ArrayList<String>();
            List<CodeVersion> codes=codeVersionService.getByProject(p.getId());
            for(int j=0;j<codes.size();j++)
            {
                version.add(codes.get(j).getVersion());
            }
            version.sort(Comparator.comparing(String::toString));
            p.setVersions(version);
            list1.set(i,p);
        }
        for(Project p:list1)
        {
            if(p.getCreateDate().getTime()-user.getLastLoginTime().getTime()>0)
            {
                p.setIs_new("1");
            }else
                p.setIs_new("0");
        }
        list1.sort(Comparator.comparing(Project::getCreateDate,Comparator.reverseOrder()));

        return ResultGenerator.genSuccessResult(list1);
    }

    @ApiOperation(value = "我的项目-基础信息")
    @GetMapping("/project/projectInfo/{id}")
    public Result project_projectInfo(@PathVariable @Valid @NotBlank(message = "项目标识不能为空") String id){
        Project project = projectService.selectById(id);
        if(project==null)
        {
           return ResultGenerator.genFailResult("未找到项目");
        }
        if(project.getMember()!=null&&project.getMember().length()>0) {
            String[] list = project.getMember().split(",");
            Map<String, String> memberMap = new HashMap<>();
            for (int i = 0; i < list.length; i++) {
                String member_ID = list[i];
                String member = uacUserService.getById(member_ID).getLoginName();
                memberMap.put(member, member_ID);
            }
            project.setMemberMap(memberMap);
        }
        List<String> ids=new ArrayList<>(Arrays.asList(project.getRanges().split(",")));
        String pro="project";
        if(ids.contains(pro)&&ids.size()>1)
        {
            ids.remove(pro);
        }
        if(!ids.get(0).equals(pro)) {
            List<UacOffice> offices = uacOfficeService.getByIds(ids);
            project.setOfficeList(offices);
        }


        UacOffice uac=new UacOffice();
        UacUser user=new UacUser();
        //List<UacOffice> officeList=uacOfficeService.list(uac);
        //officeList.sort(Comparator.comparing(UacOffice::getSort,Comparator.reverseOrder()));
        List<Object> lists=new ArrayList<>();
        List<UacUser> uacUserList=uacUserService.list(user);
        lists.add(project);
        //lists.add(officeList);
        lists.add(uacUserList);
        return ResultGenerator.genSuccessResult(lists);
    }

    @ApiOperation(value = "我的项目-新建项目")
    @GetMapping("/project/add")
    public Result project_add(UacUser uacUser, UacOffice uacOffice){

        List<Object> lists=new ArrayList<>();
        List<UacUser> list1=uacUserService.list(uacUser);
        List<UacOffice> list2=uacOfficeService.list(uacOffice);
        lists.add(list1);
        lists.add(list2);
        System.out.println(list1.size());
        System.out.println(list2.size());

        return ResultGenerator.genSuccessResult(lists);
    }

    @ApiOperation(value = "系统配置")
    @GetMapping("/systemConfig/index")
    public Result systemConfig_index(ToolDeploymentInfo tools){
        List<ToolDeploymentInfo> list=toolDeploymentInfoService.list(tools);
        for(int i=0;i<list.size();i++)
        {
            ToolDeploymentInfo tool=list.get(i);
            String hostname=tool.getHost();
            Integer port=tool.getPort();
            try{
            Socket connect = new Socket();
            connect.connect(new InetSocketAddress(hostname, port),100);
            boolean res = connect.isConnected();
            tool.setState("正常");
            }catch (IOException exception){
                tool.setState("连接异常");
            }finally {
                list.set(i,tool);
            }
        }
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/systemLog/index")
    @ApiOperation(value = "系统日志")
    public Result systemLog_index(UserLog userLog){
        List<UserLog> list=userLogService.list(userLog);
        list.sort(Comparator.comparing(UserLog::getCreateDate,Comparator.reverseOrder()));
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "配置管理")
    @GetMapping("/configmgmt/index")
    public Result configmgmt_index(){
        List<Configuration> list=configurationService.getCommon();
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "用户管理-部门管理")
    @GetMapping("/usermgmt/orgmgmt")
    public Result usermgmt_orgmgmt(){
        return ResultGenerator.genSuccessResult( officeService.listAll());
    }

    @ApiOperation(value = "用户管理-首页")
    @GetMapping("/usermgmt/index")
    public Result usermgmt_index(UacUser uacUser){
        List<UacUser> list=uacUserService.list(uacUser);
        for(int i=0;i<list.size();i++){
            UacUser user=list.get(i);
            String user_id=user.getId();
            List<UacUserOffice> officeList=uacUserOfficeService.getByUserId(user_id);
            for(UacUserOffice userOffice:officeList) {
                String office_id = userOffice.getOfficeId();
                UacOffice uacOffice = uacOfficeService.getById(office_id);
                user.setUacOffice(uacOffice);
            }
            list.set(i,user);
        }
        //PageRequest pageRequest=new PageRequest();
       // pageRequest.setPageSize(20);
        list.sort(Comparator.comparing(UacUser::getCreateDate,Comparator.reverseOrder()));
        return ResultGenerator.genSuccessResult(list);
    }


    @ApiOperation(value = "个人信息-首页")
    @GetMapping("/profile/index")
    public Result self_index()
    {
        UacUserDto user=UacUserUtils.getUserInfoFromRequest();
        return ResultGenerator.genSuccessResult(user);
    }

    @ApiOperation(value = "我的项目-分析结果")
    @GetMapping("/project/analysisResult/{id}")
    public Result project_analysisResult(@PathVariable @Valid @NotBlank(message = "项目标识不能为空") String id){
        //TODO 状态码更新
        List<Test> list=testService.getByProject(id);
        List<Configuration> commonList=configurationService.getCommon();
        String user_id= UacUserUtils.getUserInfoFromRequest().getId();
        List<Configuration> privateList=configurationService.getPrivate(user_id);
        List<Object> lists=new ArrayList<Object>();
        List<CodeVersion> codeVersionList=codeVersionService.getByProject(id);
        list.sort(Comparator.comparing(Test::getCreateDate,Comparator.reverseOrder()));
        lists.add(list);
        lists.add(commonList);
        lists.add(privateList);
        lists.add(codeVersionList);
        return ResultGenerator.genSuccessResult(lists);
    }

    @ApiOperation(value = "我的项目-查看分析")
    @GetMapping("/project/resultErr/{id}")
    public Result project_resultErr(@PathVariable @Valid @NotBlank(message = "测试计划标识不能为空") String id){
        Test t=testService.getById(id);
        String code_version=t.getCode_version();
        String project_id=t.getProject_ID();
        List<Code> codes=codeService.getByProjectAndVersion(project_id,code_version);
        List<ResultErr> results=resultErrService.getByTest(id);
        String path=CODE_BASE_PATH+project_id+"/"+code_version;
        List<Object> lists=new ArrayList<Object>();
        lists.add(results);
        System.out.println(path);
        lists.add(codeController.fileTree(path,codes));
        return ResultGenerator.genSuccessResult(lists);
    }

    @ApiOperation(value = "我的项目-代码管理")
    @GetMapping("/project/codemgmt/{id}")
    public Result project_codemgmt(@PathVariable @Valid @NotBlank(message = "项目标识不能为空") String id){

        List<CodeVersion> list=codeVersionService.getByProject(id);
        list.sort(Comparator.comparing(CodeVersion::getVersion,Comparator.reverseOrder()));
        return ResultGenerator.genSuccessResult(list);


    }

    @ApiOperation("我的项目-代码浏览")
    @GetMapping("/project/codeView/{id}")
    public Result project_codeView(@PathVariable @Valid @NotBlank(message = "代码工程id不能为空") String id,
                                   @RequestParam(value = "project_id")String project_id,
                                   @RequestParam(value = "version")String version){
        List<Code> list=codeService.getByCodeVersion(id);
        String path=CODE_BASE_PATH+project_id+"/"+version;
        return ResultGenerator.genSuccessResult(codeController.fileTree(path,list));
    }


    @ApiOperation(value = "问题排序 order:0从小到大,1从大到小")
    @PostMapping("/search")
    public Result search(@RequestParam(required = false,value ="order") String order,
                         @RequestParam(required = false,value ="state") String state,
                         @RequestParam(required = false,value ="oid") String oid,
                         @RequestParam(required = false,value ="nid") String nid,
                         @RequestParam(required = false,value ="function") String function,
                         @RequestParam(required = false,value ="compR") String compR ){
        List<ResultErr> old = resultErrService.getByTest(oid);
        List<ResultErr> n = resultErrService.getByTest(nid);
        comp(old,n);

        List<ResultErr> r = new ArrayList<>();
        for(ResultErr err:n){
            if( null != state &&  state.length() >0 && !err.getMark().equals(state)){
                     r.add(err);
            }
            if( null != function && function.length()>0 && ! err.getErr_function().equals(function)){
                r.add(err);
            }
            if( null != compR && compR.length()>0 && !err.getCompResult().equals(compR)){
                r.add(err);
            }
        }
        n.removeAll(r);
        if(order.equals("0")){
            resultErrService.sort(0,0,n);
        }else{
            resultErrService.sort(1,0,n);
        }
       return ResultGenerator.genSuccessResult(n);
    }

    @ApiOperation("我的项目-分析结果-对比分析")
    @GetMapping("/project/com/{oid}/{nid}")
    public Result com(@PathVariable @Valid String oid, @PathVariable @Valid  String nid){
        List<ResultErr> old = resultErrService.getByTest(oid);
        List<ResultErr> n = resultErrService.getByTest(nid);
        comp(old,n);
        return ResultGenerator.genSuccessResult(n);

    }
    @ApiOperation("分析结果的对比")
    public void comp(List<ResultErr> old,List<ResultErr> n){
        for (ResultErr r :n){
            boolean flag = false;
            for (ResultErr o:old){
                if(r.getSource().equals(o.getSource())
                        && r.getRule().equals(o.getRule())  && r.getRule_type().equals(r.getRule_type())){
                    r.setCompVsersion(o.getTest_ID());
                    r.setCompLine(o.getErr_line());
                    r.setCompResult("相同");
                    //r.setCompResult(getCompR(r,o));
                    flag = true;
                }
            }
            if(!flag){
                r.setCompResult("新增");
                r.setCompVsersion("--");
                r.setCompLine("--");
                //r.setMark("---");
            }
        }

        for(ResultErr r:old){
            boolean flag = true;
            for(ResultErr c:n){
                if(r.getSource().equals(c.getSource()) && r.getRule().equals(c.getRule())
                        && r.getRule_type().equals(c.getRule_type())){
                     flag = false;
                }
            }
            if(flag){
                String content = "---";
                ResultErr x = new ResultErr();
                x.setSource(r.getSource());
                x.setCompResult("消除");
                x.setErr_function(content);
                x.setErr_line(content);
                x.setRule(r.getRule());
                x.setCode(r.getCode());
                x.setRule_type(r.getRule_type());
                x.setCompLine(r.getErr_line());
                x.setErr_function(r.getErr_function());
                x.setCompVsersion(r.getTest_ID());
                x.setMark("Y");
                n.add(x);
            }
        }
    }
    @ApiOperation("获得对比结果的Xls")
    @GetMapping("/project/comXls/{oid}/{nid}")
    public void getCompXls(@PathVariable @Valid String oid, @PathVariable @Valid  String nid,HttpServletResponse response) throws IOException {
        List<ResultErr> old = resultErrService.getByTest(oid);
        List<ResultErr> n = resultErrService.getByTest(nid);
        comp(old,n);

        List<TestCompXls> xlslist = new ArrayList<TestCompXls>();
        for (int i =0;i<n.size();i++){
            xlslist.add(new TestCompXls(n.get(i)));
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String name = "oid="+oid+"nid="+nid;
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TestCompXls.class).sheet("模板").doWrite(xlslist);
    }

    @ApiOperation("获得两个错误的对比结果")
    private String getCompR(ResultErr r, ResultErr o) {
        return "消除/不同";
    }

    @GetMapping("/project/getCode")
    @ApiOperation("我的项目-分析结果-对比分析Code")
    public Result getCode(@RequestParam @Valid String pid, @RequestParam @Valid String version1,
                          @RequestParam @Valid String version2, @RequestParam @Valid String name){
        Code code1 = codeService.getByProjectVserionAndName(pid,version1,name);
        Code code2 = codeService.getByProjectVserionAndName(pid,version2,name);
        try {
            //code1.setSource_code();
            code1.setContent(readFile(code1.getPath()));
            code2.setContent(readFile(code2.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Code> list = new ArrayList<Code>();
        list.add(code1);
        list.add(code2);
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/project/getOneCode")
    @ApiOperation("我的项目-查看分析-拿取Code")
    public Result getOneCode(@RequestParam @Valid String pid,
                          @RequestParam @Valid String version, @RequestParam @Valid String name){
        Code code= codeService.getByProjectVserionAndName(pid,version,name);
        try {
            code.setContent(readFile(code.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult(code);
    }


    @ApiOperation("文件转String")
    public String readFile(String path) throws IOException {
        File file = new File(path);
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();
        String temp = "";
        while ((temp = br.readLine()) != null) {
            sb.append(temp + "\n");
        }
        br.close();
        return sb.toString();
    }
    @GetMapping("/project/testFile")
    public Result testFile(){
        Code code = new Code();
        code.setPath("D://1.txt");
        try {
            code.setContent(readFile(code.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Code> list = new ArrayList<Code>();
        list.add(code);
        return ResultGenerator.genSuccessResult(list);
    }


}

