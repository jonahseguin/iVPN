package com.shawckz.ivpn.check;

import com.shawckz.ivpn.IVPN;

import java.sql.SQLException;

public class WhitelistPreCheck extends PreICheck {

    public WhitelistPreCheck(IVPN instance) {
        super(instance);
    }

    @Override
    public boolean check(String uuid, String ip) {
        try{
            if(getInstance().getDatabaseManager().isWhitelisted(uuid)){
                return true;
            }
        }
        catch (SQLException ex){
            throw new RuntimeException("Could not check if whitelisted", ex);
        }
        return false;
    }
}
