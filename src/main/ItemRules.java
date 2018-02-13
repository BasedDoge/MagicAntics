package main;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ItemRules {

    /*
    Used by events to check a player's Itemstack against these rules.
    This defines what a 'spell tome' is.
    */
    
    public Boolean SpellTomeCheck(ItemStack item) {
        Boolean isSpellTome = false;
        if (item != null) {
            if (item.getType() == Material.ENCHANTED_BOOK) {
                if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
                    isSpellTome = true;
                }
            }
        }
        return isSpellTome;
    }

    
    /*
    Used by events to check a player's Itemstack against these rules.
    This defines what a 'Incantation'/'spell sheet' is.
    */
    public Boolean SpellSheetCheck(ItemStack item) {
        Boolean isSpell = false;
        if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
            if (item.getType() == Material.PAPER) {
                isSpell = true;
            }
        }
        return isSpell;
    }
}
