package me.markchanel.plugin.EX.AdvancedWarps;

import com.earth2me.essentials.Settings;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.ItemWarp;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.NormalWarp;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.Warp;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Config {

    private final Main main;
    public static ISettings EssC;
    protected static File WarpFolder;
    public static File[] targetFiles;
    public static List<Warp> Warps = new ArrayList<>();

    public Config(Main plugin){
        main = plugin;
    }

    public void initConfig(){
        EssC = new Settings((IEssentials) main.getServer().getPluginManager().getPlugin("Essentials"));
        WarpFolder = new File(EssC.getConfigFile().getParentFile().getAbsolutePath() + File.separator + "warps");
        targetFiles = WarpFolder.listFiles();
        convertEssWarps();
        initWarps();
    }

    public void convertEssWarps(){
        FileConfiguration fc = new YamlConfiguration();
        if(targetFiles.length == 0){
            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.YELLOW + "当前无地标可记录.");
            return;
        }
        for(File f : targetFiles){
            try {
                fc.load(f);
                if(fc.isSet("name")){
                    Warps.add(new NormalWarp(fc.getString("name"),
                            new Location(main.getServer().getWorld(fc.getString("world-name")),
                                            fc.getDouble("x"),
                                            fc.getDouble("y"),
                                            fc.getDouble("z"),
                                            Float.parseFloat(fc.getString("yaw")),
                                            Float.parseFloat(fc.getString("pitch")))));
                }
                File newFile = new File(f.getParentFile().getAbsolutePath() + File.separator + fc.getString("name") + ".yml");
                f.renameTo(newFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.YELLOW + "已将所有 Essentials 地标转换至正常状态.");
    }

    public void initWarps() {
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
                        String material = f.getString("Requirements.Material");
                        String name = f.getString("Requirements.ItemName");
                        List<String> lores = f.getStringList("Requirements.ItemLore");
                        Integer amount = f.getInt("Requirements.Amount");
                        if(Material.getMaterial(material) == null){
                            warnMessages.add(Main.Prefix + ChatColor.RED + "在定义 " + file.getName() + "时产生了错误: 定义的需求 Item 非法. 该地标已忽略.");
                            continue;
                        }
                        ItemStack is = new ItemStack(Material.getMaterial(material),amount);
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(name);
                        im.setLore(lores);
                        is.setItemMeta(im);
                        Warps.add(new ItemWarp());
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
                    case "null":
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
