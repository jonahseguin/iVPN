package com.shawckz.ivpn.database.statement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 360 on 8/26/2015.
 */
public class SQLDeleteStatement extends SQLStatement {

    private final Map<String, String> vals = new HashMap<>();
    private String operator = "=";
    private String trail = "";//ex ORDER BY 'id'

    public SQLDeleteStatement(String table) {
        super(table);
    }

    public SQLDeleteStatement with(String key, String value) {
        vals.put(key, value);
        return this;
    }

    public SQLDeleteStatement setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public SQLDeleteStatement setTrail(String trail) {
        this.trail = trail;
        return this;
    }

    @Override
    public String build() {
        //SELECT col1,col2,col3 FROM table WHERE column='value' AND column2='value' <trail>
        String statement = "DELETE ";

        String values = "";//col1='val1' AND col2='val2'

        if(!vals.isEmpty()){
            for(String s : vals.keySet()){
                String val = vals.get(s);
                try{
                    val = Long.parseLong(val)+"";
                }
                catch (NumberFormatException expected){
                    val = "'"+val+"'";
                }
                values += s+operator+val+" AND ";
            }
            if(values.length() >= 5){
                values = values.substring(0, values.length()-5);
            }
        }

        statement += " FROM " + getTable() + (values.equals("") ? "" : (" WHERE " + values + (trail.equals("") ? "" : " " + trail)));

        return statement;
    }
}
