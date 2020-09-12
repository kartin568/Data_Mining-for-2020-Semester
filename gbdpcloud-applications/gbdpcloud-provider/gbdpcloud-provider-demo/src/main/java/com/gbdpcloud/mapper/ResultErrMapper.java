package com.gbdpcloud.mapper;
import com.gbdpcloud.entity.ResultErr;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResultErrMapper extends IMapper<ResultErr> {

    List<ResultErr> getByTest(String id);
    List<ResultErr> selectByTestID(String id);
}
