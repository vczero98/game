package com.vczero98.game.world;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.vczero98.game.GameMap;
import com.vczero98.game.GameScreen;
import com.vczero98.game.controls.Thumbstick;

public class Player {
    public static final int HEIGHT = 25;
    public static final int WIDTH = 15;
    public static final int PLAYER_SPEED = 120;
    private final OrthographicCamera camera;
    private final Thumbstick thumbstick;
    private final Vector3 position;
    private final Vector3 nextPosition;
    private final WorldState worldState;

    public Player(WorldState worldState, OrthographicCamera camera, Thumbstick thumbstick) {
        this.worldState = worldState;
        this.camera = camera;
        this.thumbstick = thumbstick;

        // Set initial position to the centre of a block
        //GameMap.TILE_SIZE / 2f - WIDTH / 2f
        position = new Vector3(GameMap.TILE_SIZE / 2f - WIDTH / 2f, GameMap.TILE_SIZE / 2f - WIDTH / 2f, 0);
//        position = new Vector3(0, 0, 0);

//        Gdx.app.log("Position", "" + (GameMap.TILE_SIZE / 2f - WIDTH / 2f));
        // Check if current position is legal, important when starting the game
        while (!isPositionLegal(position)) {
//            Gdx.app.log("Player", position.toString());
            position.y += GameMap.TILE_SIZE;
        }
        nextPosition = new Vector3(position);
//        Gdx.app.log("Position", "" + position.x);
    }

    public void update(float dt) {
//        Gdx.app.log("Position", "start " + position.x);
        handleInput(dt);
        if (isPositionLegal(nextPosition)) {
            position.x = nextPosition.x;
            position.y = nextPosition.y;
        } else if (isPositionLegal(new Vector3(position.x, nextPosition.y, 0))) {
            // Check if player can move in Y direction
            position.y = nextPosition.y;
        } else if (isPositionLegal(new Vector3(nextPosition.x, position.y, 0))) {
            // Check if player can move in X direction
            position.x = nextPosition.x;
        }
        camera.position.set(position);
//        Gdx.app.log("Position", "end " + position.x);
    }

    private boolean isPositionLegal(Vector3 position) {
        Block cornerBlocks[] = getCornerBlocks(position);
        for (Block b : cornerBlocks) {
            if (!isBlockLegalPosition(b)) return false;
        }
        return true;
    }

    // Update nextPosition that will be evaluated as legal/illegal move
    private void handleInput(float dt) {
        float doubleKeyPenalty = (
                (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)) ||
                (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) ||
                (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)) ||
                (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.DOWN))
        ) ? 0.70711f : 1;
        float dSpeed = PLAYER_SPEED * dt * doubleKeyPenalty;

        if (thumbstick.xSpeed != 0f || thumbstick.ySpeed != 0f) {
            nextPosition.x = position.x + PLAYER_SPEED * thumbstick.xSpeed * dt;
            nextPosition.y = position.y + PLAYER_SPEED * thumbstick.ySpeed * dt;
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                nextPosition.x = position.x - dSpeed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                nextPosition.x = position.x + dSpeed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                nextPosition.y = position.y - dSpeed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                nextPosition.y = position.y + dSpeed;
            }
        }
    }

    // Player's X or Y position to block's X or Y coordinate
    private int playerPosToBlockPos(float p) {
        return Math.round(p / (float) GameMap.TILE_SIZE - 0.5f);
    }

    // Returns an array of the blocks the four corners of the player touches from a given position
    private Block[] getCornerBlocks(Vector3 position) {
        float leftX = position.x;
        float rightX = position.x + WIDTH;
        float bottomY = position.y;
        float topY = position.y + HEIGHT;
        Block blocks[] = {
                worldState.getBlock(playerPosToBlockPos(leftX), playerPosToBlockPos(bottomY)),
                worldState.getBlock(playerPosToBlockPos(leftX), playerPosToBlockPos(topY)),
                worldState.getBlock(playerPosToBlockPos(rightX), playerPosToBlockPos(bottomY)),
                worldState.getBlock(playerPosToBlockPos(rightX), playerPosToBlockPos(topY)),
        };
        return blocks;
    }

    private boolean isBlockLegalPosition(Block block) {
        if (block.getType() == BlockType.WATER)
            return false;

        if (block.getType() == BlockType.FOREST && block.getItemType() != ItemType.NONE)
            return false;

        return true;
    }

    public void draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.RED);
        shape.rect(position.x, position.y, WIDTH, HEIGHT);
        shape.end();
    }
}
