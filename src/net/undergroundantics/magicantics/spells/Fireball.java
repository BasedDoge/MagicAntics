package net.undergroundantics.magicantics.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Fireball implements Spell {

    private static final String NAME = "Fireball";
    private static final String DISPLAY_NAME = "&cFireball";
    private static final long COOLDOWN = 1;

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
        SmallFireball fireblast = p.launchProjectile(SmallFireball.class);
        fireblast.setShooter(p);
        fireblast.setIsIncendiary(false);
        fireblast.setVelocity(p.getLocation().getDirection().multiply(2));
        p.getWorld().spawnParticle(Particle.LAVA, p.getEyeLocation().subtract(0, 0.3, 0), 3, 0.1, 0.1, 0.1, 0.1);
        return COOLDOWN;
    }

}
