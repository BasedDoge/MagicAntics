package spells;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ProjectileEffects implements Listener {

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata("IceBolt")) {
            Entity p = e.getEntity();
            p.getWorld().spawnParticle(Particle.SNOW_SHOVEL, e.getHitEntity().getLocation(), 10, 0.2, 0.2, 0.2, 0.2);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.5f, 2.0f);
            if (e.getHitEntity() instanceof LivingEntity) {
                PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 60, 1, true);
                slow.apply((LivingEntity) e.getHitEntity());
            }

        }

    }
}