package me.mangomilktea.plugin.EX.AdvancedWarps.Warps;

import com.earth2me.essentials.Essentials;
import me.mangomilktea.plugin.EX.AdvancedWarps.Config;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class MoneyWarp extends Warp{

    private final double Amount;

    public MoneyWarp(String name, Location location,int coolingDown, int amount){
        super(name,location,coolingDown,WarpType.MONEY_WARP);
        Amount = amount;
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return Config.getEco().getBalance(target) >= Amount;
    }

    @Override
    public void teleportTo(Player target) {
        if(!target.hasPermission("essentials.warps." + getName())) {
            target.sendMessage("§4你没有权限执行此操作");
            return;
        }
        if(WarpPool.getCoolingDown(target)){
            target.sendMessage("§4传送冷却中.  请稍后再试.");
            return;
        }
        if(target.hasPermission("exaw.teleportwarp.bypass." + getName())){
            target.teleport(getLocation());
            target.sendMessage("§6你已传送至地标 §e" + getName());
            WarpPool.addCoolingDown(target,getCoolingDown());
            return;
        }
        if(checkHasRequirements(target)){
            IUser targetU = Essentials.getPlugin(Essentials.class).getUser(target);
            targetU.takeMoney(BigDecimal.valueOf(Amount));
            target.teleport(getLocation());
            target.sendMessage("§6你已传送至地标 §e" + getName());
            WarpPool.addCoolingDown(target,getCoolingDown());
        }else{
            target.sendMessage("§4你未符合传送需求! 传送被拒绝.");
        }
    }

    // 为后续版本准备.
    private Double getRequirements() {
        return Amount;
    }
}
