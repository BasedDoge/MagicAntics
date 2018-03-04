package net.undergroundantics.magicantics.spells;

import java.util.List;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Thunderstorm implements Spell {

    private static final String NAME = "Thunderstorm";
    private static final String DISPLAY_NAME = "&eThunderstorm";
    private static final long COOLDOWN = 5;
    
    @Override
    public long getCooldown() {
        return COOLDOWN;
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
    public void cast(Player p) {
        List<Entity> localMobsStorm = p.getNearbyEntities(6, 2, 6);
        for (Entity mob : localMobsStorm) {
            if (mob instanceof LivingEntity && p.hasLineOfSight(mob)) {
                mob.getWorld().strikeLightning(mob.getLocation().add(0, 1, 0));
            }
        }
        if (localMobsStorm.size() > 0) {
            p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, p.getEyeLocation(), 50, 4, 1, 4, 0);
        } else {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, SoundCategory.PLAYERS, 0.5f, 2.0f);
        }
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
    }

}
