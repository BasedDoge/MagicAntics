package net.undergroundantics.magicantics.spells;

import java.util.LinkedList;
import java.util.List;
import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Banner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Rally implements Spell {

    private static final String NAME = "Rally";
    private static final String DISPLAY_NAME = ChatColor.DARK_GREEN + NAME;
    private static final long COOLDOWN = 1;

    public Rally(Plugin plugin) {
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
        Location loc = p.getLocation();
        Banner b;
        loc.setY(loc.getY() - 1.85);
        ArmorStand marker = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        marker.setInvulnerable(true);
        marker.setMarker(true);
        marker.setVisible(false);
        marker.setGlowing(true);
        marker.setGravity(false);
        p.addPassenger(marker);
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 110, 0, true);
        PotionEffect res = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 110, 0, true);

        p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getEyeLocation(), 25, 5, 1, 5, 0);

        for (int i = 0; i < 40; i++) {
            List<Entity> ents = marker.getNearbyEntities(7, 4, 7);
            List<Entity> protectedEnts = getProtectables(ents, p);
            for (Entity ent : ents) {
                loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_CHIME, SoundCategory.AMBIENT, 0.5f, 0.0f);
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                    if (protectedEnts.contains(ent)) {
                        marker.getWorld().spawnParticle(Particle.CRIT_MAGIC, ent.getLocation().add(0, 1, 0), 3, 0.5, 0.5, 0.5, 0.1);
                        res.apply((LivingEntity) ent);
                        res.apply((LivingEntity) p);
                        speed.apply((LivingEntity) p);
                    }
                }, i * 10);
            }
        }

        marker.setHelmet(new ItemStack(Material.BANNER, 1));
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            marker.remove();
        }, 400);
        return true;
    }

    private List<Entity> getProtectables(List<Entity> ents, Player player) {
        List<Entity> protectedEnts = new LinkedList();
        for (Entity ent : ents) {
            if (ent instanceof Player) {
                protectedEnts.add(ent);
            } else if (ent instanceof Tameable) {
                Tameable an = (Tameable) ent;
                if (an.getOwner().getUniqueId() == player.getUniqueId()) {
                    protectedEnts.add(an);
                }
            }
        }
        return protectedEnts;
    }

    private final Plugin plugin;
}
