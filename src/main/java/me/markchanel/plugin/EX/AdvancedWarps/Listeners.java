package me.markchanel.plugin.EX.AdvancedWarps;

import com.earth2me.essentials.signs.EssentialsSign;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.Warp;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.WarpPool;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.WarpType;
import net.ess3.api.IUser;
import net.ess3.api.events.SignCreateEvent;
import net.ess3.api.events.SignInteractEvent;
import net.ess3.api.events.UserWarpEvent;
import net.essentialsx.api.v2.events.WarpModifyEvent;
import org.bukkit.Location;
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
        wme.setCancelled(true);
        String targetWarp = wme.getWarpName();
        Location targetLoc = wme.getNewLocation();
        IUser targetU = wme.getUser();
        switch(wme.getCause()){
            case CREATE:
                pool.createWarp(targetU.getBase(),targetWarp,targetLoc);
                break;
            case DELETE:
                pool.remove(targetWarp);
                break;
            case UPDATE:
                Warp warp = pool.getWarp(targetWarp);
                pool.modifyName(warp,targetLoc);
                break;
        }
    }

    @EventHandler
    public void SignCreateListener(SignCreateEvent sce){
        sce.setCancelled(true);
        EssentialsSign.ISign sign = sce.getSign();
        String WarpPosition = sign.getLine(0);
        String WarpName = sign.getLine(1);
        String Detail;
        IUser targetU = sce.getUser();
        if(!WarpPosition.equals("[warp]")){
            return;
        }
        if(pool.isContains(WarpName)){
            targetU.sendMessage("§4该地标未被定义过");
        }
        WarpPosition = "§1§lWarp";
        Detail = "§6§l点击传送至";
        sign.setLine(0,WarpPosition);
        sign.setLine(1,Detail);
        sign.setLine(2,WarpName);
    }

    @EventHandler
    public void SignInteractListener(SignInteractEvent sie){
        sie.setCancelled(true);
        EssentialsSign.ISign sign = sie.getSign();
        String Line1 = sign.getLine(0);
        String Line3 = sign.getLine(2);
        IUser targetU = sie.getUser();
        if(!Line1.equals("§1§lWarp")){
            return;
        }
        if(!pool.isContains(Line3)){
            return;
        }
        pool.getWarp(Line3).teleportTo(targetU.getBase());
    }
}
