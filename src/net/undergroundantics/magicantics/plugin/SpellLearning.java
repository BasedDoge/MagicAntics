package net.undergroundantics.magicantics.plugin;

import net.undergroundantics.magicantics.spells.Spell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpellLearning implements Listener {

    public SpellLearning(MagicAntics plugin) {
        this.plugin = plugin;
    }

    private final MagicAntics plugin;

    @EventHandler(priority = EventPriority.HIGH)
    public void learnSpellBook(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack spellBook = e.getPlayer().getInventory().getItemInMainHand();
            if (ItemRules.isSpellBook(spellBook)) {
                e.setCancelled(true);
                Spell spell = plugin.getSpellFromDisplayName(spellBook.getItemMeta().getLore().get(0));
                Player p = e.getPlayer();
                if ( ! plugin.hasLearntSpell(p, spell) ) {
                    plugin.learnSpell(p, spell);
                    spellBook.setAmount(spellBook.getAmount() - 1);
                    p.getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 0.5f, 2.0f);
                    p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, p.getEyeLocation(), 50, 0.75, 0.5, 0.75, 1);
                } else {
                    // TODO play a sound or something
                }

            }
        }
    }
}
