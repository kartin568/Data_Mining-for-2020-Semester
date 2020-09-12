package com.gbdpcloud.service.impl;


import com.gbdpcloud.mapper.*;
import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacOffice;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacRoleUser;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUserOffice;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.RoleUserService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.UserOfficeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * User service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class UacUserServiceImpl extends BaseService<UacUser> implements UacUserService {

    @Resource
    private UacUserMapper uacUserMapper;
    @Resource
    private UacUserOfficeMapper uacUserOfficeMapper;
    @Resource
    private UacOfficeMapper uacOfficeMapper;
    @Resource
    private UacRoleMapper uacRoleMapper;
    @Resource
    private UacRoleUserMapper uacRoleUserMapper;
    @Resource
    private UacRoleUserServiceImpl uacRoleUserService;
    @Resource
    private UacUserOfficeServiceImpl uacUserOfficeService;
    @Resource
    private UserOfficeService userOfficeService;
    @Resource
    private RoleUserService roleUserService;


    @ApiOperation(value = "获得单个用户的全部信息")
    public UacUser getInfoById(String id){
        UacUser t = new UacUser();
        t.setId(id);
        UacUser u =  uacUserMapper.selectByPrimaryKey(t);
        getALlInfo(u);
        return u;
    }
    @ApiOperation(value = "获得用户的role和office")
    public  UacUser getALlInfo(UacUser u){
        UacRoleUser uru = new UacRoleUser();
        uru.setUserId(u.getId());
        List<UacRoleUser> tt = uacRoleUserMapper.getByUser(u.getId());
        if(tt.size()>0){
            u.setUacRole( uacRoleMapper.get( tt.get(0).getRoleId() )  );
        }
        //String roleName = uacRoleMapper.get( tt.getRoleId() ).getName();
        //u.setRoleName(roleName);

        UacUserOffice t = new UacUserOffice();
        t.setUserId(u.getId());
        List<UacUserOffice> off = uacUserOfficeService.getByUserId(u.getId());
        if( off.size() >0){
            //u.setOfficeName(uacOfficeMapper.get( off.getOffice_id() ).getName());
            u.setUacOffice( uacOfficeMapper.get( off.get(0).getOfficeId() ) );
        }
        return u;
    }


    @ApiOperation(value = "通过用户名获取用户")
    @Override
    public List<UacUser> getByName(String username) {
        List<UacUser> uacUser = uacUserMapper.selectByName(username);
        for(UacUser u:uacUser){
            getALlInfo(u);
        }
        return uacUser;
    }


    @ApiOperation(value = "更新用户信息")
    @Override
    public int updateInfo(UacUser uacUser,String id) {
        updateOfficeARole(uacUser);
        return saveOrUpdate(uacUser);
    }



    @ApiOperation(value = "添加用户")
    public int addU(UacUser uacUser,String id) {
        // uacUser.setCreate_by(id);
        //uacUser.setCreateDate(new Date());
        //uacUser.setUacOffice(getOff(office));
        //addUserInfo(uacUser);
        updateOfficeARole(uacUser);
        return save(uacUser);

    }

    public void updateOfficeARole(UacUser uacUser){
        boolean f = false;
        if(null != uacUser.getUacOffice()){
            UacUserOffice t = new UacUserOffice();
            t.setUserId(uacUser.getId());
            t.setOfficeId(uacUser.getUacOffice().getId());
            if(f){
                userOfficeService.update(t);
            }else{
                List<UacUserOffice>  olist =  uacUserOfficeService.getByUserId(uacUser.getId());
                for(UacUserOffice o:olist){
                    uacUserOfficeService.del(o.getId());
                }
                uacUserOfficeService.saveOrUpdate(t);
            }


        }
        if(null != uacUser.getUacRole()){
            UacRoleUser t = new UacRoleUser();
            t.setUserId(uacUser.getId());
            t.setRoleId(uacUser.getUacRole().getId());
            if(f){
                f = true;
               // roleUserService.update(t);
            }else{
                List<UacRoleUser> rlist = uacRoleUserMapper.getByUser(uacUser.getId());
                for(UacRoleUser r:rlist){
                    uacRoleUserService.del(r.getId());
                }
                uacRoleUserService.save(t);
            }
        }
    }

    public void addUserInfo(UacUser u){
        u.setPhone("13900000000");
        u.setEmail("1234567890@qq.com");
        if(null == u.getPassword()){
            u.setPassword("1234");
        }
    }

    @ApiOperation(value = "批量添加用户")
    public int addList(List<UacUser> list,String id){

        //System.err.println(list.get(0).getId());
        List<UacUser> r = new ArrayList<>();
        int i = 0;
         for(UacUser u :list){
             if(null == u.getPassword() ){
                 u.setPassword("1234");
             }
             if(null == u.getName()){
                 u.setName(u.getLoginName());
             }
             if(u.getLoginName() == null){
                 i = 1;
             }
        }
        int j = saveBatch(list);
        for(UacUser u :list){
            updateOfficeARole(u);
        }

        return j;
    }

    public UacOffice getOff(String name){
        return uacOfficeMapper.selectByname(name).get(0);
    }


    @ApiOperation(value = "获得全部用户列表")
    @Override
    public List<UacUser> getAll() {
        List<UacUser> list = uacUserMapper.selectAll();
        for(UacUser u:list){
            getALlInfo(u);
        }
        return list;
    }


}
