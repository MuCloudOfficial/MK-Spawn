package me.MuCloud.API.MK.Spawn;

import me.MuCloud.plugin.MK.Spawn.WarpModule.Warps.WarpType;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IWarp {

    void teleport(Player target);

    String getDisplayName();

    String getInternalName();

    WarpType getType();

}
