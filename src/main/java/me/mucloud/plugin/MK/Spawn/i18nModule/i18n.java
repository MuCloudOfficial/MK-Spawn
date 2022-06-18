package me.mucloud.plugin.MK.Spawn.i18nModule;

import me.mucloud.plugin.MK.Spawn.Configuration;
import me.mucloud.plugin.MK.Spawn.Main;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class i18n {

    private final Main main;
    private static File LocaleFolder;
    private static String Locale;

    public i18n(Main plugin,Configuration config){
        main = plugin;
        LocaleFolder = config.getLocaleFolder();
        Locale = config.getLocale();
    }

    public void onLoad(){
        checkIntegrity();
        try {
            loadLanguage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            main.getLogger().log(Level.WARNING,"The Language File is corrupt");
        }
    }

    public void checkIntegrity(){
        if(LocaleFolder.listFiles() == null){
            main.saveResource("Lang.zh_CN.yml",true);
            main.saveResource("Lang.en_US.yml",true);
        }

    }

    public void loadLanguage() throws IOException, InvalidConfigurationException {
        FileConfiguration fc = new YamlConfiguration();
        for(File f : LocaleFolder.listFiles()){
            fc.load(f);
            if(f.getName().equals("Lang." + Locale + ".yml")){
                for(Messages m : Messages.values()){
                    if(fc.get(m.name()) == null){
                        throw new InvalidConfigurationException();
                    }
                }
            }
        }
    }

}
