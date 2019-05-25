package com.shawckz.ivpn.database;

import com.shawckz.ivpn.IVPN;
import com.shawckz.ivpn.database.statement.SQLDeleteStatement;
import com.shawckz.ivpn.database.statement.SQLInsertStatement;
import com.shawckz.ivpn.database.statement.SQLSelectStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseManager {

    public static final String WHITELISTED_PLAYERS = "ivpn_whitelisted";
    public static final String BLACKLISTED_HOSTS = "ivpn_blacklisted";
    public static final String CACHED_IPS = "ivpn_ipcache";

    private final IVPN instance;

    private Connection connection;
    private String ip;
    private String username;
    private String password;
    private int port;
    private String name;

    public DatabaseManager(IVPN instance) {
        this.instance = instance;
    }

    public void setup(){
        ip = instance.getConfig().getString("database.host");
        username = instance.getConfig().getString("database.username");
        password = instance.getConfig().getString("database.password");
        port = instance.getConfig().getInt("database.port");
        name = instance.getConfig().getString("database.name");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + name + "", username, password);
        }
        catch (Exception ex){
            throw new RuntimeException("Could not connect to database", ex);
        }

        if(!instance.getConfig().getBoolean("tablessetup")){
            try{
                connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS "+WHITELISTED_PLAYERS+" (uuid VARCHAR(100))");
                connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS "+BLACKLISTED_HOSTS+" (host VARCHAR(200))");
                connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS "+CACHED_IPS+" (ip VARCHAR(100),proxy BOOLEAN)");
                instance.getConfig().set("tablessetup", true);
                instance.saveConfig();
            }
            catch (SQLException ex){
                throw new RuntimeException("[iVPN] Could not create table", ex);
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isCached(String ip) throws SQLException{
        ResultSet set = connection.createStatement().executeQuery(new SQLSelectStatement(CACHED_IPS)
                .withIdentifier("ip", ip)
                .with("proxy")
                .build());
        return set.next();
    }

    public boolean isProxy(String ip) throws SQLException{
        ResultSet set = connection.createStatement().executeQuery(new SQLSelectStatement(CACHED_IPS)
                .withIdentifier("ip", ip)
                .with("proxy")
                .build());
        return set.next() && set.getBoolean("proxy");
    }

    public boolean isWhitelisted(String uuid) throws SQLException{
        ResultSet set = connection.createStatement().executeQuery(new SQLSelectStatement(WHITELISTED_PLAYERS)
                .withIdentifier("uuid", uuid)
                .with("uuid")
                .build());
        return set.next();
    }

    public boolean isBlacklisted(String host) throws SQLException{
        ResultSet set = connection.createStatement().executeQuery(new SQLSelectStatement(BLACKLISTED_HOSTS)
                .withIdentifier("host", host)
                .with("host")
                .build());
        return set.next();
    }

    public void addToWhitelist(String uuid) throws SQLException{
        connection.createStatement().executeUpdate(new SQLInsertStatement(WHITELISTED_PLAYERS)
                .with("uuid", uuid)
                .build());
    }

    public void removeFromWhitelist(String uuid) throws SQLException{
        connection.createStatement().executeUpdate(new SQLDeleteStatement(WHITELISTED_PLAYERS)
                .with("uuid", uuid)
                .build());
    }

    public void removeFromBlacklist(String host) throws SQLException{
        connection.createStatement().executeUpdate(new SQLDeleteStatement(BLACKLISTED_HOSTS)
                .with("host", host)
                .build());
    }

    public void addToBlacklist(String host) throws SQLException{
        connection.createStatement().executeUpdate(new SQLInsertStatement(BLACKLISTED_HOSTS)
                .with("host", host)
                .build());
    }

    public Set<String> getBlacklist() throws SQLException{
        Set<String> blacklist = new HashSet<>();
        ResultSet set = connection.createStatement().executeQuery(new SQLSelectStatement(BLACKLISTED_HOSTS)
                .with("host")
                .build());
        while(set.next()){
            blacklist.add(set.getString("host"));
        }
        return blacklist;
    }

}
