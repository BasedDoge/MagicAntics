package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpellCasting implements Listener {

    public SpellCasting(MagicAntics plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCast(PlayerInteractEvent e) {
        ItemStack book = e.getPlayer().getInventory().getItemInMainHand();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
            if (ItemRules.SpellTomeCheck(book)) {
                e.setCancelled(true);

                Player p = e.getPlayer();
                long currentTime = System.currentTimeMillis() / 1000;
                
                Spell spell;
                List<String> lore = book.getItemMeta().getLore();
                if (lore.size() > 1) {
                   spell = plugin.getSpell(ChatColor.stripColor(lore.get(0).split("#")[1]));
                } else {
                    // Empty book
                    return;
                }
                
                Long cooldownTime = spell.getCooldown();
                CooldownKey key = new CooldownKey(p, spell);
                Long lastUseTime = cooldowns.get(key);

                if (lastUseTime == null || currentTime - lastUseTime > cooldownTime) {
                    // The player is not on cooldown
                    spell.cast(p);
                    cooldowns.put(key, currentTime);
                } else {
                    // The player is on cooldown
                    MagicAntics.sendMessage(p, "Your spells are on cooldown for " + (cooldownTime - (currentTime - lastUseTime)) + " more seconds");
                }

            }
        }
    
    private class CooldownKey {
        public CooldownKey(Player player, Spell spell) {
            this.player = player.getUniqueId();
            this.spellName = spell.getName();
        }
        private UUID player;
        private String spellName;
    }

    private final Map<CooldownKey, Long> cooldowns = new HashMap<>();
    private final MagicAntics plugin;

}