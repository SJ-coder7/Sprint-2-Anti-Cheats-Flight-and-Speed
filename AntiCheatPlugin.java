
package anticheat;

import org.bukkit.plugin.java.JavaPlugin;

public final class AntiCheatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        // Register speed detection
        getServer().getPluginManager().registerEvents(
                new SpeedCheck(this), this
        );

        // Register flight detection
        getServer().getPluginManager().registerEvents(
                new FlightCheck(this), this
        );

        getLogger().info("AntiCheat enabled!");
        getLogger().info("SpeedCheck enabled!");
        getLogger().info("FlightCheck enabled!");

        getCommand("anticheat").setExecutor(
                new AntiCheatCommand()
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiCheat disabled.");
    }
}
