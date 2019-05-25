package com.shawckz.ivpn;

import com.google.common.io.ByteStreams;
import com.shawckz.ivpn.check.*;
import com.shawckz.ivpn.cmd.IVPNCommand;
import com.shawckz.ivpn.database.DatabaseManager;
import com.shawckz.ivpn.listener.JoinListener;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class IVPN extends Plugin {

    @Getter
    private Configuration config;

    @Getter
    private final Set<ICheck> vpnChecks = new HashSet<>();

    @Getter
    private final Set<PreICheck> preVpnChecks = new HashSet<>();

    @Getter
    private DatabaseManager databaseManager;

    @Override
    public void onEnable(){
        initConfig();

        databaseManager = new DatabaseManager(this);
        databaseManager.setup();

        registerPreVPNCheck(new WhitelistPreCheck(this));

        registerVPNCheck(new HostnameCheck(this));
        registerVPNCheck(new VPNCheck(this));
        registerVPNCheck(new ScoreVPNCheck(this));

        getProxy().getPluginManager().registerListener(this, new JoinListener(this));
        getProxy().getPluginManager().registerCommand(this, new IVPNCommand(this));
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }

    private void registerVPNCheck(ICheck check){
        vpnChecks.add(check);
    }

    private void registerPreVPNCheck(PreICheck check){
        preVpnChecks.add(check);
    }

    private void initConfig(){
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
        try {
            config = YamlConfiguration.getProvider(YamlConfiguration.class).load(configFile);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
