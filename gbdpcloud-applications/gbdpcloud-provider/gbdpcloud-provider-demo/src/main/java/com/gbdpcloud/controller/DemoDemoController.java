package com.gbdpcloud.controller;

import com.gbdpcloud.entity.DemoDemo;
import com.gbdpcloud.service.DemoDemoService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.page.PageRequest;
import gbdpcloudcommonbase.gbdpcloudcommonbase.page.PageResult;
import gbdpcloudcommonbase.gbdpcloudcommonbase.validation.CreateGroup;
import gbdpcloudcommonbase.gbdpcloudcommonbase.validation.UpdateGroup;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * DemoDemo 控制器
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-11
 */
/** swagger注解根据需要自行添加 */
@Api(value = "DemoDemoController", description = "demo接口")
@RestController
@RequestMapping("/demo/demo")
public class DemoDemoController extends BaseController {

    @Autowired
    private DemoDemoService demoDemoService;

    @ApiOperation(value = "新增")
    /*** 接口形式为restful*/
    @PostMapping
    /** Validated-输入参数验证，CreateGroup-新建情况下 UpdateGroup-更新情况下，在对应bean的需要验证字段上区分action
     *  示例如下：
     *    @NotBlank(
     *         message = "Id不能为空",
     *         groups = {UpdateGroup.class}
     *     )
     *     protected String id;
     * ========================================================
     *  需要权限校验的接口添加如下注解，
     * 注解的参数为uac_menu表中定义的permission字段，分为edit和view两类权限
     * 权限字段规范为：微服务名：接口名：操作类型 -> demo:demo:edit / uac:user:view
     * */
    //@PreAuthorize("@pms.hasPermission('demo:demo:edit')")
    public Result save(@RequestBody @Validated(CreateGroup.class) DemoDemo demoDemo) {

        log.info("DemoDemoController save [{}]", demoDemo);
        // TODO 强制！这里调用BaseService已经实现的方法，
        //  如果基础方法无法满足，需要自己实现，
        //  那么在对应的serviceImpl中不能覆盖父类方法，需要新定义方法实现
        int i = demoDemoService.saveOrUpdate(demoDemo);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("更新/插入失败！");
    }

    @ApiOperation(value = "更新")
    @PutMapping
    public Result update(@RequestBody @Validated(UpdateGroup.class) DemoDemo demoDemo) {
        log.info("DemoDemoController update [{}]", demoDemo);
        int i = demoDemoService.saveOrUpdate(demoDemo);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("更新/插入失败！");
    }

    @ApiOperation(value = "删除单个")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable @NotBlank(message = "demoId不能为空") String id) {
        log.info("DemoDemoController delete [{}]", id);
        int i = demoDemoService.deleteById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("删除失败！");
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping
    public Result deleteByIds(@RequestParam(name = "ids") @NotBlank(message = "demoIds不能为空") String ids) {
        log.info("DemoDemoController deleteMany [{}]", ids);
        int i = demoDemoService.deleteByIds(ids);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("删除失败！");
    }

    @ApiOperation(value = "查看单个记录")
    @GetMapping("/{id}")
    public Result get(@PathVariable @NotBlank(message = "demoId不能为空") String id) {
        demoDemoService.querydemo("1");
        log.info("DemoDemoController get [{}]", id);
       // DemoDemo demoDemo = demoDemoService.getById(id);
        return  ResultGenerator.genSuccessResult("");
    }

    @ApiOperation(value = "分页查看")
    @GetMapping
    /** 分页接口需要传两部分参数，分页参数和对应的筛选参数，如下所示*/
    public Result list(PageRequest pageRequest, DemoDemo demoDemo) {
        log.info("DemoDemoController list [{}]", demoDemo);
        //demoDemo.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "uo", "uu"));
        PageResult<DemoDemo> page = demoDemoService.page(pageRequest, demoDemo);
        return  ResultGenerator.genSuccessResult(page);
    }

    @ApiOperation(value = "查看所有")
    @GetMapping("/all")
    public Result listAll(DemoDemo demoDemo) {
        log.info("DemoDemoController listAll [{}]", demoDemo);
        List<DemoDemo> list = demoDemoService.list(demoDemo);
        return ResultGenerator.genSuccessResult(list);
    }
}
