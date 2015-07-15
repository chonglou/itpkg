package com.itpkg.core.dao;

import com.itpkg.core.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by flamen on 15-7-14.
 */
//public interface SettingDao extends CrudRepository<Setting, Long> {
public interface SettingDao extends JpaRepository<Setting, Long> {
    Setting findByKey(String key);
}
