package com.gbdpcloud.service.impl;
import com.gbdpcloud.entity.ResultErr;
import com.gbdpcloud.mapper.ResultErrMapper;
import com.gbdpcloud.service.ResultErrService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
//@Transactional(readOnly = true)
public class ResultErrServiceImpl extends BaseService<ResultErr> implements ResultErrService{

    @Resource
    private ResultErrMapper resultErrMapper;
    @Override
    public List<ResultErr> getByTest(String id) {

        return resultErrMapper.selectByTestID(id);
    }

    @Override
    public void sort(int order, int sortBy, List<ResultErr> list) {
        list.sort(Comparator.comparing(ResultErr::getRule_type));//.thenComparing(ResultErr::getLine));
        if(0 != order){
            Collections.reverse(list);
        }
    }

    @ApiOperation(value = "对机构进行排序 从小到大")
    public void sort(List<ResultErr> l){
        Collections.sort(l, new Comparator<ResultErr>() {
            @Override
            public int compare(ResultErr o1, ResultErr o2) {
                return o1.getRule_type().compareTo(o2.getRule_type());
            }
        });
    }
}
