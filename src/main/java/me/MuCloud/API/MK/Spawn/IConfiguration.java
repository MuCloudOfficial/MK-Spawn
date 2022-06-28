package me.MuCloud.API.MK.Spawn;

import java.io.File;
public interface IConfiguration {

    String getVersion();

    File getWarpFolder();

    File getConfigFile();

    File getLocaleFolder();

    String getLanguage();

    boolean isAutoUpdate();

    boolean isGUIMode();

    boolean isUseSignWarp();

    int getCoolingDown();

}
