package com.shawckz.ivpn.database.statement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 360 on 8/26/2015.
 */
public class SQLInsertStatement extends SQLStatement {

    private final Map<String, String> vals = new HashMap<>();
    private String trail = "";//ex ORDER BY 'id'

    public SQLInsertStatement(String table) {
        super(table);
    }

    public SQLInsertStatement with(String column, String value) {
        vals.put(column, value);
        return this;
    }

    public SQLInsertStatement setTrail(String trail) {
        this.trail = trail;
        return this;
    }

    @Override
    public String build() {

        //INSERT INTO table (col1,col2,col3) VALUES (val1,val2,val3)
        String statement = "INSERT INTO "+getTable();
        String columns = "(";
        String vals = "VALUES (";
        for(String column : this.vals.keySet()){
            //The columns
            columns += column+",";
            //The column values

            String v = this.vals.get(column);

            boolean isNumber = false;

            try{
                Long.parseLong(v);
                isNumber = true;
            }
            catch (NumberFormatException expected){
                isNumber = false;
            }

            if(isNumber){
                vals += this.vals.get(column)+",";
            }
            else{
                vals += "'"+this.vals.get(column)+"',";
            }
        }
        columns = columns.substring(0, columns.length() - 1);//Remove trailing comma
        columns += ")";

        vals = vals.substring(0, vals.length() - 1);//Remove trailing "',"
        vals += ")";

        statement += " " + columns + " " + vals;

        return statement;
    }
}
