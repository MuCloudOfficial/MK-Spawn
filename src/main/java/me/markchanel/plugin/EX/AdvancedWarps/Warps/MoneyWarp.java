package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.markchanel.plugin.EX.AdvancedWarps.Config;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class MoneyWarp extends Warp{

    private final double Amount;

    public MoneyWarp(String name, Location location, int amount){
        super(name,location,WarpType.MONEY_WARP);
        Amount = amount;
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return Config.getEco().getBalance(target) >= Amount;
    }

    @Override
    public void teleportTo(Player target) {
        if(checkHasRequirements(target)){
            IUser targetU = new User(target,new Essentials());
            targetU.takeMoney(BigDecimal.valueOf(Amount));
            target.teleport(getLocation());
            target.sendMessage("§6你已传送至地标 §e" + getName());
        }else{
            target.sendMessage("§4你未符合传送需求! 传送被拒绝.");
        }
    }

    // 为后续版本准备.
    private Double getRequirements() {
        return Amount;
    }
}
