package com.shawckz.ivpn.database.statement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 360 on 8/26/2015.
 */
public class SQLSelectStatement extends SQLStatement {

    private final Map<String, String> vals = new HashMap<>();
    private final Map<String, String> identifier = new HashMap<>();
    private String operator = "=";
    private String trail = "";//ex ORDER BY 'id'

    public SQLSelectStatement(String table) {
        super(table);
    }

    public SQLSelectStatement with(String column) {
        vals.put(column, "");
        return this;
    }

    public SQLSelectStatement withIdentifier(String column, String value) {
        identifier.put(column, value);
        return this;
    }

    public SQLSelectStatement setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public SQLSelectStatement setTrail(String trail) {
        this.trail = trail;
        return this;
    }

    @Override
    public String build() {
        //SELECT col1,col2,col3 FROM table WHERE column='value' AND column2='value' <trail>
        String statement = "SELECT ";

        String columns = "";//col1,col2,col3
        String values = "";//col1='val1' AND col2='val2'

        if(!vals.isEmpty()){
            for (String s : vals.keySet()) {
                columns += s + (vals.keySet().size() > 1 ? "," : "");
            }
            if(vals.keySet().size() >= 2){
                columns = columns.substring(0, columns.length() - 1);//remove trailing comma
            }
        }
        else{
            columns += "*";
        }

        if(!identifier.isEmpty() && identifier.keySet().size() > 0){
            for (String s : identifier.keySet()) {
                boolean isNumber = false;
                try {
                    Long.parseLong(identifier.get(s));
                    isNumber = true;
                } catch (NumberFormatException expected) {
                    isNumber = false;
                }
                values += s + operator + (isNumber ? identifier.get(s) : "'" + identifier.get(s) + "'") + " AND ";//col1='val1'
            }
            values = values.substring(0, values.length() - 5);//remove trailing ' AND '
        }

        statement += columns + " FROM " + getTable() + (values.equals("") ? "" : (" WHERE " + values + (trail.equals("") ? "" : " " + trail)));



        return statement;
    }
}
