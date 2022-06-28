package me.MuCloud.plugin.MK.Spawn.WarpModule.Warps;

import me.MuCloud.API.MK.Spawn.IWarp;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class Warp implements IWarp {

    private String InternalName;
    private String DisplayName;
    private Location Location;
    private WarpType Type;
    private UUID Creator;

    Warp(String internalName,WarpType type,Location location,String displayName,UUID creator){
        InternalName = internalName;
        DisplayName = displayName;
        Location = location;
        Type = type;
        Creator = creator;
    }

    public Location getLocation(){
        return Location;
    }

    @Override public abstract void teleport(Player target);

    @Override public String getDisplayName() {
        return DisplayName;
    }

    @Override public String getInternalName(){
        return InternalName;
    }

    @Override public WarpType getType(){
        return Type;
    }

}
