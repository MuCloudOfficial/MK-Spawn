package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.markchanel.plugin.EX.AdvancedWarps.Config;
import me.markchanel.plugin.EX.AdvancedWarps.Main;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarpPool {

    private static File WarpFolder;
    private List<Warp> Pool;

    public WarpPool(){}

    public void openPool(){
        WarpFolder = new File (Config.getEssC().getConfigFile().getParentFile().getAbsolutePath() + File.separator + "warps");
        Pool = new ArrayList<>();
    }

    public Warp getWarp(String warpName){
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

    public void createWarp(Player lastowner, String name, Location location){
        if(listWarpsName().contains(name)){
            lastowner.sendMessage("§4该地标已被定义过");
            return;
        }
        Warp warp = new NormalWarp(name, location);
        File warpFile = getWarpFile(warp);
        FileConfiguration fc = new YamlConfiguration();
        try {
            warpFile.createNewFile();
            fc.load(warpFile);
            fc.set("world",location.getWorld().getUID());
            fc.set("world-name",location.getWorld().getName());
            fc.set("x",location.getX());
            fc.set("y",location.getY());
            fc.set("z",location.getZ());
            fc.set("yaw",location.getYaw());
            fc.set("pitch",location.getPitch());
            fc.set("name",name);
            fc.set("lastowner",lastowner.getUniqueId());
            fc.set("Requirement.Type","NORMAL");
            fc.save(warpFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Pool.add(warp);
    }

    public void add(Warp warp){
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

    public void modifyName(Warp warp,Location newLocation){
        warp.setLocation(newLocation);
    }

    @Nullable public static File getWarpFile(Warp warp){
        return new File(WarpFolder,warp.getName());
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
            if(warp.getType() != type){
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

}
