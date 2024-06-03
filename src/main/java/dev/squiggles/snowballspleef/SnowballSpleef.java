package dev.squiggles.snowballspleef;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnowballSpleef implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("snowballspleef");
    @Override
    public void onInitialize() {
        LOGGER.info("Mod loaded");

        SnowballBreakableBlocksCommand.getBreakableBlocks().addAll(BlockListStorage.loadBlockList());

        CommandRegistrationCallback.EVENT.register(SnowballBreakableBlocksCommand::register);
    }
}
