
package anticheat;

import org.bukkit.plugin.java.JavaPlugin;

public final class AntiCheatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(
                new SpeedCheck(this), this
        );

        getLogger().info("AntiCheat enabled!");

        getCommand("anticheat").setExecutor(
                new AntiCheatCommand()
        );


    }

    @Override
    public void onDisable() {
        getLogger().info("AntiCheat disabled.");
    }
}
