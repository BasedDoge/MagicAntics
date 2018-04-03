package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.MagicAntics;
import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class Familiar implements Spell {

    private static final String NAME = "Familiar";
    private static final String DISPLAY_NAME = ChatColor.GREEN + NAME;
    private static final long COOLDOWN = 40;

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

    public Familiar(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean cast(Player p) {
        boolean castSuccess = true;
        Egg dogEgg = p.launchProjectile(Egg.class);
        dogEgg.setShooter(p);
        dogEgg.setVelocity(p.getLocation().getDirection().multiply(3));
        dogEgg.setMetadata(NAME, new FixedMetadataValue(plugin, MagicAntics.NAME));
        return castSuccess;
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
        Location spawnLoc = (e.getHitBlock() == null) ? e.getHitEntity().getLocation() : e.getHitBlock().getLocation().add(0, 1, 0);
        Wolf wolf = (Wolf) e.getEntity().getWorld().spawnEntity(spawnLoc, EntityType.WOLF);
        wolf.setOwner((Player) e.getEntity().getShooter());
        wolf.setHealth(20);
        wolf.setCustomName(ChatColor.GREEN + "Wolf Familiar");
        wolf.setCollarColor(DyeColor.GREEN);

        if (e.getHitEntity() instanceof LivingEntity) {
            wolf.setTarget((LivingEntity) e.getHitEntity());
        }

        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            wolf.getWorld().spawnParticle(Particle.CLOUD, wolf.getLocation(), 10, 0.2, 0.2, 0.2, 0.2);
            wolf.remove();
        }, 500);
    }

    private final Plugin plugin;
}
