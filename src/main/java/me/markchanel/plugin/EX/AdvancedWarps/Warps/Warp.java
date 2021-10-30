package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class Warp {

    private final String WarpName;
    private Location WarpLocation;
    private final File WarpFile;
    private final WarpType Type;

    Warp(String name,Location location,WarpType type){
        WarpName = name;
        WarpLocation = location;
        Type = type;
        WarpFile = WarpPool.getWarpFile(this);
    }

    public String getName(){
        return WarpName;
    }

    public void teleportTo(Player target){
        target.teleport(WarpLocation);
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

}

