package net.undergroundantics.magicantics.spells;

import java.util.LinkedList;
import java.util.List;
import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
        marker.setGravity(false);
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 100, 1, true);
        PotionEffect res = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1, true);

        for (int i = 0; i < 10; i++) {
            List<Entity> ents = p.getNearbyEntities(7, 4, 7);
            for (Entity ent : getProtectables(ents)) {
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                    res.apply((LivingEntity) ent);
                    speed.apply((LivingEntity) ent);
                }, i * 10);
            }
        }
        
        marker.setHelmet(new ItemStack(Material.BANNER, 1));
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            marker.remove();
        }, 200);
        return true;
    }

    private List<Entity> getProtectables(List<Entity> ents) {
        List<Entity> protectedEnts = new LinkedList();
        for (Entity ent : ents) {
            if (ent instanceof Player) {
                protectedEnts.add(ent);
            } else if (ent instanceof Tameable) {
                Tameable an = (Tameable) ent;
                for (Entity ps : protectedEnts) {
                    if (ps instanceof Player) {
                        if (an.getOwner().getUniqueId() == ps.getUniqueId()) {
                            protectedEnts.add(an);
                        }
                    }
                }
            }
        }
        return protectedEnts;
    }

    private final Plugin plugin;
}
