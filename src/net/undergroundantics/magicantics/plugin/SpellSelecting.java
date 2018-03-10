package net.undergroundantics.magicantics.plugin;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpellSelecting implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChangeActiveSpell(PlayerInteractEvent e) {
        if (ItemRules.SpellTomeCheck(e.getPlayer().getInventory().getItemInMainHand())) {
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                ItemStack spellTome = p.getInventory().getItemInMainHand();
                ItemMeta spellTomeMeta = spellTome.getItemMeta();

                String activeSpell = findSpellInLore(e.getPlayer(), spellTome);
                List<String> spellTomelore = spellTome.getItemMeta().getLore();
                spellTomelore.set(0, ChatColor.DARK_GRAY + "Active#" + activeSpell);
                spellTomeMeta.setLore(spellTomelore);
                spellTome.setItemMeta(spellTomeMeta);

                p.sendTitle("", ChatColor.STRIKETHROUGH + "--"
                        + ChatColor.BOLD + ">"
                        + ChatColor.RESET + "  "
                        + ChatColor.ITALIC
                        + activeSpell, 10, 20, 10);
                p.playSound(e.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
            }
        }
    }

    public String findSpellInLore(Player p, ItemStack tome) {
        boolean found = false;
        String foundSpell = "";
        String activeSpell = "";
        int spellIndex = 1;
        

        if (tome.getItemMeta().getLore().get(0).split("#").length > 1) {
            activeSpell = tome.getItemMeta().getLore().get(0).split("#")[1];
        } else if (tome.getItemMeta().getLore().size() > 1) {
            activeSpell = tome.getItemMeta().getLore().get(1);
        }
        
        while (found == false && spellIndex < tome.getItemMeta().getLore().size()) {
            if (tome.getItemMeta().getLore().get(spellIndex).contains(activeSpell)) {
                found = true;
                if (spellIndex < tome.getItemMeta().getLore().size() - 1) {
                    foundSpell = tome.getItemMeta().getLore().get(spellIndex + 1);

                } else if (tome.getItemMeta().getLore().size() > 1) {
                    foundSpell = tome.getItemMeta().getLore().get(1);
                }
            }
            spellIndex++;
        }
        return foundSpell;
    }
}