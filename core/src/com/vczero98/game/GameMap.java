package com.vczero98.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.flowpowered.noise.module.source.Perlin;
import com.vczero98.game.world.Block;
import com.vczero98.game.world.BlockType;
import com.vczero98.game.world.ItemType;
import com.vczero98.game.world.WorldState;
//import com.jayfella.fastnoise.FastNoise;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class GameMap {
    public static final int TILE_SIZE = 40;
    private OrthographicCamera camera;
    private final WorldState worldState;
    private int height;
    private int width;
    private static final Color COLOR_SAND = new Color(199 / 255f, 196 / 255f, 54 / 255f, 1);
    private static final Color COLOR_GRASS = new Color(0, 110 / 255f, 29 / 255f, 1);
    private static final Color COLOR_FOREST = new Color(0, 77 / 255f, 20 / 255f, 1);
    private static final Color COLOR_WATER = new Color(0, 0, 1, 1);
    private static final Color COLOR_BLACK = new Color(0, 0, 0, 1);
    private static final Color COLOR_TREE = new Color(0 / 255f, 51 / 255f, 2 / 255f, 1);

    public GameMap(WorldState worldState, OrthographicCamera camera) {
        this.worldState = worldState;
        height = 480 / TILE_SIZE + 2;
        width = 800 / TILE_SIZE + 2;
        if (worldState.getDebugMode()) {
            height = height * 15;
            width = width * 15;
        }
        this.camera = camera;
    }

    public void update() {
        int camX = (int) camera.position.x / TILE_SIZE;
        int camY = (int) camera.position.y / TILE_SIZE;
//        Gdx.app.log("camera", camera.position.x / TILE_SIZE + " " + camera.position.y / TILE_SIZE);
        for (int x = camX - width / 2; x < camX + width / 2; x++) {
            for (int y = camY - height / 2; y < camY + width / 2; y++) {
                worldState.generateBlock(x, y);
            }
        }
    }

    public void draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        int camX = (int) camera.position.x / TILE_SIZE;
        int camY = (int) camera.position.y / TILE_SIZE;
        int noneCount = 0;
        int commonCount = 0;
        int uncommonCount = 0;
        int rareCount = 0;
//        Gdx.app.log("camera", camera.position.x / TILE_SIZE + " " + camera.position.y / TILE_SIZE);
        for (int x = camX - width / 2; x < camX + width / 2; x++) {
            for (int y = camY - height / 2; y < camY + width / 2; y++) {
                Block block = worldState.getBlock(x, y);
                Color color;
                if (block == null) {
                    color = COLOR_BLACK;
                } else {
                    switch (block.getType()) {
                        case WATER:
                            color = COLOR_WATER;
                            break;
                        case SAND:
                            color = COLOR_SAND;
                            break;
                        case GRASS:
                            color = COLOR_GRASS;
                            break;
                        case FOREST:
                            if (block.getItemType() == ItemType.NONE) {
                                color = COLOR_FOREST;
                            } else {
                                color = COLOR_TREE;
                            }
                            break;
                        default:
                            color = COLOR_BLACK;
                    }
//
//                    switch (block.getItemType()) {
//                        case COMMON:
//                            color = Color.BLACK;
//                            commonCount++;
//                            break;
//                        case UNCOMMON:
//                            color = Color.BROWN;
//                            uncommonCount++;
//                            break;
//                        case RARE:
//                            color = Color.PINK;
//                            rareCount++;
//                            break;
//                        default:
//                            noneCount++;
//                    }
                }

                shape.setColor(color);
                shape.rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

        }
        if (worldState.getDebugMode()) {
            int total = noneCount + commonCount + uncommonCount + rareCount;
            Gdx.app.log(    "Items", "NONE: " + noneCount / (float) total * 100 + "% "
                    + "COMMON: " + commonCount / (float) total * 100 + "% "
                    + "UNCOMMON: " + uncommonCount / (float) total * 100 + "% "
                    + "RARE: " + rareCount / (float) total * 100 + "% ");
        }
        shape.end();
    }
}
