package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.markchanel.plugin.EX.AdvancedWarps.Config;

import java.io.File;
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

    public void add(Warp warp){
        Pool.add(warp);
    }

    public void remove(Warp warp){
        Pool.remove(warp);
    }

    public void clearPool(){
        Pool.clear();
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
        return WarpFolder.listFiles();
    }

}
