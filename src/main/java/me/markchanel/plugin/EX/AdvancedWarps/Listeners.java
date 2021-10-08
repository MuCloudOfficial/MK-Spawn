package me.markchanel.plugin.EX.AdvancedWarps;

import net.ess3.api.IUser;
import net.ess3.api.events.UserWarpEvent;
import net.essentialsx.api.v2.events.WarpModifyEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Listeners implements Listener {

    @EventHandler
    private void WarpCommandListener(UserWarpEvent uwe) throws NullPointerException {
        uwe.setCancelled(true);
        String target = uwe.getWarp();
        IUser targetU = uwe.getUser();
        Player targetP = targetU.getBase();
        Location targetLoc = Config.Warps.get(target);
        if (Config.Warps.containsKey(target)) {
            if(Config.RequiredPermissionWarps.containsKey(target)){
                if(!targetP.hasPermission(Config.RequiredPermissionWarps.get(target))){
                    targetU.sendMessage(Main.Prefix + ChatColor.RED + "你没有权限传送至目标地");
                    return;
                }
                teleportTo(targetP,targetLoc);
            }

            if(!targetP.hasPermission("essentials.warps."+target)){
                targetU.sendMessage(Main.Prefix + ChatColor.RED + "你没有权限传送至目标地");
                return;
            }

            if (Config.RequiredItemWarps.containsKey(target)) {
                List<Object> itemInfo = Config.RequiredItemWarps.get(target);
                boolean qualified = false;
                int amount = 0;
                for(ItemStack is : targetP.getInventory().getContents()){
                    if(is.getType().name().equals(itemInfo.get(0)) &&
                            is.getItemMeta().getDisplayName().equals(itemInfo.get(1)) &&
                            is.getItemMeta().getLore().equals(itemInfo.get(2))){
                        amount = amount + is.getAmount();
                        if(amount == ((Integer) itemInfo.get(3))){
                            qualified = true;
                        }
                    }
                }
                if(!qualified){
                    targetP.sendMessage(Main.Prefix + ChatColor.RED + "你未持有必需的物品或当前持有的必需物品数不足以允许你传送至目标地");
                    return;
                }else{
                    teleportTo(targetP,targetLoc);
                }
            }

            if (Config.RequiredMoneyWarps.containsKey(target)) {
                if (targetU.getMoney().doubleValue() < Config.RequiredMoneyWarps.get(target)) {
                    targetP.sendMessage(Main.Prefix + ChatColor.RED + "你没有足够的金钱传送至目标地");
                    return;
                }
                teleportTo(targetP,targetLoc);
            }
            teleportTo(targetP,targetLoc);
        }
    }

    @EventHandler
    private void WarpModifyListener(WarpModifyEvent wme) throws NullPointerException {
        FileConfiguration f = new YamlConfiguration();
        wme.setCancelled(true);
        IUser target = wme.getUser();
        Player targetP = target.getBase();
        Location targetLoc = wme.getNewLocation();
        String targetN = wme.getWarpName();
        switch (wme.getCause()){
            case CREATE:
                Config.Warps.put(wme.getWarpName(),wme.getNewLocation());
                File targetCreateFile = new File(Config.EssC.getConfigFile().getParentFile().getAbsolutePath() + File.separator + "warps" + File.separator + targetN + ".yml");
                try {
                    f.load(targetCreateFile);
                    f.set("world",targetLoc.getWorld().getUID());
                    f.set("world-name",targetLoc.getWorld().getName());
                    f.set("x",targetLoc.getX());
                    f.set("y",targetLoc.getY());
                    f.set("z",targetLoc.getZ());
                    f.set("yaw",targetLoc.getYaw());
                    f.set("pitch",targetLoc.getPitch());
                    f.set("name",targetN);
                    f.set("lastowner",targetP.getUniqueId());
                    f.save(targetCreateFile);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                Config.refreshWarpFiles();
                target.sendMessage(Main.Prefix + ChatColor.GREEN + "你创建了一个新地标 " + targetN);
                break;
            case DELETE:
                Config.Warps.remove(targetN);
                Config.getWarpFile(targetN).delete();
                Config.refreshWarpFiles();
                target.sendMessage(Main.Prefix + ChatColor.YELLOW + "你删除了一个地标 " + targetN);
                break;
            case UPDATE:
                Config.Warps.replace(wme.getWarpName(),wme.getNewLocation());
                File targetUpdateFile = Config.getWarpFile(wme.getWarpName());
                try {
                    f.load(targetUpdateFile);
                    f.set("world",targetLoc.getWorld().getUID());
                    f.set("world-name",targetLoc.getWorld().getName());
                    f.set("x",targetLoc.getX());
                    f.set("y",targetLoc.getY());
                    f.set("z",targetLoc.getZ());
                    f.set("yaw",targetLoc.getYaw());
                    f.set("pitch",targetLoc.getPitch());
                    f.set("lastowner",wme.getUser().getBase().getUniqueId());
                    f.save(targetUpdateFile);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                target.sendMessage(Main.Prefix + ChatColor.YELLOW + "你更新了" + targetN);
                break;
        }
    }

    public void teleportTo(Player player,Location target){
        player.teleport(target);
        player.sendMessage(Main.Prefix + ChatColor.YELLOW + "你已被传送.");
    }

}
