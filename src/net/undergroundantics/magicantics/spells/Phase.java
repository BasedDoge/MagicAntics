package net.undergroundantics.magicantics.spells;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Phase implements Spell {

    private static final String NAME = "Phase";
    private static final String DISPLAY_NAME = "&9Phase";

    private static final int SUCCESS_COOLDOWN = 10;
    private static final int FAIL_COOLDOWN = 3;

    private static final int MAX_FALL = 3; // Max distance player should fall after phaseshift
    private static final int MAX_RANGE = 6; // Max distance player can phaseshift
    private static final int MAX_RANGE_SQ = MAX_RANGE * MAX_RANGE;

    @Override
    public long cast(Player player) {
        Location start = player.getEyeLocation();
        Location target = start.clone();
        Vector direction = start.getDirection().multiply(0.2);

        boolean foundWall = false;
        while (start.distanceSquared(target) <= MAX_RANGE_SQ) {
            if (foundWall) {
                Location trueTarget = target.getBlock().getRelative(0, -1, 0).getLocation();
                if (isSafe(trueTarget)) {
                    player.teleport(trueTarget);
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
