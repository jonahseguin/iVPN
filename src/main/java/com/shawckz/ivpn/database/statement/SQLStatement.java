package com.shawckz.ivpn.database.statement;

/**
 * Created by 360 on 8/26/2015.
 */
public abstract class SQLStatement {

    private String table;

    public SQLStatement(String table) {
        this.table = table;
    }

    public SQLStatement setTable(String table) {
        this.table = table;
        return this;
    }

    public String getTable() {
        return table;
    }

    public abstract String build();

}
