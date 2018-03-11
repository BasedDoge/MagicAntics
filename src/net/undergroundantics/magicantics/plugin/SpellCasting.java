package net.undergroundantics.magicantics.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.undergroundantics.magicantics.spells.Spell;
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
        ItemStack book = e.getPlayer().getInventory().getItemInMainHand();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (ItemRules.isSpellTome(book) && e.getHand() == EquipmentSlot.HAND) {
                if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE)
                    return;
                
                e.setCancelled(true);
                Player p = e.getPlayer();
                Spell spell = plugin.getActiveSpell(book);
                if (spell == null)
                    return;
                if ( ! plugin.hasLearntSpell(p, spell) ) 
                    return;
                
                CooldownKey key = new CooldownKey(p, spell);
                Long unlockTime = cooldowns.get(key);
                long currentTime = System.currentTimeMillis() / 1000;
                
                if (unlockTime == null || currentTime >= unlockTime) {
                    // The player is not on cooldown
                    long cooldown = spell.cast(p);
                    cooldowns.put(key, currentTime + cooldown);
                } else {
                    // The player is on cooldown
                    MagicAntics.sendMessage(p,
                            "You must wait (" + ChatColor.GRAY + (unlockTime - currentTime) + ChatColor.RESET + 
                            "s) before casting " + ChatColor.translateAlternateColorCodes('&', spell.getDisplayName()) + 
                            ChatColor.RESET + " again.");
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