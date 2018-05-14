package net.undergroundantics.magicantics.spells;

import java.util.LinkedList;
import java.util.List;
import net.undergroundantics.magicantics.plugin.MagicAntics;
import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class Impact implements Spell {

    private static final String NAME = "Imapact";
    private static final String DISPLAY_NAME = ChatColor.YELLOW + NAME;
    private static final long COOLDOWN = 18;

    @Override
    public boolean cast(Player p) {
        boolean castSuccess = true;
        boolean isGround = false;
        int height = 0;
        while (isGround == false) {
            if (p.getLocation().subtract(0, height, 0).getBlock().getType() == Material.AIR && height < 255) {
                height++;
            } else {
                isGround = true;
                SpectralArrow arrow = p.launchProjectile(SpectralArrow.class);
                arrow.setShooter(p);
                arrow.setMetadata(NAME, new FixedMetadataValue(plugin, MagicAntics.NAME));
                p.setFallDistance(-((height / 5) * 4));
                arrow.addPassenger(p);
                arrow.setVelocity(new Vector(0, -(height / 2), 0));
            }
        }
        return castSuccess;
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
        Location loc = (e.getHitEntity() == null) ? e.getHitBlock().getLocation() : e.getHitEntity().getLocation();
        e.getEntity().remove();
        List<Entity> localMobsImpact = new LinkedList();
        for (Entity ent : loc.getWorld().getNearbyEntities(loc, 4, 2, 4)) {
            if (ent instanceof LivingEntity) {
                localMobsImpact.add((LivingEntity) ent);
            }
        }
        for (Entity mob : localMobsImpact) {
            mob.setVelocity(mob.getVelocity().add(new Vector(0, 1, 0)));
        }
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

    public Impact(Plugin plugin) {
        this.plugin = plugin;
    }

    private final Plugin plugin;
}
