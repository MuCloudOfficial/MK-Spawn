package me.markchanel.plugin.EX.AdvancedWarps;

import me.markchanel.plugin.EX.AdvancedWarps.Warps.WarpPool;
import me.markchanel.plugin.EX.AdvancedWarps.Warps.WarpType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {

    private Main main;
    private WarpPool pool;

    public Commands(Main plugin){
        main = plugin;
    }

    private void sendHelpPage(CommandSender sender){
        sender.sendMessage("§c§lEX§7§l-§e§lAdvancedWarps");
        sender.sendMessage("§6作者: &7&lMark_Chanel");
        sender.sendMessage("§6插件主页: §b https://gitee.com/markchanel/ex-advancedwarps");
        sender.sendMessage("§7§m------------------§b§l命令概述§7§m------------------");
        sender.sendMessage("§6 /aw info        显示插件描述及帮助                     ");
        sender.sendMessage("§6 /warps          显示当前所有地标                       ");
        sender.sendMessage("§7§m---------------------------------------------------");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] ss) {
        if(cmd.getName().equalsIgnoreCase("advancedwarps") || cmd.getName().equalsIgnoreCase(s)){
            if(ss.length == 1){
                if(ss[0].equalsIgnoreCase("info")){
                    sendHelpPage(sender);
                    return true;
                }
                if(ss[0].equalsIgnoreCase("reload")){
                    main.onReload();
                    sender.sendMessage(Main.Prefix + "§a插件加载完毕");
                    return true;
                }
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("warps") || cmd.getName().equalsIgnoreCase(s)){
            if(pool.listWarpsName().size() == 0){
                sender.sendMessage("&6当前无地标");
                return true;
            }
            if(sender.isOp() || sender.hasPermission("advancedwarps.admin")){
                List<String> message = new ArrayList<>();
                if(pool.listHasRequirementWarpsName(WarpType.NORMAL).size() == 0){
                    sender.sendMessage("&6当前无普通地标");
                    return true;
                }else{
                    message.add("§6普通地标:");
                    String names = pool.listHasRequirementWarpsName(WarpType.NORMAL).toString();
                    message.add(names.substring(1,names.length()-2));
                }
                if(pool.listHasRequirementWarpsName(WarpType.MONEY_WARP).size() == 0){
                    message.add("&6当前无需要金钱的地标");
                }else{
                    message.add("§6需要金钱的地标:");
                    String names = pool.listHasRequirementWarpsName(WarpType.NORMAL).toString();
                    message.add(names.substring(1,names.length()-2));
                }
                if(pool.listHasRequirementWarpsName(WarpType.PERMISSION_WARP).size() == 0){
                    message.add("&6当前无需要权限的地标");
                }else{
                    message.add("§6需要权限的地标:");
                    String names = pool.listHasRequirementWarpsName(WarpType.NORMAL).toString();
                    message.add(names.substring(1,names.length()-2));
                }
                if(pool.listHasRequirementWarpsName(WarpType.ITEM_WARP).size() == 0){
                    message.add("&6当前无需要物品的地标");
                }else{
                    message.add("§6需要物品的地标:");
                    String names = pool.listHasRequirementWarpsName(WarpType.NORMAL).toString();
                    message.add(names.substring(1,names.length()-2));
                }
                message.add(0,"&6当前地标列表:");
                for(String messages : message){
                    sender.sendMessage(messages);
                }
            }else{
                String names = pool.listWarpsName().toString();
                sender.sendMessage("§6当前地标列表:");
                sender.sendMessage(names.substring(1,names.length()-2));
            }
            return true;
        }
        return false;
    }
}
