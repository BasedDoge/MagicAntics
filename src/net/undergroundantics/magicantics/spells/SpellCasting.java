package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.*;
import java.util.HashMap;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpellCasting implements Listener {

    public SpellCasting(MagicAntics plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCast(PlayerInteractEvent e) {
        if (ItemRules.SpellTomeCheck(e.getPlayer().getInventory().getItemInMainHand())) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);

                Player p = e.getPlayer();
                long currentTime = System.currentTimeMillis() / 1000;
                String activeSpell = "";

                if (p.getInventory().getItemInMainHand().getItemMeta().getLore().size() > 1) {
                    if (p.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).split("#").length > 0) {
                        activeSpell = ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0).split("#")[1]);
                    }
                }

                Long lastUseTime = (long) 0;

                //if the spell has been cast before (if a cooldown time exists)
                if (playerCooldown.containsKey(p.getUniqueId()) && playerCooldown.get(p.getUniqueId()).containsKey(activeSpell)) {
                    lastUseTime = playerCooldown.get(p.getUniqueId()).get(activeSpell);
                }

                //make sure hashmap has an entry for the player to avoid NPEs
                HashMap<String, Long> spellCooldown = new HashMap<>();
                spellCooldown.put(activeSpell, lastUseTime);
                playerCooldown.put(p.getUniqueId(), spellCooldown);

                //if the player is not on cooldown
                if (currentTime - lastUseTime > cooldownTime) {
                    Spell spell = plugin.getSpell(activeSpell);
                    if (spell != null) {
                        spell.cast(p);
                    }
                    spellCooldown.put(activeSpell, currentTime);
                    playerCooldown.put(p.getUniqueId(), spellCooldown);
                } //if the player is still on cooldown
                else if (currentTime - lastUseTime < cooldownTime) {
                    p.sendMessage("your spells are on cooldown for " + (cooldownTime - (currentTime - lastUseTime)) + " more seconds");
                }

            }
        }
    }
    
    //String = player, hashmap = <spell name, time in milliseconds since last cast>
    private HashMap<UUID, HashMap<String, Long>> playerCooldown = new HashMap<>();
    private static final int cooldownTime = 5;
    private final MagicAntics plugin;

}