package me.ponktacology.cavern.block.listener;

import me.ponktacology.cavern.block.CavernBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        CavernBlock cavernBlock = CavernBlock.getCavernBlock(event.getBlock().getLocation());

        cavernBlock.onBreak(event.getPlayer());
    }
}
