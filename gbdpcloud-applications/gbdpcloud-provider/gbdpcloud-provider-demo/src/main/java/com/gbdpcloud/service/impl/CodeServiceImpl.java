package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.Code;
import com.gbdpcloud.mapper.CodeMapper;
import com.gbdpcloud.service.CodeService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


/**
 * Code service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class CodeServiceImpl extends BaseService<Code> implements CodeService {

    @Resource
    private CodeMapper codeMapper;

    @Override
    public List<Code> getByProject(String id) {

        return codeMapper.getByProject(id);
    }

    @Override
    public List<Code> getByProjectAndVersion(String id, String version) {

        return codeMapper.getByProjectAndVersion(id,version);
    }

    @Override
    public List<Code> getByCodeVersion(String id) {

        return codeMapper.getByCodeVersion(id);
    }

    @Override
    public Code getByProjectVserionAndName(@Valid String pid, @Valid String version1, @Valid String name) {
        Code code=new Code();
        code.setCode_version_ID(version1);
        code.setProject_ID(pid);
        code.setName(name);
        return  codeMapper.selectProjectVserionAndName(pid,version1,name).get(0);
        //return codeMapper.selectOne(code);
    }
}
