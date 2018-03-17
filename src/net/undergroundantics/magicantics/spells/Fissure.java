package net.undergroundantics.magicantics.spells;

import static java.lang.Math.sqrt;
import net.undergroundantics.magicantics.plugin.MagicAntics;
import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class Fissure implements Spell {

    private static final String NAME = "Fissure";
    private static final String DISPLAY_NAME = ChatColor.BLACK + NAME;
    private static final long COOLDOWN = 23;

    public Fissure(Plugin plugin) {
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
    public long getCooldown() {
        return COOLDOWN;
    }

    @Override
    public boolean isLearnable() {
        return true;
    }

    @Override
    public boolean cast(Player p) {
        boolean castSuccess = true;
        LlamaSpit proj = p.launchProjectile(LlamaSpit.class);
        proj.setShooter(p);
        proj.setMetadata(NAME, new FixedMetadataValue(plugin, MagicAntics.NAME));
        proj.setVelocity(p.getLocation().getDirection().multiply(3));
        p.getWorld().spawnParticle(Particle.PORTAL, p.getEyeLocation().subtract(0, 0.3, 0), 15, 0.1, 0.1, 0.1, 1);
        return castSuccess;
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
        Location loc = e.getEntity().getLocation();
        for (int i = 0; i < 32; i++) {
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                loc.getWorld().spawnParticle(Particle.PORTAL, loc, 25, 0.1, 0.1, 0.1, 1.2);
                loc.getWorld().playSound(loc, Sound.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.5f, 0.0f);
                for (Entity ent : e.getEntity().getNearbyEntities(13, 13, 13)) {
                double d0 = loc.getX() - ent.getLocation().getX();
                double d1 = loc.getY() - ent.getLocation().getY();
                double d2 = loc.getZ() - ent.getLocation().getZ();
                double d3 = (double) sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                Vector v = new Vector(d0 * 0.1D, d1 * 0.1D + (double) sqrt(d3) * 0.08D, d2 * 0.1D);
                ent.setVelocity(v.multiply(1.5));
                }
            }, i * 5);
        }
    }
    private final Plugin plugin;
}
