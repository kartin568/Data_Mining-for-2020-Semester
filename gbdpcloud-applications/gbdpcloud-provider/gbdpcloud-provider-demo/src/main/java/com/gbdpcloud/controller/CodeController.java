package com.gbdpcloud.controller;

import com.gbdpcloud.entity.Code;
import com.gbdpcloud.entity.CodeVersion;
import com.gbdpcloud.service.CodeService;
import com.gbdpcloud.service.CodeVersionService;
import com.gbdpcloud.service.ProjectService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Api(value = "CodeController")
@RequestMapping("/CodeController")
@Validated
@RestController
public class CodeController extends BaseController {

    public String BASE_PATH="D:/Project/";
    @Resource
    private CodeService codeService;

    @Resource
    private CodeVersionService codeVersionService;

    @Resource
    private UserLogController userLogController;
    
    @Resource
    private ProjectService projectService;

    private static void makeDir(String filePath) {
        if (filePath.lastIndexOf('/') > 0) {
            String dirPath = filePath.substring(0, filePath.lastIndexOf('/'));
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }


    @ApiOperation(value = "上传代码")
    @PostMapping("/add")
    public Result addCode(@RequestParam(value = "project_id") String project_id,
                          @RequestParam(value = "version") String version,
                          @RequestParam(value = "code") MultipartFile[] files){

        List<CodeVersion> list=codeVersionService.getByProject(project_id);
        int is_exist=0;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getVersion().equals(version))
            {
                is_exist=1;
                break;
            }
        }
        if(is_exist==0)
        {
            CodeVersion codeVersion=new CodeVersion();
            codeVersion.setProject_ID(project_id);
            codeVersion.setVersion(version);
            codeVersionService.save(codeVersion);
        }

        String basePath=BASE_PATH+project_id+"/"+version+"/";

        if (files == null || files.length == 0) {
            return ResultGenerator.genFailResult("上传文件不能为空");
        }

        CodeVersion model=new CodeVersion();
        model.setVersion(version);
        model.setProject_ID(project_id);
        CodeVersion codeVersion=codeVersionService.getOne(model);
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        for (MultipartFile file : files) {
            String filePath = basePath + "/" + file.getOriginalFilename();
            makeDir(filePath);
            File dest = new File(filePath);
            try {
                file.transferTo(dest);

                if(dest.isFile())
                {
                    Code code=new Code();
                    code.setProject_ID(project_id);
                    code.setCode_version_ID(codeVersion.getId());
                    code.setVersion(version);
                    code.setPath(filePath);
                    code.setName(filePath.substring(filePath.lastIndexOf("/")+1));
                    int i=codeService.save(code);
                    if(i<=0){
                        return ResultGenerator.genFailResult("文件写入数据库失败");
                    }

                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                return ResultGenerator.genFailResult("文件上传失败");
            }
        }
        userLogController.addLog("在项目"+ projectService.getById(project_id).getName() + "中上传了代码版本："+ version);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "从git上传代码")
    @PostMapping("/addfromGit")
    public Result addCodefromGit(@RequestParam(value = "project_id") String project_id,
                          @RequestParam(value = "version") String version,
                          @RequestParam(value = "path") String GitPath){

        List<CodeVersion> list=codeVersionService.getByProject(project_id);
        int is_exist=0;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getVersion().equals(version))
            {
                is_exist=1;
                break;
            }
        }
        if(is_exist==0)
        {
            CodeVersion codeVersion=new CodeVersion();
            codeVersion.setProject_ID(project_id);
            codeVersion.setVersion(version);
            codeVersionService.save(codeVersion);
        }

        String basePath=BASE_PATH+project_id+"/"+version+"/";

        try {
            Git.cloneRepository()
                    .setURI(GitPath)
                    .setDirectory(new File(basePath))
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            ResultGenerator.genFailResult("下载项目失败");
        }

        CodeVersion model=new CodeVersion();
        model.setVersion(version);
        model.setProject_ID(project_id);
        CodeVersion codeVersion=codeVersionService.getOne(model);
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        userLogController.addLog("在项目"+ projectService.getById(project_id).getName() + "中从git上传了代码版本：" + version);
        return  GitCode(basePath,project_id,version,codeVersion);
    }

    @ApiOperation(value = "浏览项目代码")
    @GetMapping("/read/all")
    public Result readProject(@RequestParam(value = "project_id")String project_id,
                              @RequestParam(value = "version")String version)
    {
        List<Code> codeList=codeService.getByProjectAndVersion(project_id,version);
        String path=BASE_PATH+project_id+"/"+version;
        return ResultGenerator.genSuccessResult(fileTree(path,codeList));
    }

    @ApiOperation(value = "取得一个代码内容")
    @GetMapping("/read/one")
    public Result readone(@RequestParam(value = "path")String path) throws IOException {
        String content=readFile(path);
        return ResultGenerator.genSuccessResult(content);
    }

    @ApiOperation(value = "修改代码")
    @PutMapping("/update/one")
    public Result updateOne(@RequestParam(value = "id")String id,
                            @RequestParam(value = "content")String content)
    {
        Code code=codeService.getById(id);
        File file =new File(code.getPath());
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("修改内容失败");
        }
        userLogController.addLog("修改了代码:" + code.getName());
        return ResultGenerator.genSuccessResult();

    }




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


    @Data
    public class FileDirTree{
        int has_child;
        int is_file;
        String path;
        String name;
        String code_id;
        List<FileDirTree> childList;

    }

    public  FileDirTree fileTree(String path,List<Code> codeList) {
        FileDirTree fileDirTree=new FileDirTree();
        List<FileDirTree> childlist=new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            fileDirTree.setName(file.getName());
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                fileDirTree.setChildList(null);
                fileDirTree.setIs_file(0);
                fileDirTree.setHas_child(0);
                fileDirTree.setName(file.getName());
                System.out.println("子目录名"+file.getName());
                return fileDirTree;
            } else {
                fileDirTree.setHas_child(1);
                fileDirTree.setIs_file(0);
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        childlist.add(fileTree(file2.getAbsolutePath(),codeList));
                    } else {
                        FileDirTree fileDirTree1=new FileDirTree();
                        fileDirTree1.setName(file2.getName());
                        fileDirTree1.setHas_child(0);
                        fileDirTree1.setIs_file(1);
                        fileDirTree1.setPath(file2.getPath());
                        System.out.println("子文件名"+file2.getName());
                        for(Code code:codeList) {
                            if(code.getPath().equals(fileDirTree1.getPath())||code.getPath().equals(fileDirTree1.getPath().replaceAll("\\\\","/"))){
                                fileDirTree1.setCode_id(code.getId());
                            }
                        }
                        childlist.add(fileDirTree1);
                    }

                }
                fileDirTree.setChildList(childlist);
            }
        } else {
            return null;
        }
        return fileDirTree;
    }

    public Result GitCode(String path,String project_id,String version,CodeVersion codeVersion) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        GitCode(file2.getAbsolutePath(),project_id,version,codeVersion);
                    } else {
                        Code code=new Code();
                        code.setProject_ID(project_id);
                        code.setCode_version_ID(codeVersion.getId());
                        code.setVersion(version);
                        code.setPath(file2.getPath());
                        code.setName(file2.getName());
                        int i=codeService.save(code);
                        if(i<=0){
                            return ResultGenerator.genFailResult("文件写入数据库失败");
                        }
                        }
                    }
                }
            }
        return ResultGenerator.genSuccessResult();
        }

}
