package com.stee.inventory.dao;

import com.stee.sel.inventory.DeviceInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DeviceDao extends JpaRepository<DeviceInfoEntity,String>,JpaSpecificationExecutor<DeviceInfoEntity>{
        List<DeviceInfoEntity> findByGeozoneId(Integer geoZoneId);
        DeviceInfoEntity findByDeviceId(String id);
        DeviceInfoEntity findByName(String name);
}
