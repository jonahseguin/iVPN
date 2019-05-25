package com.shawckz.ivpn.check;

import com.shawckz.ivpn.IVPN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class VPNCheck extends ICheck {

    private final String URL = "http://iphub.info/api.php?ip=%IP_ADDRESS%&showtype=3";
    private final String PROXY_KEY = "Proxy";
    private final String PROXY_VALUE = "0";

    public VPNCheck(IVPN instance) {
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

            if(line.contains(":")){
                String sub = line.substring(line.indexOf(PROXY_KEY));
                if(sub.contains(PROXY_VALUE)){
                    String[] keyValue = parseKeyValue(sub.substring(0, sub.indexOf(PROXY_VALUE) + PROXY_VALUE.length()));

                    if(!keyValue[1].equals(PROXY_VALUE.trim())) {
                        return true;
                    }
                }
            }
            return false;
        }
        catch (Exception ex){
            throw new RuntimeException("[IVPN] Exception with VPNCheck", ex);
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

    private String[] parseKeyValue(String s) {
        String[] split = s.split(":", 2);

        return new String[] {split[0].trim(), split[1].trim()};
    }

}
