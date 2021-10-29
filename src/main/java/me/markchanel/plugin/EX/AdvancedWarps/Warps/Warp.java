package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import me.markchanel.plugin.EX.AdvancedWarps.Config;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class Warp<T> {

    private String WarpName;
    private Location WarpLocation;
    private File WarpFile;
    private WarpType Type;

    Warp(){
        WarpName = null;
        WarpLocation = null;
        WarpFile = null;
        Type = WarpType.NORMAL;
    }

    Warp(String name,Location location,WarpType type){
        WarpName = name;
        WarpLocation = location;
        Type = type;
        WarpFile = Config.getWarpFile(name);
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

    public abstract T getRequirements();
    public abstract Boolean checkHasRequirements(Player target);

}

enum WarpType{
    NORMAL,
    MONEY_WARP,
    ITEM_WARP,
    PERMISSION_WARP
}
