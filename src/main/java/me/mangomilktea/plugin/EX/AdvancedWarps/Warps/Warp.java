package me.mangomilktea.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class Warp {

    private final String WarpName;
    private Location WarpLocation;
    private final File WarpFile;
    private final WarpType Type;
    private int CoolingDown;

    Warp(String name,Location location,int coolingDown,WarpType type){
        WarpName = name;
        WarpLocation = location;
        CoolingDown = coolingDown;
        Type = type;
        WarpFile = WarpPool.getWarpFile(this);
    }

    public String getName(){
        return WarpName;
    }

    public int getCoolingDown(){
        return CoolingDown;
    }

    public void setCoolingDown(int coolingDown){
        CoolingDown = coolingDown;
    }

    protected Location getLocation(){
        return WarpLocation;
    }

    public void setLocation(Location newLocation){
        WarpLocation = newLocation;
    }

    public File getFile() {
        return WarpFile;
    }

    public WarpType getType(){
        return Type;
    }

    public abstract Boolean checkHasRequirements(Player target);

    public abstract void teleportTo(Player target);
}

