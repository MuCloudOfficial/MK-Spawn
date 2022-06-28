package me.MuCloud.plugin.MK.Spawn;

import me.MuCloud.API.MK.Spawn.IConfiguration;
import me.MuCloud.API.MK.Spawn.IWarpPool;
import me.MuCloud.plugin.MK.Spawn.WarpModule.WarpPool;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static final String Prefix = "§b§lMK§7§l-§e§lSpawn";
    private final Configuration Config = new Configuration(this);
    private final WarpPool warpPool = new WarpPool(this);

    @Override public void onEnable() {
        Config.onLoad();
        warpPool.onLoad();
    }

    @Override public void onDisable() {
        Config.ClearConfiguration();
        warpPool.clearPool();
    }

    public void onReload(){

        // 其余使用动态加载 无需重新部署
        Config.onLoad();
        warpPool.onLoad();

        Config.ClearConfiguration();
        warpPool.clearPool();

    }

    public IWarpPool getWarpPool(){
        return warpPool;
    }
    public String getPrefix(){
        return Prefix;
    }

    public IConfiguration getConfiguration(){
        return Config;
    }

}
