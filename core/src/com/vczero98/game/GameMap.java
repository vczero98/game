package com.vczero98.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.jayfella.fastnoise.FastNoise;

import java.util.Random;

public class GameMap {
    private TiledMap map;
    private ShapeRenderer shape;
    private int width = 150;
    private int height = 100;
    private Array<Array<Color>> tiles;
    private int tileSize = 5;
    private Random random = new Random(124);

    public GameMap(ShapeRenderer shape) {
         this.shape = shape;
    }

    public void generate() {

//        float w = Gdx.graphics.getWidth();
//        float h = Gdx.graphics.getHeight();

//        map = new TiledMap();
//        MapLayers layers = map.getLayers();
        tiles = new Array();
        for (int x = 0; x < width; x++) {
            Array<Color> tileRow = new Array();
            tiles.add(tileRow);
            for (int y = 0; y < height; y++) {
                Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
//                Color color = new Color(0.5f, 1, 1, 1);
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
