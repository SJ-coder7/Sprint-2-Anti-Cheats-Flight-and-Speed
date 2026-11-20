
package anticheat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AntiCheatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args)
    {

        sender.sendMessage(ChatColor.DARK_GRAY + "-------------------------");
        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "AntiCheat");
        sender.sendMessage(ChatColor.GRAY + "Version: 1.0");
        sender.sendMessage(ChatColor.GREEN + "Status: Active");
        sender.sendMessage(ChatColor.YELLOW + "Checks: Not yet implemented");
        sender.sendMessage(ChatColor.DARK_GRAY + "-------------------------");

        return true;
    }
}
