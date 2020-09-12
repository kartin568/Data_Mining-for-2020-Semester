package com.gbdpcloud.controller;


import com.gbdpcloud.service.UacRoleService;
import com.gbdpcloud.service.UacRoleUserService;
import com.gbdpcloud.service.UacUserOfficeService;
import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.ResetPasswordDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.page.PageRequest;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.*;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.RoleUserService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.UserOfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(value = "UacUserController")
@RequestMapping("/UacUserController")
@Controller
@Validated
@RestController
public class UacUserController extends BaseController {

    @Resource
    private UacUserService uacUserService;
    @Resource
    private UserService userService;
    @Resource
    private UserOfficeService userOfficeService;
    @Resource
    private RoleUserService roleUserService;
    @Resource
    private UacUserOfficeService uacUserOfficeService;
    @Resource
    private UacRoleUserService uacRoleUserService;
    @Resource
    private UacRoleService uacRoleService;
    @Resource
    private UserLogController userLogController;


    @GetMapping("/test/{id}")
    public Result T(@PathVariable @Valid String id){
        return ResultGenerator.genSuccessResult("ok"+id);
    }

    @GetMapping("/getByName/{name}")
    @ApiOperation(value = "通过名字获取用户列表")
    public Result getByName(@PathVariable String name){
        //List<UacUser> uacUser = uacUserService.getByName(name);
        //UacUser uacUser = uacUserService.getInfoById(id);
        return ResultGenerator.genSuccessResult(uacUserService.getByName(name));
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/get/{id}")
    @ApiOperation(value = "通过id获取用户信息")
    public Result getInfo(@PathVariable String id){
        //UacUser uacUser = uacUserService.getByName(name);
        //UacUser uacUser = uacUserService.getInfoById(id);
        return ResultGenerator.genSuccessResult(userService.get(id));
    }
    @GetMapping("/checkId/{id}")
    @ApiOperation(value = "检查id是否存在")
    public Result checkId(@PathVariable String id){
        Result r = null ;
        if(isAdmin()){
            //UacUser uacUser = uacUserService.getById(id);
            if(null == userService.get(id).getData()){
                r = ResultGenerator.genSuccessResult("0");
            }else{
                r =  ResultGenerator.genFailResult("id已经存在");
            }
        }else{
            r = ResultGenerator.genFailResult("没有权限");
        }
        return r;
    }

    @GetMapping("/all")
    @ApiOperation(value = "获得全部用户列表")
    public Result getList(){//@ModelAttribute @Valid PageRequest var1){
        List<UacUser> list = uacUserService.getAll();
        List<UacUser> r = new ArrayList<UacUser>();

        return ResultGenerator.genSuccessResult(list);
        // return userService.list(var1);
    }

    @GetMapping("/Testall")
    @ApiOperation(value = "TEST获得全部用户列表")
    public Result TestgetList(){
        //List<UacUser> list = uacUserService.getAll();
        //return ResultGenerator.genSuccessResult(list);
        PageRequest p = new PageRequest();
        //p.setOrderBy();
        p.setPageNum(1);
        p.setPageSize(10);
        p.setOrderBy("create_date");
        p.setOrderRule("desc");
        return userService.list(p);

    }

    @GetMapping("/del/{id}")
    @ApiOperation(value = "删除用户")
    public Result delByname(@PathVariable String id){
        Result r = null ;
        if(isAdmin()){
            r = userService.delete(id);
            userLogController.addLog("删除了用户"+ uacUserService.getById(id).getName());
            //UacUser uacUser = uacUserService.getById(id);
            //int i=uacUserService.deleteById(id);
            //r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("deleteById失败");
        }else{
            r = ResultGenerator.genFailResult("删除失败，没有权限");
        }
        return r;
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/delbatch")
    @ApiOperation(value = "批量删除用户")
    public Result delByids(@RequestBody List<String> ids){
        Result r = null ;
        if(isAdmin()){
            int i = 0 ;
            String namelist = "";
            for (String s:ids){
                namelist = namelist + uacUserService.getById(s).getName() + "、";
                userService.delete(s);
            }
            r = ResultGenerator.genSuccessResult();

            namelist = namelist.substring(0,namelist.length()-1);
            userLogController.addLog("批量删除了用户" + namelist);
            //UacUser uacUser = uacUserService.getById(id);
            //int i=uacUserService.deleteById(id);
            //r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("deleteById失败");
        }else{
            r = ResultGenerator.genFailResult("删除失败，没有权限");
        }
        return r;
    }


    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/getRoleList")
    @ApiOperation(value = "获得Rolelist")
    public Result getRoleList(){
        return  ResultGenerator.genSuccessResult(uacRoleService.listAll());
    }



    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/add")
    @ApiOperation(value = "添加用户")
    public Result addU(@RequestBody @Valid UacUser uacUser){//,@RequestParam("office") @Valid String office ){
        Result r = null ;
        if(isAdmin()){
            //int i=uacUserService.saveOrUpdate(uacUser)
            int i = uacUserService.addU(uacUser,getCurrentUserId());
            if(i>0){
                r = ResultGenerator.genSuccessResult();
                userLogController.addLog("添加了用户");
            }else {
                r = ResultGenerator.genFailResult("update失败");
            }
        }else{
            r = ResultGenerator.genFailResult("添加失败，没有权限");
        }
        return r;
    }


    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/addTEST")
    @ApiOperation(value = "添加用户")
    public Result TestAdd(){
        UacUser u = new UacUser();
        u.setLoginName("hhh");
        u.setPassword("1234");
        u.setId("777");
        //u.setUacOffice();
        u.setStatus("ENABLE");
        return ResultGenerator.genSuccessResult(uacUserService.addU(u,"0"));
    }

    @PostMapping("/addList")
    @ApiOperation(value = "批量添加用户")
    public Result addList(@RequestBody @Valid List<UacUser> list){
        Result r = null ;
        if(isAdmin()){
            //int i=uacUserService.saveOrUpdate(uacUser);
            int i = uacUserService.addList(list,getCurrentUserId());
            if(i>0){
                r = ResultGenerator.genSuccessResult();
                String namelist = "";
                for (UacUser s:list){
                    namelist = namelist + s.getName() + "、";
                }
                namelist = namelist.substring(0,namelist.length()-1);
                userLogController.addLog("批量添加了用户" + namelist);
            }else {
                r = ResultGenerator.genFailResult("update失败");
            }
        }else{
            r = ResultGenerator.genFailResult("批量添加失败，没有权限");
        }
        return r;
    }
    @PostMapping("/addTESTList")
    @ApiOperation(value = "添加用户")
    public Result TestAddLIST(){
        List<UacUser> list = new ArrayList<UacUser>();
        for(int i = 0;i<5;i++){
            UacUser u = new UacUser();
            u.setStatus("ENABLE");
            //u.setName(""+i);
            u.setLoginName(""+i);
            UacOffice o = new UacOffice();
            o.setId("1");
            u.setUacOffice(o);
            list.add(u);

        }
        String o = "市场部";
        return ResultGenerator.genSuccessResult(uacUserService.addList(list,getCurrentUserId()));
    }

    @PostMapping("/updateUerInfo")
    @ApiOperation(value = "更新用户信息")
    public Result updateInfo(@RequestBody @Valid UacUser uacUser){
        Result r = null ;
        if(isAdmin()){
            if(null == uacUser.getId()){
                r = ResultGenerator.genFailResult("只能更新已存在的用户，缺少用户id");
            }else{
                int i = uacUserService.updateInfo(uacUser,getCurrentUserId());
                if(i>0){
                    r = ResultGenerator.genSuccessResult();
                    userLogController.addLog("更新了用户"+uacUser.getName() + "的信息");
                }else {
                    r = ResultGenerator.genFailResult("update失败");
                }
            }
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }
    @PostMapping("/updateTEST")
    @ApiOperation(value = "测试更新")
    public Result Testupdate(){
        UacUser u = new UacUser();
        //u.setLoginName("xxx");
        u.setPassword("0000");
        u.setId("777");
        //u.setUacOffice();
        u.setStatus("ENABLE");
        return ResultGenerator.genSuccessResult(uacUserService.updateInfo(u,getCurrentUserId()));
    }


    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/changePW")
    @ApiOperation(value = "修改密码")
    public Result changePW(@RequestBody @Validated ResetPasswordDto var1){
        return userService.changPassword(var1);
    }




    @GetMapping("/updateTime")
    @ApiOperation(value = "更新用户最后登录时间")
    public Result updateLogTime(){
        //getCurrentUserId();
        UacUserDto dto = UacUserUtils.getUserInfoFromRequest();
        UacUser u = new UacUser();
        u.setId(dto.getId());
        u.setLastLoginTime(new Date());
        return ResultGenerator.genSuccessResult(uacUserService.updateInfo(u,"0"));
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
        //return ADMINROLE.equals(getCurrentUserRole());
    }

    @ApiOperation(value = "获得当前用户id")
    public String getCurrentUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String current_user_id = (String) request.getSession().getAttribute("user_id");

        // !!!!!!!!!
        if(null ==current_user_id ){
            current_user_id = "0";
        }
        return current_user_id;
    }


    /*
        //@CrossOrigin(origins = {"http://localhost:9527", "null"})
        @ApiOperation(value = "添加用户-部门关系")
        @PostMapping("/addUserOffice")
        public Result addUserOfficexx(@RequestBody @Valid  UacUserOffice  var1){
            //userOfficeService.get("1");
            //return userOfficeService.save(var1);
            return ResultGenerator.genSuccessResult( uacUserOfficeService.save(var1));
        }

        @CrossOrigin(origins = {"http://localhost:9527", "null"})
        @PostMapping("/delUserOffice")
        @ApiOperation(value = "删除用户-部门关系")
        public Result delUserOffice(@RequestBody @Valid UacUserOffice  var1){
            UacUserOffice t = uacUserOfficeService.getByUserAndOffice(var1.getUserId(),var1.getOfficeId());
            Result r = null;
            if(null != t){
                r =  ResultGenerator.genSuccessResult( uacUserOfficeService.del(t.getId())   );//userOfficeService.delete(t.getId());
            }else{
                r = ResultGenerator.genFailResult("该关系不存在");
            }
            return r;
        }

        @CrossOrigin(origins = {"http://localhost:9527", "null"})
        @PostMapping("/addUserRole")
        @ApiOperation(value = "添加用户-角色关系")
        public Result addUserRole(@RequestBody UacRoleUser var1){
            //roleUserService.get("10");
            return ResultGenerator.genSuccessResult( uacRoleUserService.save(var1));//roleUserService.save(var1);
        }

        @CrossOrigin(origins = {"http://localhost:9527", "null"})
        @PostMapping("/delUserRole")
        @ApiOperation(value = "删除用户-部门关系")
        public Result delUserRole(@RequestBody  UacRoleUser var1){
            //UacUserOffice t = uacUserOfficeService.getByUserAndOffice(var1.getUserId(),var1.getOfficeId());
            UacRoleUser t = uacRoleUserService.getByRidAuid(var1.getRoleId(),var1.getUserId());
            Result r = null;
            if(null != t){
                //r = roleUserService.delete(t.getId());
                r =  ResultGenerator.genSuccessResult( uacRoleUserService.del(t.getId())   );
            }else{
                r = ResultGenerator.genFailResult("该关系不存在");
            }
            return r;
        }
    */


}
