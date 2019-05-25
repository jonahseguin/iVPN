package com.shawckz.ivpn.check;

import com.shawckz.ivpn.IVPN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Set;

public class HostnameCheck extends ICheck {

    private final String URL = "http://iphub.info/api.php?ip=%IP_ADDRESS%&showtype=3";
    private final String HOSTNAME_KEY = "Hostname";

    public HostnameCheck(IVPN instance) {
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
                String sub = line.substring(line.indexOf(HOSTNAME_KEY));
                Set<String> blackListedHosts = getInstance().getDatabaseManager().getBlacklist();
                if(!blackListedHosts.isEmpty()){
                    for(String hostname : blackListedHosts){
                        if(sub.contains(hostname)){
                            String[] keyValue = parseKeyValue(sub.substring(0, sub.indexOf(hostname) + hostname.length()));
                            if(keyValue[1].endsWith(hostname.trim())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            throw new RuntimeException("[IVPN] Exception with HostnameCheck", ex);
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

        return true;
    }

    private String[] parseKeyValue(String s) {
        String[] split = s.split(":", 2);

        return new String[] {split[0].trim(), split[1].trim()};
    }

}
