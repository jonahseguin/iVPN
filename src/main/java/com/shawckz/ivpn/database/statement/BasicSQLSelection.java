package com.shawckz.ivpn.database.statement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 360 on 8/26/2015.
 */
public class BasicSQLSelection {

    public BasicSQLSelection() {
    }

    private final Map<String,String> values = new HashMap<>();
    private final Map<String,String> identifier = new HashMap<>();

    public BasicSQLSelection with(String column, String value){
        values.put(column, value);
        return this;
    }

    public BasicSQLSelection withIdentifier(String column, String value){
        identifier.put(column, value);
        return this;
    }

    public Map<String,String> getValues(){
        return values;
    }

    public Map<String, String> getIdentifiers() {
        return identifier;
    }
}
