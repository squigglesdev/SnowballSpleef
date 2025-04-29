package dev.squiggles.snowballspleef;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
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
    private static Boolean ignitesTNT = BlockListStorage.loadIgnitesTnt();
    private static Boolean damagePlayers = BlockListStorage.loadDamagePlayers();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("snowball")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("block", StringArgumentType.string())
                                .executes(context -> {
                                    String blockId = StringArgumentType.getString(context, "block");
                                    Block block = Registries.BLOCK.get(Identifier.of(blockId));
                                    if (block != Registries.BLOCK.get(Identifier.of("this_does_not_exist"))) {
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
                            StringBuilder blockList = new StringBuilder("The current breakable blocks are: ");
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
                                    Block block = Registries.BLOCK.get(Identifier.of(blockId));
                                    if (breakableBlocks.remove(block)) {
                                        BlockListStorage.saveBlockList(breakableBlocks);
                                        context.getSource().sendFeedback(() -> Text.literal("Removed block " + blockId + " from breakable list"), true);
                                    } else {
                                        context.getSource().sendError(Text.literal("Block " + blockId + " is not in the breakable list"));
                                    }
                                    return 1;
                                })))
                .then(CommandManager.literal("ignitesTNT")
                        .then(CommandManager.argument("value", BoolArgumentType.bool())
                            .executes(context -> {
                                Boolean arg = BoolArgumentType.getBool(context, "value");
                                System.out.println(arg);
                                ignitesTNT = arg;
                                BlockListStorage.saveIgnitesTnt(arg);
                                String value = ignitesTNT ? "true" : "false";
                                context.getSource().sendFeedback(() -> Text.literal("SnowballsIgniteTNT set to: " + value), false);
                                return 1;
                            })
                        )
                        .executes(context -> {
                            String value = ignitesTNT ? "Snowballs currently DO ignite TNT." : "Snowballs currently DON'T ignite TNT.";
                            context.getSource().sendFeedback(() -> Text.literal(value), false);
                            return 1;
                        })
                )
                .then(CommandManager.literal("damagePlayers")
                        .then(CommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Boolean arg = BoolArgumentType.getBool(context, "value");
                                    damagePlayers = arg;
                                    BlockListStorage.saveDamagePlayers(arg);
                                    String value = damagePlayers ? "true" : "false";
                                    context.getSource().sendFeedback(() -> Text.literal("SnowballsDamagePlayers set to: " + value), false);
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            String value = damagePlayers ? "Snowballs currently DO damage players." : "Snowballs currently DON'T damage players.";
                            context.getSource().sendFeedback(() -> Text.literal(value), false);
                            return 1;
                        })
                )
        );
    }

    public static Set<Block> getBreakableBlocks() {
        return breakableBlocks;
    }

    public static Boolean getIgnitesTNT() {
        return ignitesTNT;
    }
}
