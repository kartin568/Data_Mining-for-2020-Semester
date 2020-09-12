package com.gbdpcloud.service;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacRoleUser;

import java.util.List;

public interface UacRoleUserService extends Service<UacRoleUser> {
   UacRoleUser getByRidAuid(String rid,String uid);
   List<UacRoleUser> getByUserid(String id);

   int del(String id);
}
