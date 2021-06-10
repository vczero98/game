package com.vczero98.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.flowpowered.noise.module.source.Perlin;
//import com.jayfella.fastnoise.FastNoise;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class GameMap {
    private TiledMap map;
    private ShapeRenderer shape;
    private int width = 400;
    private int height = 400;
    private Array<Array<Color>> tiles;
    private int tileSize = 1;
    private Random random = new Random(124);

    public GameMap(ShapeRenderer shape) {
         this.shape = shape;
    }

    public void generate() {
        Perlin perlinIsland = new Perlin();
        perlinIsland.setSeed(123456);
        perlinIsland.setFrequency(0.05);
//        perlinIsland.setFrequency(0.15);
//        perlin.setLacunarity(1);

        perlinIsland.setLacunarity(0.1);
//        perlin.setPersistence(0.5);
        perlinIsland.setPersistence(0.5);
//        perlin.setOctaveCount(11);
        Gdx.app.log("perlin", "frequency: " + perlinIsland.getFrequency());
        Gdx.app.log("perlin", "lacunarity: " + perlinIsland.getLacunarity());
        Gdx.app.log("perlin", "persistence: " + perlinIsland.getPersistence());
        Gdx.app.log("perlin", "octave count: " + perlinIsland.getOctaveCount());

        Perlin perlinSand = new Perlin();
        Perlin perlinGrass = new Perlin();
        Perlin perlinForest = new Perlin();
        Perlin perlins[] = {perlinSand, perlinGrass, perlinForest};
        for (int i = 0; i < perlins.length; i++) {
            perlins[i].setSeed(i + 1);
            perlins[i].setFrequency(0.15);
            perlins[i].setLacunarity(0.01);
            perlins[i].setPersistence(0.1);
        }

//        perlin.setSeed(124);
//        float w = Gdx.graphics.getWidth();
//        float h = Gdx.graphics.getHeight();

//        map = new TiledMap();
//        MapLayers layers = map.getLayers();
        tiles = new Array();
        for (int x = 0; x < width; x++) {
            Array<Color> tileRow = new Array();
            tiles.add(tileRow);
            for (int y = 0; y < height; y++) {
                float perlinIslandVal = (float) Math.abs(perlinIsland.getValue(x * 0.1d, y*0.1d, 1));
                double perlinGrassVal = Math.abs(perlinGrass.getValue(x * 0.1d, y*0.1d, 1));
                double perlinSandVal = Math.abs(perlinSand.getValue(x * 0.1d, y*0.1d, 1));
                double perlinForestVal = Math.abs(perlinForest.getValue(x * 0.1d, y*0.1d, 1));

//                Gdx.app.log("perlin", "" + perlin.getFrequency());
//                Gdx.app.log("perlin", "" + perlin.getLacunarity());

                Color color;
                Color sand = new Color(199 / 255f, 196 / 255f, 54 / 255f, 1);
                Color grass = new Color(0, 110 / 255f, 29 / 255f, 1);
                Color forest = new Color(0, 77 / 255f, 20 / 255f, 1);
                Color water = new Color(0, 0, 1, 1);

                if (perlinIslandVal < 0.2f) color = water;
                else {
//                    double perils[] = {perlinGrassVal, perlinSandVal, perlinForestVal};
                    HashMap<Double, Color> perils = new HashMap<>();
                    perils.put(perlinSandVal, sand);
                    perils.put(perlinForestVal, forest);
                    perils.put(perlinGrassVal, grass);

                    double max = 0;
                    for (double val : perils.keySet()) {
                        if (val > max) max = val;
                    }

                    color = perils.get(max);
                }
//                else if (perlinBiomeVal < 0.333f) color = grass;
//                else if (perlinBiomeVal < 0.6666f) color = sand;
//                else color = forest;

////                if (mappedVal < 0.01f) color = sand;
//                if (mappedVal < 0.2f) color = water;
//                else if (mappedVal < 0.3f) color = sand;
//                else if (mappedVal < 0.5f) color = grass;
//                else color = forest;
////                Color color = new Color(mappedVal, mappedVal, mappedVal, 1);
////                Color color = new Color(0.5f, 1, 1, 1);
                tileRow.add(color);
            }
        }
    }

    public void draw() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < tiles.size; x++) {
            Array<Color> tileRow = tiles.get(x);
            for (int y = 0; y < tileRow.size; y++) {
                shape.setColor(tileRow.get(y));
                shape.rect(x * tileSize, y * tileSize, tileSize, tileSize);
//                Gdx.app.log("Tag", "x: " + x + ", y: " + y);
            }
        }
        shape.end();
    }
}
