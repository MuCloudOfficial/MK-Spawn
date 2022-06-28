package me.MuCloud.API.MK.Spawn;

import me.MuCloud.plugin.MK.Spawn.WarpModule.Warps.Warp;

public interface IWarpPool {

    boolean isContain(Warp warp);

    void deleteWarp(Warp warp);

    void createWarp(Warp warp);

    void clearPool();

}
