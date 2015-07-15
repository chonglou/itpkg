package com.itpkg.core;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Created by flamen on 15-7-15.
 */
public class NamingStrategy extends ImprovedNamingStrategy {

    @Override
    public String classToTableName(String className) {
        return this.getClass().getPackage().getName().split(".")[0]+"_"+className.toLowerCase();
    }

    @Override
    public String propertyToColumnName(String propertyName) {
        return "_"+propertyName;
    }

    @Override
    public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
        return propertyName+"_id";
        //return super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName);
    }
}
