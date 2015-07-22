package com.itpkg.core.dialects;

import org.hibernate.dialect.DerbyTenSevenDialect;

import java.sql.Types;

/**
 * Created by flamen on 15-7-22.
 */
public class DerbyDialect extends DerbyTenSevenDialect {
    public DerbyDialect() {
        super();
        registerColumnType(Types.CLOB, "clob");
    }
}
