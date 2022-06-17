package me.mucloud.plugin.MK.Spawn.WarpModule;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IWarp {

    String getDisplayName();

    String getInternalName();

    void setLocation(Location newLocation);

    void delete();

    void teleport(Player player);

}
