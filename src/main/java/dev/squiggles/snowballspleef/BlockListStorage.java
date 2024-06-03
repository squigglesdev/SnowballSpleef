package dev.squiggles.snowballspleef;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class BlockListStorage {
    private static final Gson GSON = new Gson();
    private static final Type BLOCK_SET_TYPE = new TypeToken<Set<String>>() {}.getType();
    private static final String FILE_NAME = "config/snowballspleef/breakable_blocks.json";

    public static void saveBlockList(Set<Block> blocks) {
        Set<String> blockIds = new HashSet<>();
        for (Block block : blocks) {
            blockIds.add(Registries.BLOCK.getId(block).toString());
        }

        Path filePath = Paths.get(FILE_NAME);
        try {
            Files.createDirectories(filePath.getParent());
            try (Writer writer = new FileWriter(FILE_NAME)) {
                GSON.toJson(blockIds, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<Block> loadBlockList() {
        Set<Block> blocks = new HashSet<>();
        Path filePath = Paths.get(FILE_NAME);
        if (Files.exists(filePath)) {
            try (Reader reader = new FileReader(FILE_NAME)) {
                Set<String> blockIds = GSON.fromJson(reader, BLOCK_SET_TYPE);
                if (blockIds != null) {
                    for (String blockId : blockIds) {
                        Block block = Registries.BLOCK.get(new Identifier(blockId));
                        if (block != null) {
                            blocks.add(block);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return blocks;
    }
}