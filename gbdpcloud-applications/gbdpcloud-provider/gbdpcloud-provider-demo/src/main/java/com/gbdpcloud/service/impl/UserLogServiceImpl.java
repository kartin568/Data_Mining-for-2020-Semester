package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.UserLog;
import com.gbdpcloud.mapper.UserLogMapper;
import com.gbdpcloud.service.UserLogService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * UserLog service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class UserLogServiceImpl extends BaseService<UserLog> implements UserLogService {

    @Resource
    private UserLogMapper userLogMapper;

    public List<UserLog> selectByUsername(String username){

        UserLog userlog = new UserLog();
        userlog.setUsername(username);
        return userLogMapper.select(userlog);
    }

    public List<UserLog> selectByDateRange(String startDate, String endDate) throws ParseException{

        startDate = startDate + " 00:00:00";
        endDate = endDate + " 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date Date1 = sdf.parse(startDate);
        Date Date2 = sdf.parse(endDate);

        List<UserLog> userlogs = userLogMapper.selectAll();
        for (int i = 0; i < userlogs.size(); i++) {
            UserLog userlog = (UserLog) userlogs.get(i);
            if (userlog.getCreateDate().before(Date1) || userlog.getCreateDate().after(Date2)) {
                userlogs.remove(i);
                i--;
            }
        }

        return userlogs;
    }

    public List<UserLog> selectByOperate(String operate){

        List<UserLog> userlogs = userLogMapper.selectAll();
        for (int i = 0; i < userlogs.size(); i++){
            UserLog userlog = (UserLog)userlogs.get(i);
            if(userlog.getOperate().indexOf(operate) == -1) {
                userlogs.remove(i);
                i--;
            }
        }

        return userlogs;

    }

    public List<UserLog> selectAll(){

        List<UserLog> userLogs = userLogMapper.selectAll();
        return userLogs;
    }
}
