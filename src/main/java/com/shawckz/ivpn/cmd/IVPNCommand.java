package com.shawckz.ivpn.cmd;

import com.shawckz.ivpn.IVPN;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class IVPNCommand extends Command {

    private final IVPN instance;

    public IVPNCommand(IVPN instance){
        super("ivpn", "ivpn.use", "vpn", "antivpn");
        this.instance = instance;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if(args.length >= 3){
            sender.sendMessage(new TextComponent(ChatColor.GRAY+"Querying database asynchronously..."));
            instance.getProxy().getScheduler().runAsync(instance, new Runnable() {
                @Override
                public void run() {
                    try {
                        if (args[0].toLowerCase().startsWith("w")) {
                            String key = args[2];
                            ProxiedPlayer player = instance.getProxy().getPlayer(key);
                            if (player != null) {
                                if (args[1].equalsIgnoreCase("add")) {
                                    if (!instance.getDatabaseManager().isWhitelisted(player.getUniqueId().toString())) {
                                        instance.getDatabaseManager().addToWhitelist(player.getUniqueId().toString());
                                        sender.sendMessage(new TextComponent(ChatColor.GREEN + "[iVPN] Added '" + player.getName() + "' to the whitelist."));
                                    } else {
                                        sender.sendMessage(new TextComponent(ChatColor.RED + "Player '" + player.getName() + "' is already whitelisted."));
                                    }
                                } else {
                                    if (instance.getDatabaseManager().isWhitelisted(player.getUniqueId().toString())) {
                                        instance.getDatabaseManager().removeFromWhitelist(player.getUniqueId().toString());
                                        sender.sendMessage(new TextComponent(ChatColor.GREEN + "[iVPN] Removed '" + player.getName() + "' from the whitelist."));
                                    } else {
                                        sender.sendMessage(new TextComponent(ChatColor.RED + "Player '" + player.getName() + "' is not whitelisted."));
                                    }
                                }
                            } else {
                                sender.sendMessage(new TextComponent(ChatColor.RED + "Player '" + key + "' is not online."));
                            }
                        } else if (args[0].toLowerCase().startsWith("h")) {
                            String host = args[2];

                            if (args[1].equalsIgnoreCase("add")) {
                                if (!instance.getDatabaseManager().isBlacklisted(host)) {
                                    instance.getDatabaseManager().addToBlacklist(host);
                                    sender.sendMessage(new TextComponent(ChatColor.GREEN + "[iVPN] Added '" + host + "' to the host blacklist."));
                                } else {
                                    sender.sendMessage(new TextComponent(ChatColor.RED + "Host '" + host + "' is already blacklisted."));
                                }
                            } else {
                                if (instance.getDatabaseManager().isBlacklisted(host)) {
                                    instance.getDatabaseManager().removeFromWhitelist(host);
                                    sender.sendMessage(new TextComponent(ChatColor.GREEN + "[iVPN] Removed '" + host + "' from the host blacklist."));
                                } else {
                                    sender.sendMessage(new TextComponent(ChatColor.RED + "Host '" + host + "' is not blacklisted."));
                                }
                            }
                        } else {
                            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /ivpn <(w)hitelistuser|(h)ostblacklist> <add|remove> <value>"));
                        }
                    }
                    catch (Exception ex){
                        sender.sendMessage(new TextComponent(ChatColor.RED+"An error occured, check the console."));
                        throw new RuntimeException("Error with iVPN command", ex);
                    }
                }
            });
        }
        else{
            sender.sendMessage(new TextComponent(ChatColor.RED+"Usage: /ivpn <(w)hitelistuser|(h)ostblacklist> <add|remove> <value>"));
        }
    }
}
