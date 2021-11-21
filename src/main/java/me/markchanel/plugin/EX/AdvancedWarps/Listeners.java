package me.markchanel.plugin.EX.AdvancedWarps;

import com.earth2me.essentials.signs.EssentialsSign;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.Warp;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.WarpPool;
import net.ess3.api.IUser;
import net.ess3.api.events.SignCreateEvent;
import net.ess3.api.events.SignInteractEvent;
import net.essentialsx.api.v2.events.WarpModifyEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Listeners implements Listener {

    private static final WarpPool pool = new WarpPool();

    @EventHandler
    private void WarpModifyListener(WarpModifyEvent wme){
        wme.setCancelled(true);
        String targetWarp = wme.getWarpName();
        Location targetLoc = wme.getNewLocation();
        IUser targetU = wme.getUser();
        switch(wme.getCause()){
            case CREATE:
                pool.createWarp(targetU.getBase(),targetWarp,targetLoc,3);
                targetU.sendMessage("§6你已创建一个新地标 " + "§e" + targetWarp);
                break;
            case DELETE:
                pool.remove(targetWarp);
                targetU.sendMessage("§e" + targetWarp + " §6地标已被删除");
                break;
            case UPDATE:
                Warp warp = pool.getWarp(targetWarp);
                pool.modifyName(warp,targetLoc);
                targetU.sendMessage("§e" + targetWarp + " §6地标位置已更新.");
                break;
        }
    }

    @EventHandler
    private void EssSignDenied(SignCreateEvent sce){
        sce.setCancelled(true);
        sce.getUser().sendMessage("§6建议使用 [EXWarp] 创建地标.");
    }

    @EventHandler
    private void EssSignConvert(SignInteractEvent sie){
        sie.setCancelled(true);
        EssentialsSign.ISign sign = sie.getSign();
        if(sign.getLine(0).equals("§1§lWarp")){
            return;
        }
        Warp targetWarp = pool.getWarp(sie.getSign().getLine(1));
        Location targetL = sie.getSign().getBlock().getLocation();
        sign.setLine(0,"§1§lWarp");
        sign.setLine(1,"§e§l点击传送至");
        sign.setLine(2,targetWarp.getName());
        pool.addSignWarp(targetL,targetWarp);
        sie.getUser().sendMessage("§6地标牌已改变");
    }

    @EventHandler
    private void SignCreateListener(SignChangeEvent sce){
        Player targetP = sce.getPlayer();
        String warpPosition = sce.getLine(0);
        String warpName = sce.getLine(1);
        if(!warpPosition.equals("[EXWarp]")){
            return;
        }
        if(!targetP.hasPermission("advancedwarps.admin")){
            targetP.sendMessage("§4你没有权限执行此操作");
            return;
        }
        if(!pool.isContains(warpName)){
            sce.setLine(0,"§4§lWarp");
            sce.setLine(1,"§4§l未定义该地标");
            sce.setLine(2,warpName);
            return;
        }
        sce.setLine(0,"§1§lWarp");
        sce.setLine(1,"§e§l点击传送至");
        sce.setLine(2,warpName);
        pool.addSignWarp(sce.getBlock().getLocation(),pool.getWarp(warpName));
    }

    @EventHandler
    private void SignBreakListener(BlockBreakEvent bbe){
        Player targetP = bbe.getPlayer();
        Location targetL = bbe.getBlock().getLocation();
        if(pool.getSignWarp(targetL) == null){
            return;
        }
        if(!targetP.hasPermission("exaw.admin")){
            targetP.sendMessage("§4你没有权限破坏传送标识");
            bbe.setCancelled(true);
        }
        pool.removeSignWarp(targetL,pool.getSignWarp(targetL));
    }

    @EventHandler
    private void SignInteractListener(PlayerInteractEvent pie){
        Player targetP = pie.getPlayer();
        Location targetL = pie.getClickedBlock().getLocation();
        if(pie.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if(pool.getSignWarp(targetL) != null) {
            pool.getSignWarp(targetL).teleportTo(targetP);
        }
    }

}
