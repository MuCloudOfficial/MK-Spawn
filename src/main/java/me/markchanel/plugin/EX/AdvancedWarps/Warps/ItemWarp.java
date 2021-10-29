package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemWarp extends Warp<ItemStack>{

    private ItemStack RequiredItem;

    public ItemWarp(String name, Location location, ItemStack requirements){
        super(name,location,WarpType.ITEM_WARP);
        RequiredItem = requirements;
    }

    public String getRequiredItemName(){
        return RequiredItem.getItemMeta().getDisplayName();
    }

    public List<String> getRequiredItemLore(){
        return RequiredItem.getItemMeta().getLore();
    }

    public Integer getAmount(){
        return RequiredItem.getAmount();
    }

    public Material getMaterial(){
        return RequiredItem.getType();
    }

    @Override
    public ItemStack getRequirements() {
        return RequiredItem;
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        int amount = 0;
        boolean isQualified = false;
        for(ItemStack is : target.getInventory().getContents()){
            if(is == RequiredItem){
                amount = amount + is.getAmount();
            }
            if(amount >= getAmount()){
                isQualified = true;
            }
        }
        return isQualified;
    }

}
