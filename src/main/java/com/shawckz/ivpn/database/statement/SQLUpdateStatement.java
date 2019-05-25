package com.shawckz.ivpn.database.statement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 360 on 8/26/2015.
 */
public class SQLUpdateStatement extends SQLStatement {

    private final Map<String, String> vals = new HashMap<>();
    private final Map<String, String> identifier = new HashMap<>();
    private String trail = "";//ex ORDER BY 'id'

    public SQLUpdateStatement(String table) {
        super(table);
    }

    public SQLUpdateStatement with(String column, String value) {
        vals.put(column, value);
        return this;
    }

    public SQLUpdateStatement withIdentifier(String column, String value) {
        identifier.put(column, value);
        return this;
    }

    public SQLUpdateStatement setTrail(String trail) {
        this.trail = trail;
        return this;
    }

    @Override
    public String build() {
        //INSERT INTO table (col1,col2,col3) VALUES (val1,val2,val3)
        String statement = "UPDATE " + getTable();

        String set = "";
        String where = "";

        for (String s : vals.keySet()) {
            //Set

            String v = vals.get(s);
            boolean isNumber = false;
            try {
                Long.parseLong(v);
                isNumber = true;
            } catch (NumberFormatException expected) {
                isNumber = false;
            }

            v = (isNumber ? v : "'" + v + "'");

            set += s + "=" + v + ",";
        }
        set = set.substring(0, set.length() - 1);//Remove trailing comma

        if(identifier.keySet().size() > 0) {
            for (String s : identifier.keySet()) {
                //Where

                String v = identifier.get(s);
                boolean isNumber = false;
                try {
                    Long.parseLong(v);
                    isNumber = true;
                } catch (NumberFormatException expected) {
                    isNumber = false;
                }

                v = (isNumber ? v : "'" + v + "'");

                where += s + "=" + v + " AND ";
            }
            where = where.substring(0, where.length() - 5);//Remove trailing ' AND '
        }

        statement += " SET "+set+(identifier.keySet().size() > 0
                                    ? " WHERE "+where : "");

        return statement;
    }
}
