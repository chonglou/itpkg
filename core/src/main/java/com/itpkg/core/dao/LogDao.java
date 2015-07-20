package com.itpkg.core.dao;

import com.itpkg.core.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by flamen on 15-7-15.
 */
@Repository("core.dao.log")
public interface LogDao extends JpaRepository<Log, Long> {
}
