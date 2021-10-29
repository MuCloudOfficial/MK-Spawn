package me.markchanel.plugin.EX.AdvancedWarps;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static final String Prefix = "§e[§bEX-AdvancedWarps§e] ";
    public static Economy eco = null;
    private final Config config = new Config(this);
    private Commands commands = new Commands(this);

    @Override
    public void onEnable() {
        checkEssentials();
        checkVault();
        config.initConfig();
        getCommand("AdvancedWarps").setExecutor(commands);
        getCommand("warps").setExecutor(commands);
        getServer().getPluginManager().registerEvents(new Listeners(),this);
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.YELLOW + "插件已加载完毕.");

    }

    public void onDisable(){
        config.saveConfig();
        HandlerList.unregisterAll((Listener) this);
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.YELLOW + "插件已卸载.");
    }

    public void onReload(){
        config.saveConfig();
        HandlerList.unregisterAll((Listener) this);
        config.initConfig();
        getServer().getPluginManager().registerEvents(new Listeners(),this);
    }

    public void checkEssentials(){
        if(!getServer().getPluginManager().isPluginEnabled("Essentials")){
            super.getServer().getPluginManager().disablePlugin(this);
            throw new RuntimeException(Prefix + ChatColor.RED + "Essentials Not found! Plugin Disabling.");
        }
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.GRAY + "已侦测到 Essentials.");
    }

    public void checkVault(){
        if(!getServer().getPluginManager().isPluginEnabled("Vault")){
            super.getServer().getPluginManager().disablePlugin(this);
            throw new RuntimeException(Prefix + ChatColor.RED + "Vault Not found! Plugin Disabling.");
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
            super.getServer().getPluginManager().disablePlugin(this);
            throw new RuntimeException(Prefix + ChatColor.RED + "Economy Module Not found! Plugin Disabling.");
        }
        eco = rsp.getProvider();
    }

}
