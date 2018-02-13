package spells;

import main.ItemRules;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
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

    ItemRules MAIR = new ItemRules();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChangeActiveSpell(PlayerInteractEvent e) {
        if (MAIR.SpellTomeCheck(e.getPlayer().getInventory().getItemInMainHand())) {
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.setCancelled(true);
                ItemStack spellTome = e.getPlayer().getInventory().getItemInMainHand();
                ItemMeta spellTomeMeta = spellTome.getItemMeta();

                String activeSpell = findSpellInLore(e.getPlayer(), spellTome);
                List<String> spellTomelore = spellTome.getItemMeta().getLore();
                spellTomelore.set(0, ChatColor.DARK_GRAY + "Active#" + activeSpell);
                spellTomeMeta.setLore(spellTomelore);
                spellTome.setItemMeta(spellTomeMeta);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
            }
        }
    }

    public String findSpellInLore(Player p, ItemStack tome) {
        Boolean found = false;
        String foundSpell = "";
        int spellIndex = 1;
        String activeSpell = "";

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

                } else {
                    foundSpell = "None";
                }
            }
            spellIndex++;
        }
        return foundSpell;
    }
}
