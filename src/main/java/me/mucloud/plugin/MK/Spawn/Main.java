package me.mucloud.plugin.MK.Spawn;

import me.mucloud.plugin.MK.Spawn.WarpModule.WarpPool;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static final String Prefix = "§b§lMK§7§l-§e§lSpawn";
    private final Configuration config = new Configuration(this);


    @Override public void onEnable(){

        //  获取设置
        config.onLoad();

        //  启动地标池

    }

    @Override public void onDisable(){

    }

    public void onReload(){

    }

    public String getPrefix(){
        return Prefix;
    }

}
