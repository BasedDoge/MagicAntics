package net.undergroundantics.magicantics.spells;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

public class Fireball implements Spell {

    private static final String NAME = "Fireball";
    private static final String DISPLAY_NAME = ChatColor.RED + NAME;
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
    public long getCooldown() {
        return COOLDOWN;
    }

    @Override
    public boolean cast(Player p) {
        boolean castSuccess = true;
        SmallFireball fireblast = p.launchProjectile(SmallFireball.class);
        fireblast.setShooter(p);
        fireblast.setIsIncendiary(false);
        fireblast.setVelocity(p.getLocation().getDirection().multiply(2));
        p.getWorld().spawnParticle(Particle.LAVA, p.getEyeLocation().subtract(0, 0.3, 0), 3, 0.1, 0.1, 0.1, 0.1);
        return castSuccess;
    }

}
