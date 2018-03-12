package net.undergroundantics.magicantics.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SpellCasting implements Listener {

    public SpellCasting(MagicAntics plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCast(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if ( e.getHand() == EquipmentSlot.HAND ) {
                Player p = e.getPlayer();
                if (ItemRules.isSpellTome(item)) {
                    if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE)
                        return;

                    e.setCancelled(true);
                    Spell spell = plugin.getActiveSpell(item);
                    if (spell == null)
                        return;
                    if ( ! plugin.hasLearntSpell(p, spell) ) 
                        return;

                    CooldownKey key = new CooldownKey(p, spell);
                    Long unlockTime = cooldowns.get(key);
                    long currentTime = System.currentTimeMillis() / 1000;

                    if (unlockTime == null || currentTime >= unlockTime) {
                        // The player is not on cooldown
                        long cooldown = spell.cast(p) ? spell.getCooldown() : spell.getCooldown() / 10;
                        cooldowns.put(key, currentTime + cooldown);
                    } else {
                        // The player is on cooldown
                        MagicAntics.sendMessage(p,
                                "You must wait (" + ChatColor.GRAY + (unlockTime - currentTime) + ChatColor.RESET + 
                                "s) before casting " + ChatColor.translateAlternateColorCodes('&', spell.getDisplayName()) + 
                                ChatColor.RESET + " again.");
                    }
                } else if (ItemRules.isSpellScroll(item)) {
                    Spell spell = plugin.getSpellFromSpellScroll(item);
                    if (spell != null) {
                        if (spell.cast(p))
                            item.setAmount(item.getAmount() - 1);
                    }
                }
            }
            
        }
    }

    private class CooldownKey {
        public CooldownKey(Player player, Spell spell) {
            this.player = player.getUniqueId();
            this.spellName = spell.getName();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof CooldownKey) {
                CooldownKey c = (CooldownKey) o;
                return player.equals(c.player) && spellName.equals(c.spellName);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, spellName);
        }
    
        private final UUID player;
        private final String spellName;
    }

    // Stores the time when the cooldown resets
    private final Map<CooldownKey, Long> cooldowns = new HashMap<>();
    private final MagicAntics plugin;

}