package me.markchanel.plugin.EX.AdvancedWarps;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class commands implements CommandExecutor {

    private final Main main;

    public commands(Main plugin){
        main = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] ss) {
        if(cmd.getName().equalsIgnoreCase("advancedwarps")){
            if(!(sender.isOp() &&
                    sender.hasPermission("advancedwarps.admin"))
                    ){
                sender.sendMessage(Main.Prefix + ChatColor.RED + "你没有权限执行此操作.");
                return true;
            }
            main.onReload();
            return true;
        }
        return false;
    }
}
