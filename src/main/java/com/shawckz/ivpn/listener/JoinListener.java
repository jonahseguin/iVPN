package com.shawckz.ivpn.listener;

import com.shawckz.ivpn.check.PreICheck;
import com.shawckz.ivpn.check.ICheck;
import com.shawckz.ivpn.IVPN;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;

public class JoinListener implements Listener {

    private final IVPN instance;
    private final String kickMessage;

    public JoinListener(IVPN instance) {
        this.instance = instance;
        this.kickMessage = ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("kick-message"));
    }

    @EventHandler
    public void onConnected(final ServerConnectedEvent e){
        final String ip = e.getPlayer().getAddress().toString().replace("/", "").split(":")[0];
        final String uuid = e.getPlayer().getUniqueId().toString();
        final String name = e.getPlayer().getName();

        instance.getProxy().getScheduler().runAsync(instance, new Runnable() {
            @Override
            public void run() {
                //PreVPNChecks: If they return true, they are able to pre-bypass other checks
                for(PreICheck check : instance.getPreVpnChecks()){
                    if(check.check(uuid, ip)){
                        return;
                    }
                }
                try{
                    if(!instance.getDatabaseManager().isProxy(ip)){
                        return;
                    }
                }
                catch (SQLException ex){
                    throw new RuntimeException("Exception with SQL query (ip cache ServerConnectedEvent)", ex);
                }
                //VPNChecks: If they return false, they failed a check and are not allowed to login.
                for(ICheck check : instance.getVpnChecks()){
                    if(!check.check(uuid, ip)){
                        instance.getLogger().info("[IVPN] " + name + "'s IP failed: " + ip);
                        e.getPlayer().disconnect(new TextComponent(kickMessage));
                        break;
                    }
                }
            }
        });
    }

  /*  @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final LoginEvent e){
        final String ip = e.getConnection().getAddress().toString().replace("/", "").split(":")[0];
        final String uuid = e.getConnection().getUniqueId().toString();
        final String name = e.getConnection().getName();

        instance.getProxy().getScheduler().runAsync(instance, new Runnable() {
            @Override
            public void run() {
                //PreVPNChecks: If they return true, they are able to pre-bypass other checks
                for(PreICheck check : instance.getPreVpnChecks()){
                    if(check.check(uuid, ip)){
                        return;
                    }
                }
                //VPNChecks: If they return false, they failed a check and are not allowed to login.
                for(ICheck check : instance.getVpnChecks()){
                    if(!check.check(uuid, ip)){
                        instance.getLogger().info("[IVPN] "+name+"'s IP failed: "+ip);
                        e.setCancelReason(kickMessage);
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        });

    }*/

}
