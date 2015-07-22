package com.itpkg.core.dao;

import com.itpkg.core.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by flamen on 15-7-14.
 */
//public interface SettingDao extends CrudRepository<Setting, Long> {
@Repository("core.dao.setting")
public interface SettingDao extends JpaRepository<Setting, Long> {
    Setting findByKey(String key);
}
