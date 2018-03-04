package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.MagicAntics;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Icicle implements Spell {

    private static final String NAME = "Icicle";
    private static final String DISPLAY_NAME = "&bIcicle";
    private static final long COOLDOWN = 1;
    
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
    
    public Icicle(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void cast(Player p) {
    Snowball icicle = p.launchProjectile(Snowball.class);
    icicle.setShooter(p);
    icicle.setMetadata(NAME, new FixedMetadataValue(plugin, MagicAntics.NAME));
    icicle.setVelocity(p.getLocation().getDirection().multiply(3));
    p.getWorld().spawnParticle(Particle.SNOW_SHOVEL, p.getEyeLocation().subtract(0, 0.3, 0), 3, 0.1, 0.1, 0.1, 0.1);
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
        Entity p = e.getEntity();
        p.getWorld().spawnParticle(Particle.SNOW_SHOVEL, p.getLocation(), 10, 0.2, 0.2, 0.2, 0.2);
        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.5f, 2.0f);
        if (e.getHitEntity() instanceof LivingEntity) {
            PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 60, 1, true);
            slow.apply((LivingEntity) e.getHitEntity());
        }
    }

    private final Plugin plugin;
}
