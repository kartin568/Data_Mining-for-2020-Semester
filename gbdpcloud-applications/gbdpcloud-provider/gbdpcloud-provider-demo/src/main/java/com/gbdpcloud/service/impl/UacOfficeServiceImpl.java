package com.gbdpcloud.service.impl;

import com.gbdpcloud.mapper.UacOfficeMapper;
import com.gbdpcloud.service.UacOfficeService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacOffice;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class UacOfficeServiceImpl extends BaseService<UacOffice>implements UacOfficeService {

    @Resource
    private UacOfficeMapper uacOfficeMapper;


    @ApiOperation(value = "新增机构")

    public int add(@Valid UacOffice uacOffice, String id) {
        String  pid = uacOffice.getParentId();
        uacOffice.setParentIds(getById(pid).getParentIds()+","+pid);
        return save(uacOffice);
    }

    @ApiOperation(value = "更新机构信息")
    public int updateInfo(@Valid UacOffice uacOffice,String id) {
        return update(uacOffice);
    }
    @ApiOperation(value = "级联删除机构")
    public int delete(String oid,String uid){
        UacOffice u = getById(oid);
        List<String> ids = new ArrayList<String>();
        getsub(oid,ids);
        ids.add(oid);
        int count = 0;
        for(String s:ids){
            count += deleteById(s);
        }
        return count ;
    }

    @ApiOperation(value = "获得全部机构")
    @Override
    public List<UacOffice> listAll() {
        List<UacOffice> list = uacOfficeMapper.list();
        sort(list);
        return list;
    }

    public List<UacOffice> getByIds(List<String> ids)
    {
        return uacOfficeMapper.getByIds(ids);
    }

    @Override
    public List<UacOffice> listLeaf() {
        List<UacOffice> list = uacOfficeMapper.getAll();
        List<UacOffice> r = new ArrayList<UacOffice>();
        for(UacOffice u :list){
            if(0 == uacOfficeMapper.getchildren(u.getId()).size()){
                r.add(u);
            }
        }
        return r;
    }

    @Override
    public int addRoot(UacOffice uacOffice) {
        //uacOfficeMapper.insert(uacOffice);
        System.out.println("root="+uacOffice.getId());
        UacOffice old = uacOfficeMapper.getRoot();
        old.setParentId(uacOffice.getId());
        old.setParentIds(old.getParentIds()+","+uacOffice.getId());
        uacOfficeMapper.updateByPrimaryKey(old);
        uacOfficeMapper.setRoot(uacOffice.getId());
        return 0;
    }


    @ApiOperation(value = "获得全部机构")
    @Override
    public Map listAsT() {
        List<UacOffice> list = uacOfficeMapper.getAll();
        Map map = BuildTree(list);
        return map;
    }

    public  Map BuildTree(List<UacOffice> l){
        String root = "0";
        Map map = new HashMap<String,List<String>>();
        build(root,map);
        return map;
    }

    private void build(String root,Map map ) {
        List<UacOffice> ch = uacOfficeMapper.getchildren(root);
        map.put(root,ch);
        for(int i = 0;i<ch.size();i++){
            build(ch.get(i).getId(),map);
        }
    }

    @ApiOperation(value = "对机构进行排序")
    public void sort(List<UacOffice> l){
        Collections.sort(l, new Comparator<UacOffice>() {
            @Override
            public int compare(UacOffice o1, UacOffice o2) {
                return o1.getSort().compareTo(o2.getSort());
            }
        });
    }

    @ApiOperation(value = "查找某节点的全部子节点")
    public void getsub(String id,List<String> ids){
        UacOffice u = new UacOffice();
        u.setParentId(id);
        List<UacOffice> list = list(u);
        if(list.size() >0){
            for(UacOffice i :list){
                ids.add(i.getId());
                getsub(i.getId(),ids);
            }
        }
    }

}
