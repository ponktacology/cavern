package me.ponktacology.cavern.block;

import me.ponktacology.simpleconfig.config.annotation.Configurable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CavernBlock {

    @Configurable
    public static int timeToRegen = 20;

    private static final Map<Location, CavernBlock> cavernBlocks = new HashMap<>();

    private final JavaPlugin plugin;
    private final Location location;
    private final Material defaultMaterial;

    public CavernBlock(JavaPlugin plugin, Location location, Material defaultMaterial) {
        this.plugin = plugin;
        this.location = location;
        this.defaultMaterial = defaultMaterial;
    }

    public static Set<CavernBlock> getCavernBlocks() {
        return new HashSet<>(cavernBlocks.values());
    }

    public static void addBlock(CavernBlock cavernBlock) {
        cavernBlocks.put(cavernBlock.getLocation(), cavernBlock);
    }

    public static void removeBlock(CavernBlock cavernBlock) {
        cavernBlocks.remove(cavernBlock.getLocation());
    }

    public static CavernBlock getCavernBlock(Location location) {
        return cavernBlocks.get(location);
    }

    public void regenerate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Block block = location.getBlock();

                block.setType(defaultMaterial);
            }
        }.runTaskLater(plugin, timeToRegen);
    }

    public void onBreak(Player player) {
        Block block = location.getBlock();
        block.setType(Material.BEDROCK);

        /*
           Give random reward
         */
        regenerate();
    }

    public void save() {

    }

    public static void loadAll() {

    }

    public Location getLocation() {
        return location;
    }

    public Material getDefaultMaterial() {
        return defaultMaterial;
    }


}
