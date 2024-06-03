package dev.squiggles.snowballspleef;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Set;

public class SnowballBreakableBlocksCommand {

    private static final Set<Block> breakableBlocks = BlockListStorage.loadBlockList();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("snowballbreakable")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("block", StringArgumentType.string())
                                .executes(context -> {
                                    String blockId = StringArgumentType.getString(context, "block");
                                    Block block = Registries.BLOCK.get(new Identifier(blockId));
                                    if (block != Registries.BLOCK.get(new Identifier("this_does_not_exist"))) {
                                        breakableBlocks.add(block);
                                        BlockListStorage.saveBlockList(breakableBlocks);
                                        context.getSource().sendFeedback(() -> Text.literal("Added block " + blockId + " to breakable list"), true);
                                    } else {
                                        context.getSource().sendError(Text.literal("Invalid block ID: " + blockId));
                                    }
                                    return 1;
                                })))
                .then(CommandManager.literal("list")
                        .executes(context -> {
                            StringBuilder blockList = new StringBuilder("Breakable blocks: ");
                            for (Block block : breakableBlocks) {
                                blockList.append(Registries.BLOCK.getId(block).toString()).append(", ");
                            }
                            context.getSource().sendFeedback(() -> Text.literal(blockList.toString()), false);
                            return 1;
                        }))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("block", StringArgumentType.string())
                                .executes(context -> {
                                    String blockId = StringArgumentType.getString(context, "block");
                                    Block block = Registries.BLOCK.get(new Identifier(blockId));
                                    if (breakableBlocks.remove(block)) {
                                        BlockListStorage.saveBlockList(breakableBlocks);
                                        context.getSource().sendFeedback(() -> Text.literal("Removed block " + blockId + " from breakable list"), true);
                                    } else {
                                        context.getSource().sendError(Text.literal("Block " + blockId + " is not in the breakable list"));
                                    }
                                    return 1;
                                }))));
    }

    public static Set<Block> getBreakableBlocks() {
        return breakableBlocks;
    }
}
