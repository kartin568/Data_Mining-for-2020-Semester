package com.gbdpcloud.controller;

import com.gbdpcloud.entity.CodeVersion;
import com.gbdpcloud.entity.Project;
import com.gbdpcloud.entity.ProjectMember;
import com.gbdpcloud.mapper.ProjectMapper;
import com.gbdpcloud.service.*;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Api(value = "ProjectController")
@RequestMapping("/ProjectController")
@Validated
@RestController
public class ProjectController extends BaseController {

    @Resource
    private ProjectService projectService;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private UacUserService uacUserService;

    @Resource
    private ProjectMemberService projectMemberService;

    @Resource
    private MasterController masterController;

    @Resource
    private CodeVersionService codeVersionService;
    
    @Resource
    private UserLogController userLogController;


    @ApiOperation(value = "添加项目")
    @PostMapping("/add")
    public Result save(@RequestBody @Valid Project project) {
        log.info("projectController save [{}]", project);
        project.setLeader(UacUserUtils.getUserInfoFromRequest().getLoginName());
        project.setId(UUID.randomUUID().toString());
        project.setDelFlag("0");
        int ii=projectService.save(project);
        if(ii<=0){
            return ResultGenerator.genFailResult("添加失败,项目添加失败");
        }
        String member=project.getMember();
        String []members=member.split(",");
        List<ProjectMember> projectMemberList=new ArrayList<ProjectMember>();
        for(int i=0;i<members.length;i++){
            ProjectMember projectMember=new ProjectMember();
            projectMember.setProject_ID(project.getId());
            projectMember.setMember_ID(members[i]);
            projectMemberList.add(projectMember);
        }
        int j=projectMemberService.saveBatch(projectMemberList);
        if(j<=0){
            return ResultGenerator.genFailResult("添加失败,项目成员添加失败");
        }
        ProjectMember projectMember=new ProjectMember();
        projectMember.setProject_ID(project.getId());
        projectMember.setMember_ID(UacUserUtils.getUserInfoFromRequest().getId());
        int i=projectMemberService.save(projectMember);
        if(i<=0){
            return ResultGenerator.genFailResult("添加失败,项目组长添加失败");
        }
        userLogController.addLog("添加了项目" + project.getName());
        return  ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "编辑项目")
    @PutMapping("/update")
    public Result update(@RequestBody @Valid Project project,@RequestParam(value = "user_id") String user_id) {
        log.info("projectController update [{}]", project);
        String preMember=projectService.selectById(project.getId()).getMember();
        String newMember=project.getMember();
        if(newMember==null)
        {
            newMember="";
        }
        if(preMember==null)
        {
            preMember="";
        }
        if(preMember.equals("")&&!newMember.equals(""))
        {
            String [] newMemberList=newMember.split(",");
            List<ProjectMember> list1=new ArrayList<>();
            for(int i=0;i<newMemberList.length;i++)
            {
                ProjectMember member=new ProjectMember();
                member.setMember_ID(newMemberList[i]);
                member.setProject_ID(project.getId());
                list1.add(member);
            }

            if(list1.size()!=0) {
                int j1 = projectMemberService.saveBatch(list1);
                if (j1 <= 0) {
                    return ResultGenerator.genFailResult("添加项目成员失败");
                }
            }

        }else if(newMember.equals("")&&!preMember.equals(""))
        {
            List<ProjectMember> list=projectMemberService.getByProject(project.getId());
            List<String> deleteIDs=new ArrayList<String>();
            for(int i=0;i<list.size();i++)
            {
                deleteIDs.add(list.get(i).getId());
            }
            if(deleteIDs.size()!=0) {
                int j = projectMemberService.deleteIds(deleteIDs);
                if (j <= 0) {
                    return ResultGenerator.genFailResult("删除项目成员失败");
                }
            }
        }else if(!newMember.equals(preMember))
        {
            List<ProjectMember> list=projectMemberService.getByProject(project.getId());
            String [] newMembers=newMember.split(",");
            String [] preMembers=preMember.split(",");
            List<String> newMemberList=new ArrayList<>();
            List<String> preMemberList=new ArrayList<>();
            newMemberList.addAll(Arrays.asList(newMembers));
            preMemberList.addAll(Arrays.asList(preMembers));
            List<String> deleteIDs=new ArrayList<String>();
            List<ProjectMember> projectMemberList=new ArrayList<>();

            for(int i=0;i<list.size();i++)
            {
                ProjectMember p=list.get(i);
                if(!newMember.contains(p.getMember_ID())&&!p.getMember_ID().equals(user_id)) {
                    deleteIDs.add(p.getId());
                }
            }
            if(deleteIDs.size()!=0) {
                int j = projectMemberService.deleteIds(deleteIDs);
                if (j <= 0) {
                    return ResultGenerator.genFailResult("删除项目成员失败");
                }
            }

            for(int i=0;i<newMemberList.size();i++)
            {
                if(!preMemberList.contains(newMemberList.get(i)))
                {
                    ProjectMember p=new ProjectMember();
                    p.setProject_ID(project.getId());
                    p.setMember_ID(newMemberList.get(i));
                    projectMemberList.add(p);
                }
            }
            if(projectMemberList.size()!=0) {
            int j1 = projectMemberService.saveBatch(projectMemberList);
            if (j1 <= 0) {
                return ResultGenerator.genFailResult("添加项目成员失败");
            }
        }
        }

        int i = projectService.update(project);
        if(i>0){
            userLogController.addLog("更新了项目"+ project.getName() +"的信息");
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("更新项目信息失败！");
    }

    @ApiOperation(value = "获得型号平台和编译器")
    @GetMapping("/getOther")
    public Result getType(){
        ArrayList<Project> list = (ArrayList<Project>) projectMapper.getAll(); //(ArrayList<Project>) projectService.list(null);
        ArrayList<String> type = new ArrayList<>();
        ArrayList<String> platform = new ArrayList<>();
        ArrayList<String> compiler = new ArrayList<>();
        Map<String ,ArrayList<String>> map = new HashMap<String ,ArrayList<String>>();
        map.put("type",type);
        map.put("platform",platform);
        map.put("compiler",compiler);
        for(Project p:list){
            if(!type.contains(p.getType())){
                type.add(p.getType());
            }
            if(!platform.contains(p.getPlatform())){
                platform.add(p.getPlatform());
            }
            if(!compiler.contains(p.getCompiler())){
                compiler.add(p.getCompiler());
            }
        }
        return ResultGenerator.genSuccessResult(map);
    }


    @ApiOperation(value = "删除项目")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable @Valid @NotBlank(message = "项目标识不能为空") String id) {
        log.info("projectController delete [{}]", id);
        Project project = projectService.selectById(id);
        int i = projectService.deleteById(id);
        if(i>0){
            userLogController.addLog("删除了项目"+ project.getName());
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("删除失败！");
    }



    @ApiOperation(value = "按条件查找项目")
    @GetMapping("/project/find")
    public Result findProject(@RequestParam(required = false,value = "time")String time,
                              @RequestParam(required = false,value = "type")String type,
                              @RequestParam(required = false,value = "platform")String platform,
                              @RequestParam(required = false,value = "compiler")String compiler,
                              @RequestParam(required = false,value = "name")String name)
    {
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

        Date nowDate=new Date();
        List<Project> deletes=new ArrayList<>();
        for(int i=0;i<list1.size();i++) {
            Project p=list1.get(i);
            System.out.println(p.getName());
            if (time != null && time.length() > 0)
            {
                int days = Integer.parseInt(time);
                if ((nowDate.getTime() - p.getCreateDate().getTime()) / (1000 * 60 * 60 * 24) > days * 30) {
                    deletes.add(p);
                }
            }
            if(type != null && type.length() > 0&&!p.getType().equals(type))
            {
                deletes.add(p);
            }
            if(platform != null && platform.length() > 0&&!p.getPlatform().equals(platform))
            {
                deletes.add(p);
            }
            if(compiler != null && compiler.length() > 0&&!p.getCompiler().equals(compiler))
            {
                deletes.add(p);
            }
            if(name != null && name.length() > 0&&!p.getName().equals(name))
            {
                deletes.add(p);
            }
        }

        list1.removeAll(deletes);

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

        for(int i=0;i<list1.size();i++)
        {
            Project p=list1.get(i);
            if(p.getCreateDate().getTime()-user.getLastLoginTime().getTime()>0)
            {
                p.setIs_new("1");
            }else
                p.setIs_new("0");
        }

        list1.sort(Comparator.comparing(Project::getCreateDate,Comparator.reverseOrder()));

        return ResultGenerator.genSuccessResult(list1);

}
}
