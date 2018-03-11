package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.Spell;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Thunderstorm implements Spell {

    private static final String NAME = "Thunderstorm";
    private static final String DISPLAY_NAME = ChatColor.YELLOW + NAME;
    private static final long COOLDOWN = 45;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public long getCooldown() {
        return COOLDOWN;
    }

    @Override
    public boolean isLearnable() {
        return true;
    }

    @Override
    public boolean cast(Player p) {
        boolean castSuccess = false;
        List<Entity> localMobsStorm = new LinkedList();
        for (Entity ent : p.getNearbyEntities(6, 2, 6)) {
            if (ent instanceof LivingEntity) {
                localMobsStorm.add((LivingEntity) ent);
            }
        }
        for (Entity mob : localMobsStorm) {
            if (mob instanceof LivingEntity && p.hasLineOfSight(mob)) {
                mob.getWorld().strikeLightning(mob.getLocation().add(0, 1, 0));
            }
        }
        if (localMobsStorm.isEmpty()) {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, SoundCategory.PLAYERS, 0.5f, 2.0f);
        } else {
            p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, p.getEyeLocation(), 50, 4, 1, 4, 0);
            castSuccess = true;
        }
        return castSuccess;
    }

}
