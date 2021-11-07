package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NormalWarp extends Warp{

    public NormalWarp(String name, Location location,int coolingDown){
        super(name,location,coolingDown,WarpType.NORMAL);
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return true;
    }

    @Override
    public void teleportTo(Player target) {
        if(!target.hasPermission("essentials.warps." + getName())) {
            target.sendMessage("§4你没有权限执行此操作");
            return;
        }
        if(WarpPool.getCoolingDown(target)){
            target.sendMessage("§4传送冷却中.  请稍后再试.");
            return;
        }
        target.sendMessage("§6你已被传送至地标 §e" + getName());
        target.teleport(getLocation());
        WarpPool.addCoolingDown(target,getCoolingDown());
    }
}
