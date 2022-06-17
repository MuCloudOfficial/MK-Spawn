package me.mucloud.plugin.MK.Spawn.WarpModule.Warps;

import me.mucloud.plugin.MK.Spawn.WarpModule.IWarp;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Warp implements IWarp {

    private String InternalName;
    private String DisplayName;
    private Location Location;

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

    }

    @Override public abstract void teleport(Player player);

}
