package com.stee.inventory.dao;

import com.stee.inventory.entity.sel.GeoZoneEntity;
import com.stee.sel.gzm.GZone;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SerryMiano on 2017/1/10.
 */
public interface GeoZoneRepository extends JpaRepository<GeoZoneEntity, String> {
    GeoZoneEntity findByName(Integer name);
}
