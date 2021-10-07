package me.markchanel.plugin.EX.AdvancedWarps;

import com.earth2me.essentials.Settings;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Config {

    private final Main main;
    public static ISettings EssC;
    public static File[] targetFiles;
    public static Map<String,Location> Warps = new HashMap<>();
    public static Map<String,List<Object>> RequiredItemWarps = new HashMap<>();
    public static Map<String,Double> RequiredMoneyWarps = new HashMap<>();
    public static Map<String,String> RequiredPermissionWarps = new HashMap<>();

    public Config(Main plugin){
        main = plugin;
    }

    public void initConfig(){
        EssC = new Settings((IEssentials) main.getServer().getPluginManager().getPlugin("Essentials"));
        refreshWarpFiles();
        main.getServer().getConsoleSender().sendMessage(new File(EssC.getConfigFile().getParentFile().getAbsolutePath() + File.separator + "warps").getAbsolutePath());
        initEssWarps();
        searchRequirements();
    }

    public void initEssWarps(){
        FileConfiguration fc = new YamlConfiguration();
        if(targetFiles.length == 0){
            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.YELLOW + "当前无地标可记录.");
            return;
        }
        for(File f : targetFiles){
            try {
                fc.load(f);
                if(fc.isSet("name")){
                    Warps.put(fc.getString("name"),
                            new Location(main.getServer().getWorld(fc.getString("world-name")),
                                            fc.getDouble("x"),
                                            fc.getDouble("y"),
                                            fc.getDouble("z"),
                                            Float.parseFloat(fc.getString("yaw")),
                                            Float.parseFloat(fc.getString("pitch"))));
                }
                File newFile = new File(f.getParentFile().getAbsolutePath() + File.separator + fc.getString("name") + ".yml");
                f.renameTo(newFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        refreshWarpFiles();
        main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.YELLOW + "已将所有 Essentials 地标转换至正常状态.");
    }

    public void searchRequirements() {
        List<String> warnMessages = new ArrayList<>();
        warnMessages.add(Main.Prefix + ChatColor.RED + "============================== 警告信息 ================================");
        FileConfiguration f = new YamlConfiguration();
        if (targetFiles == null) {
            return;
        }
        for (File file : targetFiles) {
            try {
                f.load(file);
                if (!f.isSet("Requirements")) {
                    warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 并没有定义需求块. 该地标已忽略.");
                    continue;
                }
                if (!f.isSet("Requirements.Type")) {
                    warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 并没有定义需求类型. 该地标已忽略.");
                    continue;
                }
                switch (f.getString("Requirements.Type").toLowerCase()) {
                    case "item":
                        if (!f.isSet("Requirements.Material")) {
                            warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 并没有定义需求 Item. 该地标已忽略.");
                            continue;
                        }
                        if (!f.isSet("Requirements.Amount")) {
                            warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 并没有定义需求 Item 数量. 该地标已忽略.");
                            continue;
                        }
                        if (!f.isSet("Requirements.ItemName")) {
                            warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 并没有定义需求 Permission. 该地标已忽略.");
                            continue;
                        }
                        if (!f.isSet("Requirements.ItemLore")) {
                            warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 并没有定义需求 Permission. 该地标已忽略.");
                            continue;
                        }
                        List<Object> itemInfo = new ArrayList<>();
                        itemInfo.add(f.getString("Requirements.Material").toUpperCase());
                        itemInfo.add(f.getString("Requirements.ItemName"));
                        itemInfo.add(f.getStringList("Requirements.ItemLore"));
                        itemInfo.add(f.getInt("Requirements.Amount"));
                        RequiredItemWarps.put(f.getString("name"), itemInfo);
                        continue;
                    case "money":
                        if (!f.isSet("Requirements.Amount")) {
                            warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 并没有定义需求 Money 数量. 该地标已忽略.");
                            continue;
                        }
                        RequiredMoneyWarps.put(f.getString("name"), f.getDouble("Requirements.Amount"));
                        continue;
                    case "permission":
                        if (!f.isSet("Requirements.Permission")) {
                            warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 并没有定义需求 Permission. 该地标已忽略.");
                            continue;
                        }
                        RequiredPermissionWarps.put(f.getString("name"), f.getString("Requirements.Permission"));
                        continue;
                    default:
                        warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 定义的需求类型不合法. 该地标已忽略.");
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        if (warnMessages.size() != 1) {
            warnMessages.add(Main.Prefix + ChatColor.RED + "=======================================================================");
            for (String s : warnMessages) {
                main.getServer().getConsoleSender().sendMessage(s);
            }
        } else {
            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.YELLOW + "已装载地标.");
        }
    }

    public static void refreshWarpFiles(){
        targetFiles = new File(EssC.getConfigFile().getParentFile().getAbsolutePath() + File.separator + "warps").listFiles();
    }

    public static File getWarpFile(String warpName){
        try {
            FileConfiguration f = new YamlConfiguration();
            refreshWarpFiles();
            for(File file : targetFiles){
                f.load(file);
                if(warpName.equals(f.getString("name"))){
                    return file;
                }
            }
            return null;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveConfig(){
        EssC = null;
        targetFiles = null;
        Warps.clear();
        RequiredItemWarps.clear();
        RequiredMoneyWarps.clear();
        RequiredPermissionWarps.clear();
    }
}
