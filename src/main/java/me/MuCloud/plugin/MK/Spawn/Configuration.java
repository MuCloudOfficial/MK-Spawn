package me.MuCloud.plugin.MK.Spawn;

import me.MuCloud.API.MK.Spawn.IConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Configuration implements IConfiguration {

    private final Main main;
    private static File ConfigFolder;
    private static File ConfigFile;
    private static File WarpFolder;
    private static File SpawnPointFile;
    private static File LocaleFolder;

    Configuration(Main plugin){
        main = plugin;
        ConfigFolder = main.getDataFolder();
        ConfigFile = new File(ConfigFolder,"config.yml");
        WarpFolder = new File(ConfigFolder,"warps");
        LocaleFolder = new File(ConfigFolder,"Locales");
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
        if(!ConfigFile.exists() || ConfigFile.length() == 0){
            main.saveDefaultConfig();
        }
        if(!WarpFolder.exists()){
            WarpFolder.mkdir();
        }
        if(!LocaleFolder.exists()){
            LocaleFolder.mkdir();
        }
        if(!SpawnPointFile.exists()){
            try {
                SpawnPointFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String ConfigVersion;
    private static boolean AutoUpdate;
    private static int TeleportCoolingDown;
    private static String Language;
    private static boolean GUIMode;
    private static boolean UseSignWarp;

    private void fetchConfig(){
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(ConfigFile);
            if(fc.get("Version") == null){
                throw new InvalidConfigurationException("The configuration file does not define a configuration version, which means that the file is invalid. Please delete this file to rebuild.");
            }

            ConfigVersion = fc.getString("Version");
            AutoUpdate = fc.get("AutoUpdate") == null || fc.getBoolean("AutoUpdate");
            TeleportCoolingDown = fc.get("Settings.CoolingDown") == null ? 3 : fc.getInt("Settings.CoolingDown");
            Language = fc.get("Settings.Language") == null ? "Chinese" : fc.getString("Settings.Language");
            GUIMode = fc.get("Settings.GUIMode") == null || fc.getBoolean("Settings.GUIMode");
            UseSignWarp = fc.get("Settings.UseSignWarp") == null || fc.getBoolean("Settings.UseSignWarp");

        } catch (IOException | InvalidConfigurationException e) {
            main.getServer().getPluginManager().disablePlugin(main);
            throw new RuntimeException("Environment Error. The MK-Spawn will be disabled.");
        }
    }

    public void ClearConfiguration(){

    }

    @Override public String getVersion() {
        Yaml y = new Yaml();
        HashMap<?,?> map = y.loadAs(main.getResource("plugin.yml"),HashMap.class);
        return map.get("Version").toString();
    }

    @Override public File getWarpFolder() {
        return WarpFolder;
    }

    @Override public File getConfigFile() {
        return ConfigFile;
    }

    @Override public File getLocaleFolder() {
        return LocaleFolder;
    }

    @Override public String getLanguage() {
        return Language;
    }

    @Override public boolean isAutoUpdate() {
        return AutoUpdate;
    }

    @Override public boolean isGUIMode() {
        return GUIMode;
    }

    @Override public boolean isUseSignWarp() {
        return UseSignWarp;
    }

    @Override public int getCoolingDown() {
        return TeleportCoolingDown;
    }
}
