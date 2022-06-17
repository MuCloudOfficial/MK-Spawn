package me.mucloud.plugin.MK.Spawn.WarpModule;

import me.mucloud.plugin.MK.Spawn.Configuration;
import me.mucloud.plugin.MK.Spawn.WarpModule.Warps.Warp;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpPool {

    private static final List<Warp> Pool = new ArrayList<>();
    private static final Map<Player, BukkitTask> CollingDownPool = new HashMap<>();
    private int CoolingDown;

    public void openPool(Configuration config){
        CoolingDown = config.getTeleportCoolingDown();
    }

    public void addNewWarp(){

    }

    public void deleteWarp(Warp warp){

    }

    public void isContain(Warp warp){

    }

    public void addCoolingDownPlayer(){

    }

}
