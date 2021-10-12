package me.markchanel.plugin.EX.AdvancedWarps;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class commands implements CommandExecutor {

    private final Main main;

    public commands(Main plugin){
        main = plugin;
    }

    public void sendHelpPage(CommandSender sender){
        sender.sendMessage(Main.Prefix + ChatColor.AQUA + "EX-AdvancedWarps       " + ChatColor.YELLOW + "Ver.1.0.Beta.206");
        sender.sendMessage(Main.Prefix + ChatColor.AQUA + "Author: Mark_Chanel");
        sender.sendMessage(Main.Prefix + ChatColor.AQUA + "插件主页: https://gitee.com/markchanel/ex-advancedwarps");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] ss) {
        if(cmd.getName().equalsIgnoreCase("advancedwarps")){
            if(ss.length < 2){
                sendHelpPage(sender);
            }
            if(ss[0].equalsIgnoreCase("reload")) {
                if (!(sender.isOp() &&
                        sender.hasPermission("advancedwarps.admin"))){
                    sender.sendMessage(Main.Prefix + ChatColor.RED + "你没有权限执行此操作.");
                    return true;
                }
                main.onReload();
                return true;
            }
            if(ss[0].equalsIgnoreCase("warps")){
                List<String> warpPage = new ArrayList<>();
                StringBuilder result = new StringBuilder();
                for(String s1 : Config.Warps.keySet()){
                    result.append(s1).append(",");
                }
                warpPage.add(String.valueOf(result));
                if(Config.RequiredItemWarps.size() == 0){
                    warpPage.add(ChatColor.GOLD + "需要物品的传送点: " + "无");
                }else{
                    warpPage.add(ChatColor.GOLD + "需要物品的传送点: ");
                    for(Map.Entry<String, List<Object>> entry1 : Config.RequiredItemWarps.entrySet()){

                    }
                }
                if(Config.RequiredMoneyWarps.size() == 0){
                    warpPage.add(ChatColor.GOLD + "需要金钱的传送点: " + "无");
                }else{
                    warpPage.add(ChatColor.GOLD + "需要金钱的传送点: ");
                    for(Map.Entry<String, Double> entry2 : Config.RequiredMoneyWarps.entrySet()){

                    }
                }
                if(Config.RequiredPermissionWarps.size() == 0){
                    warpPage.add(ChatColor.GOLD + "需要权限的传送点: " + "无");
                }else{
                    warpPage.add(ChatColor.GOLD + "需要权限的传送点: ");
                    for(Map.Entry<String,String> entry3 : Config.RequiredPermissionWarps.entrySet()){

                    }
                }
                for(String pageLine : warpPage){
                    sender.sendMessage(pageLine);
                }
                return true;
            }
        }
        return false;
    }
}
