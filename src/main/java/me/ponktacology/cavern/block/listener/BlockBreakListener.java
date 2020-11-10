package me.ponktacology.cavern.block.listener;

import me.ponktacology.cavern.block.CavernBlock;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        CavernBlock cavernBlock = CavernBlock.getCavernBlock(event.getBlock().getLocation());

        if (cavernBlock != null) {
            event.setCancelled(true);
            event.getBlock().breakNaturally();
            cavernBlock.onBreak(event.getPlayer());
        }
    }
}
