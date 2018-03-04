package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.MagicAntics;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Stasis implements Spell {

    private static final String NAME = "Stasis";
    private static final String DISPLAY_NAME = "&eStasis";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }
    
    public Stasis(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void cast(Player p) {
        LlamaSpit stasisProj = p.launchProjectile(LlamaSpit.class);
        stasisProj.setShooter(p);
        stasisProj.setMetadata(NAME, new FixedMetadataValue(plugin, MagicAntics.NAME));
        stasisProj.setVelocity(p.getLocation().getDirection().multiply(3));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.PLAYERS, 0.5f, 2.0f);
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
        Entity p = e.getEntity();
        p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation(), 10, 1, 1, 1, 0);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 2.0f);
        if (e.getHitEntity() instanceof LivingEntity) {
            PotionEffect lev = new PotionEffect(PotionEffectType.LEVITATION, 20, 1, true);
            for (int i = 0; i < 60; i++){
                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    public void run() {
                        lev.apply((LivingEntity) e.getHitEntity());
                        e.getHitEntity().playEffect(EntityEffect.HURT);
                    }
                }, i);
            }
        }
    }

    private final Plugin plugin;

}
