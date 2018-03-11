package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Fangs implements Spell {

    private static final String NAME = "Fangs";
    private static final String DISPLAY_NAME = ChatColor.GRAY + NAME;
    private static final long COOLDOWN = 5;
    private static final int RANGE = 9;
    private static final int VRANGE = 4;

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
    public boolean isLearnable() {
        return true;
    }

    @Override
    public long getCooldown() {
        return COOLDOWN;
    }

    @Override
    public boolean cast(Player p) {
        boolean castSuccess = true;
        for (int i = 1; i < RANGE; i++) {
            int failCount = 0;
            Location l = p.getLocation().add(p.getLocation().getDirection().setY(0).normalize().multiply(i));
            l.setY(l.getWorld().getHighestBlockAt(l).getY());
            if (l.getY() >= p.getLocation().getY() - VRANGE && l.getY() <= p.getLocation().getY() + VRANGE) {
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
            else {
                failCount++;
                if (failCount == RANGE){
                    castSuccess = false;
                }
            }
        }
        return castSuccess;
    }

    private final Plugin plugin;
}
