
package anticheat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class AntiCheatCommand implements CommandExecutor {

    private final AntiCheatPlugin plugin;

    public AntiCheatCommand(AntiCheatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args) {

        // FlightCheck test command
        if (args.length > 0 && args[0].equalsIgnoreCase("testflight")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("This command must be used by a player.");
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("anticheat.admin")) {
                player.sendMessage(ChatColor.RED + "You don't have permission!");
                return true;
            }

            boolean registered = false;

            for (org.bukkit.event.HandlerList list :
                    HandlerList.getHandlerLists()) {

                for (org.bukkit.plugin.RegisteredListener listener :
                        list.getRegisteredListeners()) {

                    if (listener.getListener() instanceof FlightCheck) {
                        registered = true;
                        break;
                    }
                }

                if (registered) break;
            }

            if (registered) {
                player.sendMessage(ChatColor.GREEN +
                        "FlightCheck is registered and listening!");
                player.sendMessage(ChatColor.YELLOW +
                        "Try testing movement in Survival mode.");
            } else {
                player.sendMessage(ChatColor.RED +
                        "FlightCheck is NOT registered!");
            }

            return true;
        }

        // Default AntiCheat menu
        sender.sendMessage(ChatColor.DARK_GRAY + "-------------------------");
        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "AntiCheat");
        sender.sendMessage(ChatColor.GRAY + "Version: 1.0");
        sender.sendMessage(ChatColor.GREEN + "Status: Active");
        sender.sendMessage(ChatColor.YELLOW + "Checks: Speed, Flight");
        sender.sendMessage(ChatColor.DARK_GRAY + "-------------------------");

        sender.sendMessage(ChatColor.GRAY + "Use /anticheat testflight");

        return true;
    }
}
