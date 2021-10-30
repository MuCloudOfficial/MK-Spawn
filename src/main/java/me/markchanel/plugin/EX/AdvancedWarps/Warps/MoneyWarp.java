package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import me.markchanel.plugin.EX.AdvancedWarps.Config;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MoneyWarp extends Warp{

    private double Amount;

    public MoneyWarp(String name, Location location, int amount){
        super(name,location,WarpType.MONEY_WARP);
        Amount = amount;
    }

    public Double getRequirements() {
        return Amount;
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return (Config.getEco().getBalance(target) >= Amount);
    }
}
