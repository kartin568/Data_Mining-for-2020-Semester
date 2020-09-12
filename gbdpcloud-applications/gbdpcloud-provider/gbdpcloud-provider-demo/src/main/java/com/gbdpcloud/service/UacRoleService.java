package com.gbdpcloud.service;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacRole;

import java.util.List;

public interface UacRoleService extends Service<UacRole> {
    List<UacRole> listAll();
}
