package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.Result;
import com.gbdpcloud.service.ResultService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Result service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class ResultServiceImpl extends BaseService<Result> implements ResultService {


}
