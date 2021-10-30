package me.markchanel.plugin.EX.AdvancedWarps;

import me.markchanel.plugin.EX.AdvancedWarps.Warps.Warp;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.WarpPool;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.WarpType;
import net.ess3.api.IUser;
import net.ess3.api.events.SignCreateEvent;
import net.ess3.api.events.SignInteractEvent;
import net.ess3.api.events.UserWarpEvent;
import net.essentialsx.api.v2.events.WarpModifyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Listeners implements Listener {

    private WarpPool pool;

    @EventHandler
    public void WarpListener(UserWarpEvent uwe){
        uwe.setCancelled(true);
        String targetWarp = uwe.getWarp();
        IUser targetU = uwe.getUser();
        if(!pool.isContains(targetWarp)){
            targetU.sendMessage("§4你没有指定一个有效的地标! 传送被拒绝.");
            return;
        }
        if(!targetU.getBase().hasPermission("essentials.warps." + targetWarp)){
            targetU.sendMessage("§4你没有权限执行此操作");
            return;
        }
        Warp warp = pool.getWarp(targetWarp);
        if(warp.getType() == WarpType.MONEY_WARP){
            if(!warp.checkHasRequirements(targetU.getBase())){
                targetU.sendMessage("§4你没有足够的金钱传送至地标! 传送被拒绝.");
                return;
            }
        }
        if(warp.getType() == WarpType.PERMISSION_WARP){
            if(!warp.checkHasRequirements(targetU.getBase())){
                targetU.sendMessage("§你没有足够的权限传送至地标! 传送被拒绝.");
                return;
            }
        }
        if(warp.getType() == WarpType.ITEM_WARP){
            if(!warp.checkHasRequirements(targetU.getBase())){
                targetU.sendMessage("§你没有足够的权限传送至地标! 传送被拒绝.");
                return;
            }
        }
        warp.teleportTo(targetU.getBase());
        targetU.sendMessage("§6你已传送至地标 " + targetWarp);
    }

    @EventHandler
    public void WarpModifyListener(WarpModifyEvent wme){

    }

    @EventHandler
    public void SignCreateListener(SignCreateEvent sce){

    }

    @EventHandler
    public void SignInteractListener(SignInteractEvent sie){

    }
}
