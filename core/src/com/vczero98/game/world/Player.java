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
    private final Vector3 position = new Vector3(0, 0, 0);
    private final Vector3 nextPosition = new Vector3(0, 0, 0);
    private final WorldState worldState;

    public Player(WorldState worldState, OrthographicCamera camera, Thumbstick thumbstick) {
        this.worldState = worldState;
        this.camera = camera;
        this.thumbstick = thumbstick;
    }

    public void update(float dt) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            nextPosition.x = position.x - PLAYER_SPEED * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            nextPosition.x = position.x + PLAYER_SPEED * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            nextPosition.y = position.y - PLAYER_SPEED * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            nextPosition.y = position.y + PLAYER_SPEED * dt;
        }

        if (thumbstick.xSpeed != 0f || thumbstick.ySpeed != 0f) {
            nextPosition.x = position.x + PLAYER_SPEED * thumbstick.xSpeed * dt;
            nextPosition.y = position.y + PLAYER_SPEED * thumbstick.ySpeed * dt;
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
        return block.getItemType() == ItemType.NONE;
    }

    public void draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.RED);
        shape.rect(position.x, position.y, WIDTH, HEIGHT);
        shape.end();
    }
}
