package me.mucloud.plugin.MK.Spawn;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configuration implements IConfiguration{

    private final Main main;
    private static File ConfigFolder;
    private static File ConfigFile;
    private static File WarpFolder;
    private static File LocaleFolder;
    private static File SpawnPointFile;

    private String Locale;
    private int TeleportCoolingDown;
    private boolean GUIMode;
    private boolean UseSignWarp;

    public Configuration(Main plugin){
        main = plugin;
        ConfigFolder = main.getDataFolder();
        ConfigFile = new File(ConfigFolder,"config.yml");
        WarpFolder = new File(ConfigFolder,"warps");
        LocaleFolder = new File(ConfigFolder,"Locale");
        SpawnPointFile = new File(ConfigFolder,"spawnpoint.yml");
    }

    public void onLoad(){
        checkIntegrity();
        fetchConfig();
    }

    private void checkIntegrity(){
        if(!ConfigFolder.exists()){
            ConfigFolder.mkdir();
        }
        if(!ConfigFile.exists()){
            main.saveDefaultConfig();
        }
        if(!WarpFolder.exists()){
            WarpFolder.mkdir();
        }
        if(!LocaleFolder.exists()){
            LocaleFolder.mkdir();
        }
        try {
            if (!SpawnPointFile.exists()) {
                SpawnPointFile.createNewFile();
            }
        }catch(IOException IOE){
            IOE.printStackTrace();
        }
    }

    private void fetchConfig(){
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(ConfigFile);
            TeleportCoolingDown = fc.get("Settings.TeleportCoolingDown") == null ? fc.getInt("Settings.TeleportCoolingDown") : 10;
            GUIMode = fc.getBoolean("Setting.GUIMode");
            UseSignWarp = fc.getBoolean("Settings.UseSignWarp");
            Locale = fc.get("Settings.Locale") == null ? fc.getString("Settings.Locale") : "zh_CN";
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public int getTeleportCoolingDown(){
        return TeleportCoolingDown;
    }

    @Override public boolean isGUIMode(){
        return GUIMode;
    }

    @Override public boolean isUseSignWarp(){
        return UseSignWarp;
    }

    @Override public File getWarpFolder(){
        return WarpFolder;
    }

    @Override public File getLocaleFolder(){
        return LocaleFolder;
    }

    @Override public String getLocale(){
        return Locale;
    }


}
