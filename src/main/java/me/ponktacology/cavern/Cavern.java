package me.ponktacology.cavern;

import me.ponktacology.cavern.block.listener.BlockBreakListener;
import me.ponktacology.cavern.command.CavernCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Cavern extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("cavern").setExecutor(new CavernCommand(this));
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
