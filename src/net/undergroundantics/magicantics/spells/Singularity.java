package net.undergroundantics.magicantics.spells;

import static java.lang.Math.sqrt;
import java.util.Collection;
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
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class Singularity implements Spell {

    private static final String NAME = "Singularity";
    private static final String DISPLAY_NAME = ChatColor.BLACK + NAME;
    private static final long COOLDOWN = 23;
    private static final int DURATION = 32; //in quarters of seconds
    private static final int DRAG_RANGE = 12;
    private static final int EXPULSION_RANGE = 5;

    public Singularity(Plugin plugin) {
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
        ProjectileSource shooter = e.getEntity().getShooter();
        Location loc = e.getEntity().getLocation();
        for (int i = 0; i < DURATION; i++) {
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                loc.getWorld().spawnParticle(Particle.PORTAL, loc, 45, 0.5, 0.5, 0.5, 3);
                loc.getWorld().playSound(loc, Sound.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.5f, 0.0f);
                for (Entity ent : loc.getWorld().getNearbyEntities(loc, DRAG_RANGE, DRAG_RANGE, DRAG_RANGE)) {
                    if (ent != shooter){
                        ent.setVelocity(drag(loc, ent).multiply(1.5));
                    }
                }
            }, i * 5);
        }
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            Collection<Entity> ents = loc.getWorld().getNearbyEntities(loc, EXPULSION_RANGE, EXPULSION_RANGE, EXPULSION_RANGE);
            for (Entity ent : ents) {
                if (ent != shooter){
                    ent.setVelocity(drag(loc, ent).multiply(15));
                }
            }
        }, DURATION * 5);
    }

    private Vector drag(Location loc, Entity ent) {
        double d0 = loc.getX() - ent.getLocation().getX();
        double d1 = loc.getY() - ent.getLocation().getY();
        double d2 = loc.getZ() - ent.getLocation().getZ();
        double d3 = (double) sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        Vector v = new Vector(d0 * 0.1D, d1 * 0.1D + (double) sqrt(d3) * 0.08D, d2 * 0.1D);
        return v;
    }

    private final Plugin plugin;
}
