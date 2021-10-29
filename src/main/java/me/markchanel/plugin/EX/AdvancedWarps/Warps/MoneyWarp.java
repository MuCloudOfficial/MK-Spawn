package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import me.markchanel.plugin.EX.AdvancedWarps.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MoneyWarp extends Warp<Double>{

    private double Amount;

    public MoneyWarp(String name, Location location, int amount){
        super(name,location,WarpType.MONEY_WARP);
        Amount = amount;
    }

    @Override
    public Double getRequirements() {
        return Amount;
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return (Main.eco.getBalance(target) >= Amount);
    }
}
