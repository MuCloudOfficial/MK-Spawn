package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NormalWarp extends Warp{

    public NormalWarp(String name, Location location){
        super(name,location,WarpType.NORMAL);
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return true;
    }

    @Override
    public void teleportTo(Player target) {
        target.sendMessage("§6你已被传送至地标 §e" + getName());
        target.teleport(getLocation());
    }
}
