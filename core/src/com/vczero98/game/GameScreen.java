package com.vczero98.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.vczero98.game.controls.Thumbstick;

public class GameScreen implements Screen {
    private final MyGdxGame game;
    private OrthographicCamera camera = new OrthographicCamera();
    private GameMap gameMap;
    private int cameraSpeed = 5;
    private ShapeRenderer shape = new ShapeRenderer();
    private Thumbstick thumbstick = new Thumbstick();

    public GameScreen(final MyGdxGame game) {
        this.game = game;

        camera.setToOrtho(false, 800, 480);

        gameMap = new GameMap(shape);
        gameMap.generate();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        ScreenUtils.clear(0,0,0.2f, 1);
        camera.update();
        thumbstick.update();
        handleInput();
//        Gdx.app.log("tapped", "xSpeed: " + thumbstick.xSpeed + ", ySpeed: " + thumbstick.ySpeed);

        game.batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);
        game.batch.begin();
        gameMap.draw();
//        game.font.draw(game.batch, "Hello, Game!", 10, 400-10);
        thumbstick.draw();
        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-cameraSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(cameraSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -cameraSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, cameraSpeed);
        }

        if (thumbstick.xSpeed != 0f || thumbstick.ySpeed != 0f) {
            camera.translate(cameraSpeed * thumbstick.xSpeed, cameraSpeed * thumbstick.ySpeed);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shape.dispose();
        thumbstick.dispose();
    }
}
