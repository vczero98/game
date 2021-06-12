package com.vczero98.game.world;

import com.badlogic.gdx.graphics.Color;
import com.flowpowered.noise.module.source.Perlin;

import java.util.HashMap;

public class WorldState {
    private final HashMap<String, Block> blocks = new HashMap<>();

    private final Perlin perlinIsland;
    private final Perlin perlinSand;
    private final Perlin perlinForest;
    private final Perlin perlinGrass;
    private HashMap<Double, BlockType> perlinsMap = new HashMap<>();

    public WorldState(int seed) {
        this.perlinIsland = new Perlin();
        this.perlinIsland.setSeed(seed);
        perlinIsland.setFrequency(0.05);
        perlinIsland.setLacunarity(2);
        perlinIsland.setPersistence(0.5);

        this.perlinSand = new Perlin();
        this.perlinForest = new Perlin();
        this.perlinGrass = new Perlin();

        Perlin perlins[] = {perlinSand, perlinForest, perlinGrass};
        for (int i = 0; i < perlins.length; i++) {
            perlins[i].setSeed(seed + i + 500);
            perlins[i].setFrequency(0.02);
            perlins[i].setLacunarity(7);
            perlins[i].setPersistence(0.1);
        }
    }

    public Block getBlock(int x, int y) {
        return blocks.get(x + "," + y);
    }

    public void setBlock(int x, int y, Block block) {
        blocks.put(x + "," + y, block);
    }

    public Block generateBlock(int x, int y) {
        Block block = getBlock(x, y);
        if (block != null) return block;

        double perlinIslandVal = getPerlinVal(perlinIsland, x, y);
        double perlinGrassVal = getPerlinVal(perlinGrass, x, y);
        double perlinSandVal = getPerlinVal(perlinSand, x, y);
        double perlinForestVal = getPerlinVal(perlinForest, x, y);

        BlockType type;

        if (perlinIslandVal < 0.2f) type = BlockType.WATER;
        else if (perlinIslandVal < 0.25f) type = BlockType.SAND;
        else {
            // Choose max value of the perlin values
            perlinsMap.put(perlinSandVal, BlockType.SAND);
            perlinsMap.put(perlinForestVal, BlockType.FOREST);
            perlinsMap.put(perlinGrassVal, BlockType.GRASS);

            double max = 0;
            for (double val : perlinsMap.keySet()) {
                if (val > max) max = val;
            }
            type = perlinsMap.get(max);
            perlinsMap.clear();
        }

        block = new Block(type);
        setBlock(x, y, block);
        return block;
    }

    private static double getPerlinVal(Perlin perlin, int x, int y) {
        return Math.abs(perlin.getValue(x * 0.1d, y*0.1d, 1));
    }
}
