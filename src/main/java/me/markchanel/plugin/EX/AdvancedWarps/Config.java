package me.markchanel.plugin.EX.AdvancedWarps;

import com.earth2me.essentials.Settings;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.*;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Config {

    private final Main main;
    private static Economy eco = null;
    private static ISettings EssC;
    private final WarpPool pool = new WarpPool();

    public Config(Main plugin){
        main = plugin;
        pool.openPool();
    }

    public void initConfig(){
        checkEssentials();
        checkVault();
        EssC = new Settings((IEssentials) Objects.requireNonNull(main.getServer().getPluginManager().getPlugin("Essentials")));
        convertEssWarps();
    }

    public void checkEssentials(){
        if(!main.getServer().getPluginManager().isPluginEnabled("Essentials")){
            main.getServer().getPluginManager().disablePlugin(main);
            throw new RuntimeException(Main.Prefix + ChatColor.RED + "Essentials Not found! Plugin Disabling.");
        }
        main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.GRAY + "已侦测到 Essentials.");
    }

    public void checkVault(){
        if(!main.getServer().getPluginManager().isPluginEnabled("Vault")){
            main.getServer().getPluginManager().disablePlugin(main);
            throw new RuntimeException(Main.Prefix + ChatColor.RED + "Vault Not found! Plugin Disabling.");
        }
        RegisteredServiceProvider<Economy> rsp = main.getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
            main.getServer().getPluginManager().disablePlugin(main);
            throw new RuntimeException(Main.Prefix + ChatColor.RED + "Economy Module Not found! Plugin Disabling.");
        }
        eco = rsp.getProvider();
    }

    public void convertEssWarps(){
        FileConfiguration fc = new YamlConfiguration();
        if(pool.listWarpsFile().length == 0){
            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.YELLOW + "当前无地标可记录.");
            return;
        }
        for(File f : pool.listWarpsFile()){
            try {
                fc.load(f);
                if(fc.get("Requirement") == null){
                    continue;
                }
                if(fc.get("name") != null){
                    fc.set("Requirement.Type","Normal");
                    fc.save(f);
                    File newFile = new File(f.getParentFile().getAbsolutePath() + File.separator + fc.getString("name") + ".yml");
                    f.renameTo(newFile);
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        loadWarps();
    }

    private void loadWarps(){
        FileConfiguration fc = new YamlConfiguration();
        for(File f : pool.listWarpsFile()){
            try {
                fc.load(f);
                if(fc.get("name") == null){
                    main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 基本 warp 数据");
                    continue;
                }
                if(fc.get("x") == null ||
                        fc.get("y") == null ||
                        fc.get("z") == null ||
                        fc.get("yaw") == null ||
                        fc.get("pitch") == null){
                    main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 基本 warp 数据");
                    continue;
                }
                if(fc.get("Requirement.Type") == null){
                    main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 需求类型   已转换为普通地标.");
                    fc.set("Requirement.Type","NORMAL");
                    fc.save(f);
                }
                String warpName = fc.getString("name");
                Location l = new Location(
                        main.getServer().getWorld(fc.getString("world-name")),
                        fc.getDouble("x"),
                        fc.getDouble("y"),
                        fc.getDouble("z"),
                        (float)fc.getDouble("yaw"),
                        (float)fc.getDouble("pitch")
                );
                switch (fc.getString("Requirement.Type")){
                    case "NORMAL":
                        Warp warp = new NormalWarp(warpName,l);
                        pool.add(warp);
                        break;
                    case "MONEY":
                        if(fc.get("Requirement.Amount") == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 需求数据   已跳过.");
                            break;
                        }
                        int amount = fc.getInt("Requirement.Amount");
                        Warp warp1 = new MoneyWarp(warpName,l,amount);
                        pool.add(warp1);
                        break;
                    case "PERMISSION":
                        String permission = fc.getString("Requirement.Permission");
                        if(permission == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 需求数据   已跳过.");
                            break;
                        }
                        Warp warp2 = new PermissionWarp(warpName,l,permission);
                        pool.add(warp2);
                        break;
                    case "ITEM":
                        String item_name;
                        List<String> item_lore;
                        int item_amount;
                        if(fc.get("Requirement.Material") == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 需求数据   已跳过.");
                            break;
                        }
                        Material m = Material.getMaterial(fc.getString("Requirement.Material"));
                        if(m == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 需求数据   已跳过.");
                            break;
                        }
                        if(fc.get("Requirement.Amount") == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 需求数据   已跳过.");
                            break;
                        }

                        item_amount = fc.getInt("Requirement.Amount");

                        ItemStack is = new ItemStack(m,item_amount);
                        ItemMeta im = is.getItemMeta();

                        if(fc.get("Requirement.Name") != null){
                            item_name = fc.getString("Requirement.Name");
                            im.setDisplayName(item_name);
                            is.setItemMeta(im);
                        }else{
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 需求数据   已忽略.");
                        }
                        if(fc.get("Requirement.Lore") != null && fc.isList("Requirement.Lore")){
                            item_lore = fc.getStringList("Requirement.Lore");
                            im.setLore(item_lore);
                            is.setItemMeta(im);
                        }else{
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 需求数据   已忽略.");
                        }
                        Warp warp3 = new ItemWarp(warpName,l,is);
                        pool.add(warp3);
                        break;
                    default:
                        main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + "加载错误: 需求类型   已跳过.");
                        break;
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public static Economy getEco() {
        return eco;
    }

    public static ISettings getEssC() {
        return EssC;
    }

    public void reload(){
        pool.clearPool();
        loadWarps();
    }

    public void clearConfig(){
        pool.clearPool();
    }
}
