package net.undergroundantics.magicantics.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SpellLearning implements Listener {

    public SpellLearning(MagicAntics plugin) {
        this.plugin = plugin;
    }

    private final MagicAntics plugin;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void learnSpellBook(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.HAND) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ItemStack spellBook = e.getPlayer().getInventory().getItemInMainHand();
                if (ItemRules.isSpellBook(spellBook)) {
                    e.setCancelled(true);
                    Spell spell = plugin.getSpellFromSpellBook(spellBook);
                    Player p = e.getPlayer();
                    if (!plugin.hasLearntSpell(p, spell)) {
                        plugin.learnSpell(p, spell);
                        spellBook.setAmount(spellBook.getAmount() - 1);
                        p.sendTitle(ChatColor.ITALIC + "" + ChatColor.GRAY + "New Spell Unlocked:", spell.getDisplayName(), 10, 40, 20);
                        p.getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 0.5f, 0.0f);
                        p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, p.getEyeLocation(), 50, 0.25, 0.25, 0.25, 1);
                    } else {
                        MagicAntics.sendMessage(p, ChatColor.GRAY + "You already know this spell.");
                    }

                }
            }
        }
    }
}
