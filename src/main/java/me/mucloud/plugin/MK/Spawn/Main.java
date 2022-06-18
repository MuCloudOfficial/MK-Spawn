package me.mucloud.plugin.MK.Spawn;

import me.mucloud.plugin.MK.Spawn.CommandModule.CommandProcessor;
import me.mucloud.plugin.MK.Spawn.SignModule.SignPool;
import me.mucloud.plugin.MK.Spawn.WarpModule.WarpPool;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static final String Prefix = "§b§lMK§7§l-§e§lSpawn";
    private final Configuration config = new Configuration(this);
    private final WarpPool WPM = new WarpPool();
    private final SignPool SPM = new SignPool();

    @Override public void onEnable(){

        //  获取设置
        config.onLoad();

        //  启动地标池
        WPM.openPool(this);

        //  尝试启动地标告示池
        SPM.openPool(this);

        //  注册监听器


        //  注册指令
        getCommand("").setExecutor(new CommandProcessor());

    }

    @Override public void onDisable(){

    }

    public void onReload(){

    }

    public String getPrefix(){
        return Prefix;
    }

    public IConfiguration getConfiguration(){
        return config;
    }

    public WarpPool getWarpPool(){
        return WPM;
    }

    public SignPool getSignPool(){
        return SPM;
    }

}
