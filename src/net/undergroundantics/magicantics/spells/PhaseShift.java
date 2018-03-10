package net.undergroundantics.magicantics.spells;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PhaseShift implements Spell {

    private static final String NAME = "PhaseShift";
    private static final String DISPLAY_NAME = "&9PhaseShift";

    private static final int SUCCESS_COOLDOWN = 10;
    private static final int FAIL_COOLDOWN = 3;

    private static final int MAX_FALL = 3; // Max distance player should fall after phaseshift
    private static final int MAX_RANGE = 6; // Max distance player can phaseshift
    private static final int MAX_RANGE_SQ = MAX_RANGE * MAX_RANGE;

    @Override
    public long cast(Player player) {
        Location start = player.getLocation();
        Location target = new Location(start.getWorld(), start.getX(), start.getY(), start.getZ());
        Vector direction = start.getDirection().multiply(0.2);

        boolean foundWall = false;
        while (start.distanceSquared(target) <= MAX_RANGE_SQ) {
            if (foundWall) {
                if (isSafe(target)) {
                    player.teleport(target);
                    return SUCCESS_COOLDOWN;
                }
            } else {
                foundWall = target.getBlock().getType().isSolid();
            }
            target = target.add(direction);
        }

        return FAIL_COOLDOWN;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    private boolean isSafe(Location loc) {

        // Check for suffocation
        Block feet = (new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ())).getBlock();
        Block head = (new Location(loc.getWorld(), loc.getX(), loc.getY() + 2, loc.getZ())).getBlock();
        if (!feet.isEmpty() || !head.isEmpty()) {
            return false;
        }

        // Check for solid block not too far under feet
        Location landing = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
        int fall = 0;
        while (landing.getBlock().getY() > 0 && fall <= MAX_FALL) {
            if (landing.getBlock().isEmpty()) {
                fall++;
                landing.setY(landing.getY() - 1);
            } else {
                return landing.getBlock().getType().isSolid();
            }
        }
        return false;
    }
}
