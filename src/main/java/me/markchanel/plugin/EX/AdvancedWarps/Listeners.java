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

    @EventHandler
    public void SignCreateListener(SignCreateEvent sce){
        EssentialsSign.ISign sign = sce.getSign();
        String WarpPosition = sign.getLine(0);
        String WarpName = sign.getLine(1);
        String Detail;
        Player targetP = sce.getUser().getBase();
        if(!WarpPosition.equals("[warp]")){
            return;
        }
        if(!pool.isContains(WarpName)){
            targetP.sendMessage("§4该地标未被定义过");
            return;
        }
        WarpPosition = "§1§lWarp";
        Detail = "§6§l点击传送至";
        sign.setLine(0,WarpPosition);
        sign.setLine(1,Detail);
        sign.setLine(2,WarpName);
        sce.setCancelled(true);
    }

    @EventHandler
    public void SignInteractListener(SignInteractEvent sie){
        IUser targetU = sie.getUser();
        EssentialsSign.ISign sign = sie.getSign();
        pool.getWarp(sign.getLine(1)).teleportTo(targetU.getBase());
        sie.setCancelled(true);
    }

    /*
      //  为未来版本准备.
      public void SignChangeListener(SignChangeEvent sce){
              String WarpPosition = sce.getLine(0);
              String WarpName = sce.getLine(1);
              String Detail;
              Player targetP = sce.getPlayer();
              if(!Objects.equals(WarpPosition, "[warp]")){
                  return;
              }
              if(sce.getLines().length < 2){
                  return;
              }
              if(!pool.isContains(WarpName)){
                  targetP.sendMessage("§4该地标未被定义过");
                  return;
              }
              WarpPosition = "§1§lWarp";
              Detail = "§e§l点击传送至";
              sce.setLine(0,WarpPosition);
              sce.setLine(1,Detail);
              sce.setLine(2,WarpName);
          }

          public void SignInteractListener(PlayerInteractEvent pie){}

     */


}
