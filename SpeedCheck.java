
package anticheat;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SpeedCheck implements Listener {

    private static final double MAX_SPEED = 8.0;
    private static final int REQUIRED_VIOLATIONS = 2;

    private final JavaPlugin plugin;
    private final Map<UUID, Location> previousLocations =
            new HashMap<>();
    private final Map<UUID, Integer> violations =
            new HashMap<>();

    public SpeedCheck(JavaPlugin plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskTimer(
                plugin,
                this::checkPlayers,
                5L,
                5L
        );
    }

    private void checkPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID id = player.getUniqueId();
            Location current = player.getLocation();
            Location previous = previousLocations.put(
                    id, current.clone()
            );

            if (previous == null
                    || !previous.getWorld().equals(current.getWorld())
                    || isExempt(player)) {
                violations.remove(id);
                continue;
            }

            double dx = current.getX() - previous.getX();
            double dz = current.getZ() - previous.getZ();

            // Horizontal distance traveled in this sample.
            double distance = Math.sqrt(dx * dx + dz * dz);

            // Five ticks at 20 TPS is approximately 0.25 seconds.
            double speed = distance / 0.25;

            if (speed > MAX_SPEED) {
                int count = violations.merge(id, 1, Integer::sum);

                if (count >= REQUIRED_VIOLATIONS) {
                    alertAdmins(player, speed);
                }
            } else {
                violations.remove(id);
            }
        }
    }

    private boolean isExempt(Player player) {
        return player.getGameMode() == GameMode.CREATIVE
                || player.getGameMode() == GameMode.SPECTATOR
                || player.isFlying()
                || player.isInsideVehicle()
                || player.isGliding()
                || player.isSwimming()
                || player.isDead()
                || player.hasPotionEffect(PotionEffectType.SPEED);
    }

    private void alertAdmins(Player player, double speed) {
        String message = String.format(
                "[AntiCheat] %s triggered Speed Check (%.1f blocks/sec)",
                player.getName(), speed
        );

        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (admin.hasPermission("anticheat.alerts")) {
                admin.sendMessage("§c" + message);
            }
        }

        plugin.getLogger().info(message);

        // Avoid spamming alerts continuously.
        violations.put(player.getUniqueId(), 0);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        previousLocations.put(
                player.getUniqueId(), player.getLocation()
        );
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        previousLocations.put(
                player.getUniqueId(), event.getTo()
        );
        violations.remove(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        previousLocations.remove(id);
        violations.remove(id);
    }
}
