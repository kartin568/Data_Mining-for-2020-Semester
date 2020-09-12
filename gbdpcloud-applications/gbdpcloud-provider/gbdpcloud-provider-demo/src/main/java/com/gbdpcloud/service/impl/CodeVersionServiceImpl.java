package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.CodeVersion;
import com.gbdpcloud.mapper.CodeVersionMapper;
import com.gbdpcloud.service.CodeVersionService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CodeVersionServiceImpl extends BaseService<CodeVersion> implements CodeVersionService {

    @Resource
    private CodeVersionMapper codeVersionMapper;

    @Override
    public List<CodeVersion> getByProject(String id) {

        return codeVersionMapper.selectByProject(id);
    }
}
