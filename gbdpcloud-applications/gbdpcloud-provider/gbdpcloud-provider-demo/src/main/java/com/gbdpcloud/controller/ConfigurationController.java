package com.gbdpcloud.controller;

import com.gbdpcloud.entity.Configuration;
import com.gbdpcloud.service.ConfigurationService;
import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Api(value = "ConfigurationController")
@RequestMapping("/ConfigurationController")
@Validated
@RestController
public class ConfigurationController extends BaseController {

    public String Base_PATH="D:/configuration/";

    @Resource
    private ConfigurationService configurationService;

    @Resource
    private UacUserService uacUserService;

    @Resource
    private UserLogController userLogController;

    private static void makeDir(String filePath) {
        if (filePath.lastIndexOf('/') > 0) {
            String dirPath = filePath.substring(0, filePath.lastIndexOf('/'));
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    @ApiOperation(value = "查看公共方案")
    @GetMapping("/getCommon")
    public Result getCommon(){
        List<Configuration> list=configurationService.getCommon();
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "查看个人方案")
    @GetMapping("/getPrivate")
    public Result getPrivate(){
        UacUserDto user= UacUserUtils.getUserInfoFromRequest();
        String user_id=user.getId();
        List<Configuration> list=configurationService.getPrivate(user_id);
        return ResultGenerator.genSuccessResult(list);
    }


    @ApiOperation(value = "添加方案")
    @PostMapping("/add")
    public Result save(@RequestParam(value = "name") String name,
                       @RequestParam(value = "tools") String tools,
                       @RequestParam(value = "rule") String rule,
                       @RequestParam(required = false,value = "is_common") String is_common,
                       @RequestParam(required = false,value = "header") MultipartFile header,
                       @RequestParam(required = false,value = "define")MultipartFile [] define)  {

        Configuration configuration=new Configuration();
        configuration.setName(name);
        configuration.setRule(rule);
        configuration.setTools(tools);
        if(is_common!=null)
        {
            configuration.setIs_common(is_common);
        }else
        {
            configuration.setIs_common("0");
        }
        configuration.setId(UUID.randomUUID().toString());
        configuration.setDelFlag("0");
        configuration.setIs_default("0");
        log.info("ConfigurationController save [{}]", configuration);
        String basePath=Base_PATH+configuration.getId()+"/"+"header"+"/";
        if(header==null)
        {
            return ResultGenerator.genFailResult("头文件不能为空");
        }

        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        String filePath = basePath + "/" + header.getOriginalFilename();
        makeDir(filePath);
        File dest = new File(filePath);
        try {
            header.transferTo(dest);
            configuration.setHeader(filePath);
        }catch (IOException e){

            ResultGenerator.genFailResult("文件上传失败！");
        }

        if (define == null || define.length == 0) {
            return ResultGenerator.genFailResult("上传文件不能为空");
        }

        basePath=Base_PATH+configuration.getId()+"/"+"define"+"/";
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        String paths="";

        for (MultipartFile file : define) {
            String Path = basePath + "/" + file.getOriginalFilename();
            makeDir(Path);
            File dest1 = new File(Path);
            try {
                file.transferTo(dest1);
                paths=paths+Path+",";

            }catch (IOException e){

                ResultGenerator.genFailResult("文件上传失败！");
            }
            }

        configuration.setDefine(paths.substring(0,paths.length()-1));

        int i = configurationService.save(configuration);

        if(i>0){
            userLogController.addLog("添加了配置方案"+name);
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("添加失败！");
        } 
    }

    @ApiOperation(value = "更新方案")
    @PostMapping("/update")
    public Result update(
            @RequestParam(value = "id") String id,
            @RequestParam(required = false,value = "name") String name,
            @RequestParam(required = false,value = "tools") String tools,
            @RequestParam(required = false,value = "rule") String rule,
            @RequestParam(required = false,value = "is_common") String is_common,
            @RequestParam(required = false,value = "is_default") String is_default,
            @RequestParam(required = false,value = "header") MultipartFile header,
            @RequestParam(required = false,value = "define")MultipartFile [] define) {

        Configuration configuration=configurationService.getById(id);
        configuration.setName(name);
        configuration.setIs_common(is_common);
        configuration.setTools(tools);
        configuration.setRule(rule);
        configuration.setId(id);
        configuration.setIs_default(is_default);
        log.info("ConfigurationController update [{}]", configuration);

       String basePath=Base_PATH+configuration.getId()+"/"+"header"+"/";

        if(header!=null&&!header.isEmpty())
        {
            if (basePath.endsWith("/")) {
                basePath = basePath.substring(0, basePath.length() - 1);
            }

            String filePath = basePath + "/" + header.getOriginalFilename();
            makeDir(filePath);
            File dest = new File(filePath);
            try {
                header.transferTo(dest);
                String filename=configuration.getHeader();

                System.out.println((filename));
                File file=new File(filename);
                if(file.exists()&&file.isFile())
                {
                    if(!file.delete()){
                        ResultGenerator.genFailResult("更新文件失败");
                    }
                }
                configuration.setHeader(filePath);
            }catch (IOException e){

                ResultGenerator.genFailResult("文件上传失败！");
            }
        }


        if (define!=null&&define.length>0) {
            System.out.println("宏定义文件长度为"+define.length);

            basePath=Base_PATH+configuration.getId()+"/"+"define"+"/";
            if (basePath.endsWith("/")) {
                basePath = basePath.substring(0, basePath.length() - 1);
            }

            String paths="";

            for (MultipartFile file : define) {
                String Path = basePath + "/" + file.getOriginalFilename();
                makeDir(Path);
                File dest1 = new File(Path);
                try {
                    file.transferTo(dest1);
                    paths=paths+Path+",";

                }catch (IOException e){

                    ResultGenerator.genFailResult("文件上传失败！");
                }
            }
            String prePath=configuration.getDefine();
            if(prePath!=null&&prePath.length()>0) {
                String[] prePaths = prePath.split(",");
                String[] newPaths = paths.split(",");
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < newPaths.length; i++) {
                    list.add(newPaths[i]);
                }
                for (int i = 0; i < prePaths.length; i++) {
                    if (!list.contains(prePaths[i])) {
                        File file = new File(prePaths[i]);
                        if (file.exists() && file.isFile()) {
                            file.delete();
                        }
                    }
                }
            }
            configuration.setDefine(paths.substring(0,paths.length()-1));
        }
        
        int i = configurationService.saveOrUpdate(configuration);
        if(i>0){
            userLogController.addLog("更新了配置方案"+name);
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("更新失败！");
        } 
    }

    @ApiOperation(value = "删除单个方案")
    @PutMapping("/delete/{id}")
    public Result delete(@PathVariable @Valid @NotBlank(message = "配置方案id不能为空") String id) {
        log.info("ConfigurationController delete [{}]", id);
       Configuration con= configurationService.getById(id);
      String header=con.getHeader();
      if(header!=null&&header.length()>0) {
          File file = new File(header);
          if (file.exists() && file.isFile()) {
              if (!file.delete()) {
                  return ResultGenerator.genFailResult("删除配置文件失败！");
              }
          }
      }
       String define=con.getDefine();
      if(define!=null&&define.length()>0) {
          String[] define1 = define.split(",");
          for (int j = 0; j < define1.length; j++) {
              File f = new File(define1[j]);
              if (f.exists() && f.isFile()) {
                  if (!f.delete()) {
                      return ResultGenerator.genFailResult("删除配置文件失败！");
                  }
              }
          }
      }
        int i = configurationService.deleteById(id);
        if(i>0){
            userLogController.addLog("删除了配置方案" + con.getName());
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败！");
        }
    }

    @ApiOperation(value = "设为默认方案")
    @PutMapping("setDefault/{id}")
    public Result setDefault(@PathVariable @Valid @NotBlank(message = "配置方案id不能为空") String id){
        Configuration pre=configurationService.getDefault();
        if(pre!=null) {
            pre.setIs_default("0");
            int j = configurationService.update(pre);
            if (j <= 0) {
                return ResultGenerator.genFailResult("添加失败");
            }
        }
        Configuration con=configurationService.getById(id);
        con.setIs_default("1");
        int i=configurationService.saveOrUpdate(con);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("设为默认方案失败！");
    }

    @ApiOperation(value = "取消默认方案")
    @PutMapping("removeDefault/{id}")
    public Result removeDefault(@PathVariable @Valid @NotBlank(message = "配置方案id不能为空") String id){
        Configuration con=configurationService.getById(id);
        con.setIs_default("0");
        int i=configurationService.saveOrUpdate(con);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("取消默认方案失败！");
    }

    @ApiOperation(value = "复制方案")
    @PutMapping("copy/{id}")
    public Result copy(@PathVariable @Valid @NotBlank(message = "配置方案id不能为空") String id){

        Configuration con=configurationService.getById(id);
        userLogController.addLog("复制配置方案"+con.getName()+"为自己的方案");
        con.setIs_common("0");
        con.setIs_default("0");
        con.setId(null);
        int i=configurationService.save(con);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("复制方案失败！");
    }
}
