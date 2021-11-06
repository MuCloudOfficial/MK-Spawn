package me.markchanel.plugin.EX.AdvancedWarps.Warps;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemWarp extends Warp{

    private final ItemStack RequiredItem;

    public ItemWarp(String name, Location location, ItemStack requirements){
        super(name,location,WarpType.ITEM_WARP);
        RequiredItem = requirements;
    }

    public Integer getAmount(){
        return RequiredItem.getAmount();
    }

    @Override
    public Boolean checkHasRequirements(Player target) {
        return target.getInventory().contains(RequiredItem);
    }

    @Override
    public void teleportTo(Player target) {
        if(checkHasRequirements(target)){
            target.getInventory().removeItem(RequiredItem);
            target.teleport(getLocation());
            target.sendMessage("§6你已传送至地标 §e" + getName());
        }else{
            target.sendMessage("§4你未符合传送需求! 传送被拒绝.");
        }
    }

    // 将在未来版本使用的方法.
    private String getRequiredItemName(){ return RequiredItem.getItemMeta().getDisplayName(); }
    private List<String> getRequiredItemLore(){ return RequiredItem.getItemMeta().getLore(); }
    private Material getMaterial(){ return RequiredItem.getType(); }
    private ItemStack getRequirements() {
        return RequiredItem;
    }
    // -----------------------------------
}
