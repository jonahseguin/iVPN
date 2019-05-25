package com.shawckz.ivpn.check;

import com.shawckz.ivpn.IVPN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ScoreVPNCheck extends ICheck {

    private final String URL = "http://check.getipaddr.net/check.php?ip=%IP_ADDRESS%";
    private final double PROXY_VALUE_LIMIT = 1.0;

    public ScoreVPNCheck(IVPN instance) {
        super(instance);
    }

    @Override
    public boolean check(String uuid, String ip) {
        InputStream is = null;
        BufferedReader br = null;

        try{
            is = new URL(this.URL.replaceAll("%IP_ADDRESS%", ip)).openStream();
            br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();

            if(Double.parseDouble(line) > PROXY_VALUE_LIMIT){
                return false;
            }
            return true;
        }
        catch (Exception ex){
            throw new RuntimeException("[IVPN] Exception with ScoreVPNCheck", ex);
        }
        finally {
            try{
                if(is != null){
                    is.close();
                }
                if(br != null){
                    br.close();
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
