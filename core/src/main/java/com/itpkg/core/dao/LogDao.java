package com.itpkg.core.dao;

import com.itpkg.core.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by flamen on 15-7-15.
 */

public interface LogDao extends JpaRepository<Log, Long> {
}
