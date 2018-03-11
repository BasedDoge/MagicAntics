package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Phase implements Spell {

    private static final String NAME = "Phase";
    private static final String DISPLAY_NAME = ChatColor.BLUE + NAME;

    private static final int COOLDOWN = 10;

    private static final int MAX_FALL = 3; // Max distance player should fall after phaseshift
    private static final int MAX_RANGE = 6; // Max distance player can phaseshift
    private static final int MAX_RANGE_SQ = MAX_RANGE * MAX_RANGE;

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
    public boolean cast(Player player) {
        Location start = player.getEyeLocation();
        Location target = start.clone();
        Vector direction = start.getDirection().multiply(0.2);

        boolean foundWall = false;
        while (start.distanceSquared(target) <= MAX_RANGE_SQ) {
            if (foundWall) {
                Location trueTarget = target.getBlock().getRelative(0, -1, 0).getLocation();
                trueTarget.setX(trueTarget.getX() + 0.5);
                trueTarget.setZ(trueTarget.getZ() + 0.5);
                trueTarget.setPitch(start.getPitch());
                trueTarget.setYaw(start.getYaw());
                if (isSafe(trueTarget)) {
                    player.teleport(trueTarget);
                    player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getEyeLocation(), 15, 0.5, 1, 0.5, 0.0);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 0.0f);
                    return true;
                }
            } else {
                foundWall = target.getBlock().getType().isSolid();
            }
            target = target.add(direction);
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, SoundCategory.PLAYERS, 0.5f, 2.0f);
        return false;
    }

    private boolean isSafe(Location loc) {
        // Check for suffocation
        Block feet = loc.getBlock();
        Block head = feet.getRelative(0, +1, 0);
        if (!feet.isEmpty() || !head.isEmpty()) {
            return false;
        }

        // Check for solid block not too far under feet
        Block landing = feet.getRelative(0, -1, 0);
        int fall = 0;
        while (landing.getY() > 0 && fall <= MAX_FALL) {
            if (landing.isEmpty()) {
                fall++;
                landing = landing.getRelative(0, -1, 0);
            } else {
                return landing.getType().isSolid();
            }
        }
        return false;
    }
}
