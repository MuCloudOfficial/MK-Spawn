package me.mucloud.plugin.MK.Spawn.WarpModule;

import me.mucloud.plugin.MK.Spawn.WarpModule.Warps.WarpType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

public interface IWarp {

    String getDisplayName();

    String getInternalName();

    File getWarpFile();

    WarpType getType();

    void setLocation(Location newLocation);

    void delete();

    void teleport(Player player);


}
