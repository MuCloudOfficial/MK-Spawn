package me.mucloud.plugin.MK.Spawn.WarpModule.Warps;

import me.mucloud.plugin.MK.Spawn.WarpModule.IWarp;
import me.mucloud.plugin.MK.Spawn.WarpModule.WarpPool;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public abstract class Warp implements IWarp {

    private String InternalName;
    private String DisplayName;
    private Location Location;
    private File WarpFile;
    private WarpType Type;
    private UUID Creator;

    @Override public WarpType getType(){
        return Type;
    }

    @Override public File getWarpFile(){
        return WarpFile;
    }

    @Override public String getDisplayName() {
        return DisplayName;
    }

    @Override public String getInternalName() {
        return InternalName;
    }

    @Override public void setLocation(Location newLocation){
        Location = newLocation;
    }

    @Override public void delete() {
        new WarpPool().deleteWarp(this);
    }

    public abstract void createFile();

    @Override public abstract void teleport(Player player);

}

