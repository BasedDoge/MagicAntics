package net.undergroundantics.magicantics.spells;

import java.util.List;
import net.undergroundantics.magicantics.plugin.MagicAntics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Stasis implements Spell {

    private static final String NAME = "Stasis";
    private static final String DISPLAY_NAME = ChatColor.DARK_BLUE + NAME;
    private static final long COOLDOWN = 60;

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

    public Stasis(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean cast(Player p) {
        boolean castSuccess = false;
        List<Entity> localMobsStasis = p.getNearbyEntities(6, 2, 6);
        for (Entity e : localMobsStasis) {
            if (e instanceof LivingEntity) {
                ShulkerBullet stasisProj = p.launchProjectile(ShulkerBullet.class);
                stasisProj.setMetadata(NAME, new FixedMetadataValue(plugin, MagicAntics.NAME));
                stasisProj.setVelocity(stasisProj.getVelocity().multiply(2));
                stasisProj.setShooter(p);
                stasisProj.setTarget(e);
                stasisProj.setBounce(true);
            }
        }
        if (localMobsStasis.isEmpty()) {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, SoundCategory.PLAYERS, 0.5f, 2.0f);
        } else {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.PLAYERS, 0.5f, 2.0f);
            castSuccess = true;
        }
        return castSuccess;
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
        Entity p = e.getEntity();
        PotionEffect stasis = new PotionEffect(PotionEffectType.LEVITATION, 20, 2, true);
        p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation(), 10, 1, 1, 1, 0);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 2.0f);
        if (e.getHitEntity() instanceof LivingEntity) {
            for (int i = 0; i < 120; i++) {
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                    e.getHitEntity().playEffect(EntityEffect.HURT);
                    stasis.apply((LivingEntity) e.getHitEntity());
                }, i);
            }
        }
    }

    private final Plugin plugin;

}
