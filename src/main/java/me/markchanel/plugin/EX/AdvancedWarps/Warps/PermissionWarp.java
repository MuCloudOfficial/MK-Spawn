package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PermissionWarp extends Warp{

    private final String RequiredPermission;

    public PermissionWarp(String name, Location location,int coolingDown, String permission){
        super(name,location,coolingDown,WarpType.PERMISSION_WARP);
        RequiredPermission = permission;
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return target.hasPermission(RequiredPermission);
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
        if(target.hasPermission("exaw.teleportwarp.bypass." + getName())){
            target.teleport(getLocation());
            target.sendMessage("§6你已传送至地标 §e" + getName());
            WarpPool.addCoolingDown(target,getCoolingDown());
            return;
        }
        if(checkHasRequirements(target)){
            target.teleport(getLocation());
            WarpPool.addCoolingDown(target,getCoolingDown());
        }else{
            target.sendMessage("§4你未符合传送需求! 传送被拒绝.");
        }
    }

    // 为后续版本准备.
    public String getRequirements() { return RequiredPermission; }

}
