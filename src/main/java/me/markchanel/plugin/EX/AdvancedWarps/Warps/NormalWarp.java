package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NormalWarp extends Warp{

    public NormalWarp(String name, Location location){
        super(name,location,WarpType.NORMAL);
    }

    public Object getRequirements() {
        return null;
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return true;
    }
}
