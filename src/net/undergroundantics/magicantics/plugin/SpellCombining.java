package net.undergroundantics.magicantics.plugin;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpellCombining implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void addSpellToTome(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (ItemRules.SpellTomeCheck(e.getPlayer().getInventory().getItemInOffHand()) && ItemRules.SpellSheetCheck(e.getPlayer().getInventory().getItemInMainHand())) {
                e.setCancelled(true);

                ItemStack spellTome = e.getPlayer().getInventory().getItemInOffHand();
                ItemStack spellSheet = e.getPlayer().getInventory().getItemInMainHand();
                ItemMeta spellSheetMeta = spellSheet.getItemMeta();
                ItemMeta spellTomeMeta = spellTome.getItemMeta();
                List<String> tomeSpellsList = spellTomeMeta.getLore();

                if (!tomeSpellsList.contains(spellSheetMeta.getLore().get(0))){
                    tomeSpellsList.add(spellSheetMeta.getLore().get(0));
                    spellSheet.setAmount(spellSheet.getAmount() - 1);
                    spellTomeMeta.setLore(tomeSpellsList);
                    spellTome.setItemMeta(spellTomeMeta);
                    if (spellTomeMeta.getLore().size() == 1){
                        spellTomeMeta.getLore().set(0, ChatColor.DARK_GRAY + "Active#" + spellTomeMeta);
                    }
                    e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 0.5f, 2.0f);
                    e.getPlayer().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, e.getPlayer().getEyeLocation(), 50, 0.75, 0.5, 0.75, 1);
                }
            }
        }
    }
}
