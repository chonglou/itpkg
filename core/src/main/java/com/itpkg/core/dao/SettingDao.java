package com.itpkg.core.dao;

import com.itpkg.core.models.Setting;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by flamen on 15-7-14.
 */
public interface SettingDao extends CrudRepository<Setting, Long> {
    Setting findByKey(String key);
}
