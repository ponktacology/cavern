package me.ponktacology.cavern;

import me.ponktacology.cavern.block.CavernBlock;
import me.ponktacology.cavern.block.listener.BlockBreakListener;
import me.ponktacology.cavern.command.CavernCommand;
import me.ponktacology.simpleconfig.config.ConfigFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Cavern extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new ConfigFactory(this.getClass());
        getCommand("cavern").setExecutor(new CavernCommand(this));
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);

        CavernBlock.loadAll(this);
    }
}
