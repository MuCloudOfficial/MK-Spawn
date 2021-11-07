package me.markchanel.plugin.EX.AdvancedWarps;

import com.earth2me.essentials.signs.EssentialsSign;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.Warp;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.WarpPool;
import net.ess3.api.IUser;
import net.ess3.api.events.SignInteractEvent;
import net.essentialsx.api.v2.events.WarpModifyEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Objects;

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
        if(pool.isContains(WarpName)){
            targetP.sendMessage("§4该地标未被定义过");
            return;
        }
        WarpPosition = "§1§lWarp";
        Detail = "§6§l点击传送至";
        sce.setLine(0,WarpPosition);
        sce.setLine(1,Detail);
        sce.setLine(2,WarpName);
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
