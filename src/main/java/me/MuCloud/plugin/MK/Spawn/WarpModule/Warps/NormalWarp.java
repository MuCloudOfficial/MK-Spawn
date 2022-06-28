package me.MuCloud.plugin.MK.Spawn.WarpModule.Warps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NormalWarp extends Warp{

    NormalWarp(String internalName, Location location, String displayName, UUID creator) {
        super(internalName,WarpType.NORMAL,location,displayName,creator);
    }

    @Override public void teleport(Player target) {
        if(!target.hasPermission("mks.warp." + getInternalName())){
            target.sendMessage("§4你没有权限执行此操作");
            return;
        }
        target.teleport(getLocation());
    }

}
