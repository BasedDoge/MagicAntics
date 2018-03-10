package net.undergroundantics.magicantics.spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;

public class Fangs implements Spell {

    private static final String NAME = "Fangs";
    private static final String DISPLAY_NAME = "&7Fangs";
    private static final long COOLDOWN = 5;

    public Fangs(Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public long cast(Player p) {
        for (int i = 1; i < 9; i++) {
            Location l = p.getLocation().add(p.getLocation().getDirection().setY(0).normalize().multiply(i));
            l.setY(l.getWorld().getHighestBlockAt(l).getY());
            if (l.getY() >= p.getLocation().getY() - 4 && l.getY() <= p.getLocation().getY() + 4) {
                EvokerFangs f = (EvokerFangs) p.getWorld().spawnEntity(l, EntityType.EVOKER_FANGS);
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                    for (Entity ent : f.getNearbyEntities(0.5, 0.5, 0.5)){
                        if(ent instanceof Item && ent.getWorld() == p.getWorld()){
                            ent.teleport(p);
                            p.getWorld().spawnParticle(Particle.SPIT, p.getLocation(), 1, 0.2, 0.2, 0.2, 0.1);
                        }
                    }
                f.remove();
        }, 15);
            }
        }
        return COOLDOWN;
    }

    private final Plugin plugin;
}
