package com.itpkg.core.dao;

import com.itpkg.core.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by flamen on 15-7-15.
 */
public interface RoleDao extends JpaRepository<Role, Long> {
    @Query(value = "select r from Role r where r.user.id=:userId and r.name=:name and r.rType=:rType and r.rId=:rId")
    Optional<Role> find(@Param("userId") long userId,
                        @Param("name") String name,
                        @Param("rType") String rType,
                        @Param("rId") Integer rId);

    @Query(value = "delete from Role r where r.user.id=:userId and r.name=:name and r.rType=:rType and r.rId=:rId")
    void delete(@Param("userId") long userId,
                @Param("name") String name,
                @Param("rType") String rType,
                @Param("rId") Integer rId);

    @Query(value = "delete from Role r where r.user.id=:userId and r.name=:name and r.rType=:rType")
    void delete(@Param("userId") long userId,
                @Param("name") String name,
                @Param("rType") String rType);

    @Query(value = "delete from Role r where r.user.id=:userId and r.name=:name")
    void delete(@Param("userId") long userId,
                @Param("name") String name);

    @Query(value = "delete from Role r where r.user.id=:userId")
    void delete(@Param("userId") long userId);

}
