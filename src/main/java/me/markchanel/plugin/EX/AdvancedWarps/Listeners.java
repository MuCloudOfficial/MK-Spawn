package me.markchanel.plugin.EX.AdvancedWarps;

import me.markchanel.plugin.EX.AdvancedWarps.Warps.Warp;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.WarpPool;
import net.ess3.api.IUser;
import net.ess3.api.events.SignCreateEvent;
import net.essentialsx.api.v2.events.WarpModifyEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Listeners implements Listener {

    private static final WarpPool pool = new WarpPool();

    @EventHandler
    public void WarpModifyListener(WarpModifyEvent wme){
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

    public void EssSignDenied(SignCreateEvent sce){
        sce.setCancelled(true);
        sce.getUser().sendMessage("§6建议使用 [Warps] 创建地标.");
    }

    @EventHandler
    public void SignCreateListener(SignChangeEvent sce){
        String warpPosition = sce.getLine(0);
        String warpName = sce.getLine(1);
        if(!warpPosition.equals("[Warp]")){
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
        pool.addSignWarp(sce.getBlock(),pool.getWarp(warpName));
    }

    @EventHandler
    public void SignBreakListener(BlockBreakEvent bbe){
        Player targetP = bbe.getPlayer();
        Block targetB = bbe.getBlock();
        if(pool.getSignWarp(targetB) == null){
            return;
        }
        if(!targetP.hasPermission("exaw.admin")){
            targetP.sendMessage("§4你没有权限破坏传送标识");
            bbe.setCancelled(true);
        }
        pool.removeSignWarp(targetB);
    }

    @EventHandler
    public void SignInteractListener(PlayerInteractEvent pie){
        Player targetP = pie.getPlayer();
        Block targetB = pie.getClickedBlock();
        if(pool.getSignWarp(targetB) != null) {
            pool.getSignWarp(targetB).teleportTo(targetP);
        }
    }

}
