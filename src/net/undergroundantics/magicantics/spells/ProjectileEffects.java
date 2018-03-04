package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.MagicAntics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileEffects implements Listener {
    
    public ProjectileEffects(MagicAntics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        for (Spell spell : plugin.getSpells()) {
            if (e.getEntity().hasMetadata(spell.getName())) {
                spell.onHit(e);
            }
        }
    }

    private final MagicAntics plugin;

}
