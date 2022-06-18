package me.mucloud.plugin.MK.Spawn.WarpModule;

import me.mucloud.plugin.MK.Spawn.Main;
import me.mucloud.plugin.MK.Spawn.WarpModule.Warps.Warp;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpPool {

    private Main main;
    private static final List<Warp> Pool = new ArrayList<>();
    private static final Map<Player, BukkitTask> CoolingDownPool = new HashMap<>();
    private int CoolingDown;

    public void openPool(Main plugin){
        main = plugin;
        CoolingDown = main.getConfiguration().getTeleportCoolingDown();
    }

    public void addNewWarp(Warp warp){
        Pool.add(warp);
    }

    public void deleteWarp(Warp warp){
        Pool.remove(warp);
    }

    public boolean isContain(Warp warp){
        return Pool.contains(warp);
    }

    public void clearPool(){
        Pool.clear();
    }

    public void addCoolingDownPlayer(Player player){
        CoolingDownPool.put(player,new BukkitRunnable(){
            private int Time = CoolingDown;
            @Override public void run() {
                if(Time == 0){
                    CoolingDownPool.remove(player);
                }
                Time--;
            }
        }.runTaskTimer(main,0,20L));
    }


    public boolean isContain(Player player){
        return CoolingDownPool.containsKey(player);
    }

    public void clearCoolingDownPool(){
        CoolingDownPool.clear();
    }

    public List<String> listWarps(){
        List<String> result = new ArrayList<>(6);

        List<String> nw = new ArrayList<>();
        List<String> mw = new ArrayList<>();
        List<String> aw = new ArrayList<>();
        List<String> ew = new ArrayList<>();
        List<String> iw = new ArrayList<>();

        for(Warp warp : Pool){
            switch (warp.getType()){
                case NORMAL:
                    nw.add( "§6" + warp.getInternalName() + " §6(§7" + warp.getDisplayName() + "§6)§7, ");
                case MONEY:
                    mw.add( "§6" + warp.getInternalName() + " §6(§7" + warp.getDisplayName() + "§6)§7, ");
                case ACHIEVEMENT:
                    aw.add( "§6" + warp.getInternalName() + " §6(§7" + warp.getDisplayName() + "§6)§7, ");
                case EXP:
                    ew.add( "§6" + warp.getInternalName() + " §6(§7" + warp.getDisplayName() + "§6)§7, ");
                case ITEM:
                    iw.add( "§6" + warp.getInternalName() + " §6(§7" + warp.getDisplayName() + "§6)§7, ");
            }
            if(nw.size() == 0){

            }else{

            }

            if(mw.size() == 0){

            }else{

            }

            if(aw.size() == 0){

            }else{

            }

            if(ew.size() == 0){

            }else{

            }

            if(iw.size() == 0){

            }else{

            }

        }
        return nw;
    }

}
