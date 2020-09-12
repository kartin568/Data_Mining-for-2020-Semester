package com.gbdpcloud.controller;

import com.gbdpcloud.mapper.UacOfficeMapper;
import com.gbdpcloud.service.UacOfficeService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacOfficeDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacOffice;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.OfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;

@Api(value = "UacOfficeController")
@RequestMapping("/UacOfficeController")
@Controller
@Validated
@RestController


public class UacOfficeController extends BaseController {

    @Resource
    private UacOfficeService uacOfficeService;

    @Resource
    private UacOfficeMapper uacOfficeMapper;

    //@Resource
    //private OfficeService officeService;
    private String ADMINROLE = "0";
    
    @Resource
    private UserLogController userLogController;

   /* @PostMapping("/del/{id}")
    @ApiOperation(value = "删除机构")
    public Result delByname(@PathVariable String id){
        Result r = null ;
        if(isAdmin()){
            int i= uacOfficeService.delete(id,getCurrentUserId());
            r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("deleteById失败");
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增机构")
    public Result add(@RequestBody @Valid UacOffice uacOffice){
        Result r = null ;
        if(isAdmin()){
            int i= uacOfficeService.add(uacOffice,getCurrentUserId());
            r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("新增失败");
        }else{
            r = ResultGenerator.genFailResult("新增失败，没有权限");
        }
        return r;
    }


    @PostMapping("/update")
    @ApiOperation(value = "更新机构信息")
    public Result update(@RequestBody @Valid UacOffice uacOffice){
        Result r = null ;
        if(isAdmin()){
            int i= uacOfficeService.updateInfo(uacOffice,getCurrentUserId());
            r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("新增失败");
        }else{
            r = ResultGenerator.genFailResult("新增失败，没有权限");
        }
        return r;
    }

    @GetMapping("/list")
    @ApiOperation(value = "获得机构列表")
    public Result getList(){
        Result r = null ;
        if(isAdmin()){
            r =  ResultGenerator.genSuccessResult(uacOfficeService.listAll() );
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }

    @ApiOperation(value = "获得当前用户角色")
    public String getCurrentUserRole(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String current_user_role = (String) request.getSession().getAttribute("user_role");
        return current_user_role;
    }

    @ApiOperation(value = "判断当前用户是否为管理员")
    public boolean isAdmin(){
         return true;
        // return ADMINROLE.equals(getCurrentUserRole());
    }

    public Result simple(){
        Result r = null ;
        if(isAdmin()){
            r = null;
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }

    @ApiOperation(value = "获得当前用户id")
    public String getCurrentUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String current_user_id = (String) request.getSession().getAttribute("user_id");
        return current_user_id;
    }*/

   // bug  userLogController.addLog("删除了机构" + uacOfficeService.getById(id).getName());
   @CrossOrigin(origins = {"http://localhost:9527", "null"})
   @PostMapping("/del/{id}")
   @ApiOperation(value = "删除机构")
   public Result delByname(@PathVariable String id){
       Result r = null ;
       if(isAdmin()){
           userLogController.addLog("删除了机构" + uacOfficeService.getById(id).getName());
           //r = officeService.delete(id);
           int i= uacOfficeService.delete(id,getCurrentUserId());
           //r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("deleteById失败");
       }else{
           r = ResultGenerator.genFailResult("更新失败，没有权限");
       }
       return r;
   }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/add")
    @ApiOperation(value = "新增机构")
    public Result add(@RequestBody @Valid UacOffice var1){
        Result r = null ;
        if(isAdmin()){
            //r = officeService.save(var1);
            uacOfficeService.save(var1);
            userLogController.addLog("新增了机构" + var1.getName());
            //int i= uacOfficeService.add(uacOffice,getCurrentUserId());
            // r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("新增失败");
        }else{
            r = ResultGenerator.genFailResult("新增失败，没有权限");
        }
        return r;
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/update")
    @ApiOperation(value = "更新机构信息")
    public Result update(@RequestBody  UacOffice var1){
        Result r = null ;
        if(isAdmin()){
            //r = officeService.update(var1);
            uacOfficeService.update(var1);
            userLogController.addLog("更新了机构"+ var1.getName() + "的信息");
            //int i= uacOfficeService.updateInfo(uacOffice,getCurrentUserId());
            //r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("新增失败");
        }else{
            r = ResultGenerator.genFailResult("新增失败，没有权限");
        }
        return r;
    }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/list")
    @ApiOperation(value = "获得机构列表")
    public Result getList(){
        Result r = null ;
        if(isAdmin()){
            r =  ResultGenerator.genSuccessResult(uacOfficeService.listAll() );
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }

    //bug  递归失败
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/listAsT")
    @ApiOperation(value = "获得树状机构列表")
    public Result getListAsT(){
        Result r = null ;
        if(isAdmin()){
            r =  ResultGenerator.genSuccessResult(uacOfficeService.listAsT() );
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }


    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/listLeaf")
    @ApiOperation(value = "获得叶子机构列表")
    public Result getListLeaf(){
        Result r = null ;
        if(isAdmin()){
            r =  ResultGenerator.genSuccessResult(uacOfficeService.listLeaf() );
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }

    @PostMapping("/addRoot")
    @ApiOperation(value = "更新根节点")
    public Result addRoot(@RequestBody @Valid UacOffice var1){
         uacOfficeService.save(var1);
         //uacOfficeService.addRoot(var1);
        UacOffice old = uacOfficeMapper.getRoot();
        old.setParentId(var1.getId());
        if(null == old.getParentIds()){
            old.setParentIds(var1.getId());
        }else{
            old.setParentIds(old.getParentIds()+","+var1.getId());
        }
        uacOfficeService.update(old);
        ArrayList l = new ArrayList();
        //l.add(old);
        //uacOfficeService.saveBatch(l);
        //officeService.update(old);
        uacOfficeMapper.setRoot(var1.getId());
         return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/Test")
    @ApiOperation(value = "测试")
    public Result T(){
       return ResultGenerator.genSuccessResult(uacOfficeMapper.getRoot());
    }

    public int checkId(String id){
       return 0;

    }

    @ApiOperation(value = "获得当前用户角色")
    public String getCurrentUserRole(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String current_user_role = (String) request.getSession().getAttribute("user_role");
        return current_user_role;
    }

    @ApiOperation(value = "判断当前用户是否为管理员")
    public boolean isAdmin(){
        return true;
        // return ADMINROLE.equals(getCurrentUserRole());
    }

    public Result simple(){
        Result r = null ;
        if(isAdmin()){
            r = null;
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }

    @ApiOperation(value = "获得当前用户id")
    public String getCurrentUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String current_user_id = (String) request.getSession().getAttribute("user_id");
        return current_user_id;
    }
//



}
