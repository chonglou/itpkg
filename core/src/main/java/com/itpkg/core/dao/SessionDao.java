package com.itpkg.core.dao;

import com.itpkg.core.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by flamen on 15-7-23.
 */
@Repository("core.dao.session")
public interface SessionDao  extends JpaRepository<Session,Long> {
}
