package me.markchanel.plugin.EX.AdvancedWarps;

import com.earth2me.essentials.Settings;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.*;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class Config {

    private final Main main;
    private static Economy eco = null;
    private static ISettings EssC;
    private static WarpPool pool = new WarpPool();
    private static String Version;

    public Config(Main plugin){
        main = plugin;
    }

    protected void initConfig(){
        checkEssentials();
        checkVault();
        EssC = new Settings((IEssentials) Objects.requireNonNull(main.getServer().getPluginManager().getPlugin("Essentials")));
        pool.openPool(main);
        convertEssWarps();
        loadWarps();
        Version();
    }

    private void Version(){
        try {
            InputStream is = main.getClass().getClassLoader().getResourceAsStream("plugin.yml");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int length;
            while((length = is.read(buf)) != -1){
                baos.write(buf,0,length);
            }
            is.close();
            baos.close();
            LineNumberReader lnr = new LineNumberReader(new CharArrayReader(baos.toString("UTF-8").toCharArray()));
            Version = ((String) lnr.lines().toArray()[6]).substring(9);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkEssentials(){
        if(!main.getServer().getPluginManager().isPluginEnabled("Essentials")){
            main.getServer().getPluginManager().disablePlugin(main);
            throw new RuntimeException(Main.Prefix + ChatColor.RED + "Essentials Not found! Plugin Disabling.");
        }
        main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.GRAY + "已侦测到 Essentials.");
    }

    private void checkVault(){
        if(!main.getServer().getPluginManager().isPluginEnabled("Vault")){
            return;
        }
        RegisteredServiceProvider<Economy> rsp = main.getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
            return;
        }
        eco = rsp.getProvider();
    }

    private void convertEssWarps(){
        FileConfiguration fc = new YamlConfiguration();
        if(pool.listWarpsFile().length == 0){
            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.YELLOW + "当前无地标可记录.");
            return;
        }
        for(File f : pool.listWarpsFile()){
            try {
                fc.load(f);
                if(fc.get("Requirement") != null){
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
    }

    private void loadWarps(){
        FileConfiguration fc = new YamlConfiguration();
        for(File f : pool.listWarpsFile()){
            try {
                fc.load(f);
                if(fc.get("name") == null){
                    main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 基本 warp 数据");
                    continue;
                }
                if(fc.get("x") == null ||
                        fc.get("y") == null ||
                        fc.get("z") == null ||
                        fc.get("yaw") == null ||
                        fc.get("pitch") == null){
                    main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 基本 warp 数据");
                    continue;
                }
                if(fc.get("Requirement.Type") == null){
                    main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 需求类型   已转换为普通地标.");
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
                if(fc.get("CoolingDown") == null){
                    main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 地标传送冷却时间   已设置为默认(3s).");
                    fc.set("CoolingDown",3);
                    fc.save(f);
                }
                int coolingDown = fc.getInt("CoolingDown");
                switch (fc.getString("Requirement.Type")){
                    case "NORMAL":
                        Warp warp = new NormalWarp(warpName,l,coolingDown);
                        pool.add(warp);
                        break;
                    case "MONEY":
                        if(fc.get("Requirement.Amount") == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 需求数据   已跳过.");
                            break;
                        }
                        int amount = fc.getInt("Requirement.Amount");
                        Warp warp1 = new MoneyWarp(warpName,l,coolingDown,amount);
                        pool.add(warp1);
                        break;
                    case "PERMISSION":
                        String permission = fc.getString("Requirement.Permission");
                        if(permission == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 需求数据   已跳过.");
                            break;
                        }
                        Warp warp2 = new PermissionWarp(warpName,l,coolingDown,permission);
                        pool.add(warp2);
                        break;
                    case "ITEM":
                        String item_name;
                        List<String> item_lore;
                        int item_amount;
                        if(fc.get("Requirement.Material") == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 需求数据   已跳过.");
                            break;
                        }
                        Material m = Material.getMaterial(fc.getString("Requirement.Material"));
                        if(m == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 需求数据   已跳过.");
                            break;
                        }
                        if(fc.get("Requirement.Amount") == null){
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 需求数据   已跳过.");
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
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 需求数据   已忽略.");
                        }
                        if(fc.get("Requirement.Lore") != null && fc.isList("Requirement.Lore")){
                            item_lore = fc.getStringList("Requirement.Lore");
                            im.setLore(item_lore);
                            is.setItemMeta(im);
                        }else{
                            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 需求数据   已忽略.");
                        }
                        Warp warp3 = new ItemWarp(warpName,l,coolingDown,is);
                        pool.add(warp3);
                        break;
                    default:
                        main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + f.getName() + " 加载错误: 需求类型   已跳过.");
                        break;
                }
                if(fc.get("Signs") != null){
                    List<Location> list = (List<Location>) fc.getList("Signs");
                    for(Location loc : list){
                        pool.addSignWarp(loc,pool.getWarp(warpName));
                    }
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

    public static String getVersion(){
        return Version;
    }

    protected void reload(){
        pool.clearPool();
        checkEssentials();
        checkVault();
        loadWarps();
    }

    protected void clearConfig(){
        pool.clearPool();
    }
}
