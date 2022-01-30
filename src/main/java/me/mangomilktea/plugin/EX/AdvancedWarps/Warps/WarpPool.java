package me.mangomilktea.plugin.EX.AdvancedWarps.Warps;

import me.mangomilktea.plugin.EX.AdvancedWarps.Config;
import me.mangomilktea.plugin.EX.AdvancedWarps.Main;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WarpPool {

    private static Main main;
    private static File WarpFolder;
    private static List<Warp> Pool;
    private static Map<Player,Integer> CoolingDownPool;
    private static Map<Location,Warp> SignWarps;

    public WarpPool(){}

    public void openPool(Main plugin){
        main = plugin;
        WarpFolder = new File (Config.getEssC().getConfigFile().getParentFile().getAbsolutePath() + File.separator + "warps");
        Pool = new ArrayList<>();
        CoolingDownPool = new HashMap<>();
        SignWarps = new HashMap<>();
    }

    @Nullable public Warp getWarp(String warpName){
        for(Warp warp : Pool){
            if(warpName.equals(warp.getName())){
                return warp;
            }
        }
        return null;
    }

    public boolean isContains(String name){
        return Pool.contains(getWarp(name));
    }

    public void createWarp(Player lastowner, String name, Location location,int coolingDown){
        if(listWarpsName().contains(name)){
            lastowner.sendMessage("§4该地标已被定义过");
            return;
        }
        Warp warp = new NormalWarp(name, location, coolingDown);
        File warpFile = getWarpFile(warp);
        FileConfiguration fc = new YamlConfiguration();
        try {
            warpFile.createNewFile();
            fc.load(warpFile);
            fc.set("world", Objects.requireNonNull(location.getWorld()).getUID().toString());
            fc.set("world-name",location.getWorld().getName());
            fc.set("x",location.getX());
            fc.set("y",location.getY());
            fc.set("z",location.getZ());
            fc.set("yaw",location.getYaw());
            fc.set("pitch",location.getPitch());
            fc.set("name",name);
            fc.set("lastowner",lastowner.getUniqueId().toString());
            fc.set("Requirement.Type","NORMAL");
            fc.set("CoolingDown",coolingDown);
            fc.save(warpFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Pool.add(warp);
    }

    public void add(@NotNull Warp warp){
        Pool.add(warp);
    }

    public void remove(String warpName){
        Warp warp = getWarp(warpName);
        Pool.remove(warp);
        warp.getFile().delete();
    }

    public void clearPool(){
        Pool.clear();
    }

    public void modifyName(@NotNull Warp warp, Location newLocation){
        warp.setLocation(newLocation);
    }

    @NotNull public static  File getWarpFile(@NotNull Warp warp){
        return new File(WarpFolder,warp.getName() + ".yml");
    }

    public List<String> listWarpsName(){
        List<String> names = new ArrayList<>();
        for(Warp warp : Pool){
            names.add(warp.getName());
        }
        return names;
    }

    public List<String> listHasRequirementWarpsName(@NotNull WarpType type){
        List<Warp> target = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for(Warp warp : Pool){
            if(warp.getType() == type){
                target.add(warp);
            }
        }
        for(Warp warp : target){
            names.add(warp.getName());
        }
        return names;
    }

    public File[] listWarpsFile(){
        List<File> files = new ArrayList<>();
        for(File f : WarpFolder.listFiles()){
            if(f.getName().endsWith(".yml")){
                files.add(f);
            }
        }
        return files.toArray(new File[0]);
    }

    protected static void addCoolingDown(Player target,int coolingDown){
        if(CoolingDownPool.size() == 0){
            CoolingDownPoolCounting();
        }
        CoolingDownPool.put(target,coolingDown);
    }

    protected static boolean getCoolingDown(Player target){
        return CoolingDownPool.containsKey(target);
    }

    private static void CoolingDownPoolCounting(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(CoolingDownPool.size() == 0){
                    cancel();
                }
                for(Map.Entry<Player,Integer> entry : CoolingDownPool.entrySet()){
                    CoolingDownPool.replace(entry.getKey(), entry.getValue() - 1);
                    if(entry.getValue() == 0){
                        CoolingDownPool.remove(entry.getKey());
                    }
                }
            }
        }.runTaskTimer(main,0,20L);
    }

    public void addSignWarp(Location targetLoc ,Warp targetWarp){
        SignWarps.put(targetLoc,targetWarp);
        FileConfiguration fc = new YamlConfiguration();
        List<Location> LocList = new ArrayList<>();
        for(Map.Entry<Location,Warp> entry : SignWarps.entrySet()){
            if(entry.getValue().equals(targetWarp)){
                LocList.add(entry.getKey());
            }
        }
        try {
            File targetF = getWarpFile(targetWarp);
            fc.load(targetF);
            fc.set("Signs", LocList);
            fc.save(targetF);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void removeSignWarp(Location targetLoc,Warp targetWarp){
        SignWarps.remove(targetLoc);
        FileConfiguration fc = new YamlConfiguration();
        List<Location> LocList = new ArrayList<>();
        for(Map.Entry<Location,Warp> entry : SignWarps.entrySet()){
            if(entry.getValue().equals(targetWarp)){
                LocList.add(entry.getKey());
            }
        }
        try {
            File targetF = getWarpFile(targetWarp);
            fc.load(targetF);
            fc.set("Signs", LocList);
            fc.save(targetF);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Nullable public Warp getSignWarp(Location targetLoc){
        if(!SignWarps.containsKey(targetLoc)) {
            return null;
        }
        return SignWarps.get(targetLoc);
    }
}
