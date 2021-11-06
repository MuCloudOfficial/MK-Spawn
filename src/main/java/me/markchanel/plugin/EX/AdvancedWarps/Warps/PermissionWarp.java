package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PermissionWarp extends Warp{

    private final String RequiredPermission;

    public PermissionWarp(String name, Location location, String permission){
        super(name,location,WarpType.PERMISSION_WARP);
        RequiredPermission = permission;
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return target.hasPermission(RequiredPermission);
    }

    @Override
    public void teleportTo(Player target) {
        if(checkHasRequirements(target)){
            target.teleport(getLocation());
            target.sendMessage("§6你已传送至地标 §e" + getName());
        }else{
            target.sendMessage("§4你未符合传送需求! 传送被拒绝.");
        }
    }

    // 为后续版本准备.
    public String getRequirements() { return RequiredPermission; }

}
