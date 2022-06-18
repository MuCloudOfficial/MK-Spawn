package me.mucloud.plugin.MK.Spawn.SignModule;

import me.mucloud.plugin.MK.Spawn.Main;
import me.mucloud.plugin.MK.Spawn.WarpModule.Warps.Warp;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignPool {

    private Main main;
    private static Map<Warp,Sign> Pool;

    public SignPool(){

    }

    public void openPool(Main plugin){
        main = plugin;
        if(!main.getConfiguration().isUseSignWarp()){
            return;
        }
        Pool = new HashMap<>();
    }

    public void fetchSigns(){

    }

    public void addSignWarp(Warp warp,Sign sign){
        Pool.put(warp,sign);
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(warp.getWarpFile());

        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

}
