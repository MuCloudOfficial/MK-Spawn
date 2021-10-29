package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PermissionWarp extends Warp<String> {

    private String RequiredPermission;

    public PermissionWarp(String name, Location location, String permission){
        super(name,location,WarpType.PERMISSION_WARP);
        RequiredPermission = permission;
    }

    @Override
    public String getRequirements() {
        return RequiredPermission;
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return target.hasPermission(RequiredPermission);
    }
}
