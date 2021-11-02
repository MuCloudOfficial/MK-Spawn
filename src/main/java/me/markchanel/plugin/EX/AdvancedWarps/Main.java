package me.markchanel.plugin.EX.AdvancedWarps;

import com.earth2me.essentials.Essentials;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin implements Listener {

    public static final String Prefix = "§7§l[§c§lEX§7§l-§6§lAdvancedWarps§7§l] ";
    private final Config config = new Config(this);
    private final Commands commands = new Commands(this);

    @Override
    public void onEnable() {
        config.initConfig();
        Objects.requireNonNull(getCommand("AdvancedWarps")).setExecutor(commands);
        Objects.requireNonNull(getCommand("warps")).setExecutor(commands);
        getServer().getPluginManager().registerEvents(new Listeners(),this);
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.YELLOW + "插件已加载完毕.");
    }

    public void onDisable(){
        config.clearConfig();
        HandlerList.unregisterAll((Listener) this);
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.YELLOW + "插件已卸载.");
    }

    public void onReload(){
        config.reload();
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.YELLOW + "插件重载完毕.");
    }

}
