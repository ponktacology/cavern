package me.ponktacology.cavern.block;

import me.ponktacology.simpleconfig.config.annotation.Configurable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CavernBlock {

    @Configurable
    public static int timeToRegen = 20;

    private static final Map<Location, CavernBlock> cavernBlocks = new HashMap<>();
    private static final Map<String, Integer> cavernRewards = new HashMap<>();

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
        block.setType(Material.BEDROCK, true);

        regenerate();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), getRandomReward().replace("{player}", player.getName()));
    }

    public void remove() {
        FileConfiguration configuration = plugin.getConfig();
        configuration.set("data." + locationToString(this.getLocation()), null);
        plugin.saveConfig();
    }

    public void save() {
        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection section = configuration.getConfigurationSection("data." + locationToString(this.getLocation()));

        if (section == null) {
            section = configuration.createSection("data." + locationToString(this.getLocation()));
        }

        section.set("defaultMaterial", defaultMaterial.toString());

        plugin.saveConfig();
    }

    public static void loadAll(JavaPlugin plugin) {
        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection section = configuration.getConfigurationSection("data");

        if (section != null) {
            Map<String, Object> sections = section.getValues(false);

            sections.keySet().forEach(it -> {
                cavernBlocks.put(locationFromString(it), new CavernBlock(plugin, locationFromString(it), Material.valueOf(section.getString(it + ".defaultMaterial"))));
            });
        }

        loadRewards(plugin);
    }

    private String getRandomReward() {
        double sum = cavernRewards.values().stream().mapToDouble(d -> d).sum();
        double rand = Math.random() * sum;
        String choice = "";

        for (String e : cavernRewards.keySet()) {
            choice = e;
            rand -= cavernRewards.get(e);
            if (rand < 0) {
                break;
            }
        }

        return choice;
    }

    private static void loadRewards(JavaPlugin plugin) {
        FileConfiguration configuration = plugin.getConfig();
        List<String> rewards = configuration.getStringList("rewards");

        if (rewards != null && !rewards.isEmpty()) {
            rewards.forEach(s -> {
                String[] args = s.split(";");
                cavernRewards.put(args[0], Integer.valueOf(args[1]));
            });
        }
    }

    public Location getLocation() {
        return location;
    }

    public Material getDefaultMaterial() {
        return defaultMaterial;
    }

    private static String locationToString(Location location) {
        return (location.getWorld().getName() + "@" + location.getX() + "@" + location.getY() + "@" + location.getZ()).replace(".", "*");
    }

    private static Location locationFromString(String location) {
        String[] args = location.replace("*", ".").split("@");
        return new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
    }
}
