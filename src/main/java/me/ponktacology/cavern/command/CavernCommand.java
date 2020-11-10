package me.ponktacology.cavern.command;

import me.ponktacology.cavern.block.CavernBlock;
import me.ponktacology.cavern.util.ColorUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Set;

public class CavernCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public CavernCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.color("&cYou can only use this in game."));
            return false;
        }

        if (args.length != 1) {
            Arrays.asList("Cavern Help",
                    "/cavern list - List of all cavern blocks",
                    "/cavern add - Adds block you are currently looking at to the cavern",
                    "/cavern remove - Removes block you are currently looking from the cavern")
                    .forEach(it -> sender.sendMessage(ColorUtil.color(it)));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "list": {
                sender.sendMessage("List of all cavern blocks:");
                CavernBlock.getCavernBlocks().forEach(it -> {
                    sender.sendMessage("Loc: " + it.getLocation().toString() + " Mat: " + it.getDefaultMaterial());
                });
                break;
            }
            case "add": {
                Block block = ((Player) sender).getTargetBlock((Set<Material>) null, 6);

                if (block == null) {
                    sender.sendMessage(ColorUtil.color("&cYou must be looking at the block."));
                    return false;
                }

                Location location = block.getLocation();
                CavernBlock cavernBlock = CavernBlock.getCavernBlock(location);

                if (cavernBlock != null) {
                    sender.sendMessage(ColorUtil.color("&cThis block is already added to the cavern blocks."));
                    return false;
                }

                cavernBlock = new CavernBlock(plugin, location, block.getType());
                cavernBlock.save();
                CavernBlock.addBlock(cavernBlock);

                sender.sendMessage(ColorUtil.color("&aSuccessfully added this block to cavern blocks."));
                break;
            }
            case "remove": {
                Block block = ((Player) sender).getTargetBlock((Set<Material>) null, 6);

                if (block == null) {
                    sender.sendMessage(ColorUtil.color("&cYou must be looking at the block."));
                    return false;
                }

                Location location = block.getLocation();
                CavernBlock cavernBlock = CavernBlock.getCavernBlock(location);

                if (cavernBlock == null) {
                    sender.sendMessage(ColorUtil.color("&cThis block is not the cavern block."));
                    return false;
                }

                cavernBlock.remove();
                CavernBlock.removeBlock(cavernBlock);

                sender.sendMessage(ColorUtil.color("&aSuccessfully removed this block from the cavern blocks."));
                break;
            }
            default: {
                Arrays.asList("Cavern Help",
                        "/cavern list - List of all cavern blocks",
                        "/cavern add - Adds block you are currently looking at to the cavern",
                        "/cavern remove - Removes block you are currently looking from the cavern")
                        .forEach(it -> sender.sendMessage(ColorUtil.color(it)));
                break;
            }
        }
        return false;
    }
}
