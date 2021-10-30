package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import com.sun.istack.internal.Nullable;
import me.markchanel.plugin.EX.AdvancedWarps.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WarpPool {

    private static File WarpFolder;
    private final List<Warp> Pool;

    public WarpPool(){
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

    public List<Warp> listWarps(){
        return Pool;
    }

    public List<Warp> listHasRequirementWarps(WarpType type){
        List<Warp> target = new ArrayList<>();
        for(Warp warp : Pool){
            if(warp.getType() != type){
                target.add(warp);
            }
        }
        return target;
    }

    public File[] listWarpsFile(){
        return WarpFolder.listFiles();
    }

}
