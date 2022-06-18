package me.mucloud.plugin.MK.Spawn;

import java.io.File;

public interface IConfiguration {

    int getTeleportCoolingDown();

    boolean isGUIMode();

    boolean isUseSignWarp();

    File getWarpFolder();

    File getLocaleFolder();

    String getLocale();

}
