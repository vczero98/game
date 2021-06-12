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
import com.vczero98.game.world.Player;
import com.vczero98.game.world.WorldState;

public class GameScreen implements Screen {
    private final MyGdxGame game;
    private OrthographicCamera camera = new OrthographicCamera();
    private GameMap gameMap;
    private ShapeRenderer shape = new ShapeRenderer();
    private Thumbstick thumbstick = new Thumbstick();
    private WorldState worldState = new WorldState(123);
    private final Player player;

    public GameScreen(final MyGdxGame game) {
        this.game = game;

        camera.setToOrtho(false, 800, 480);
        if (worldState.getDebugMode()) {
            camera.zoom = 15;
            Gdx.app.log("Camera", "Camera x: " + camera.position.x + ", y: " + camera.position.y);
        }

        gameMap = new GameMap(worldState, camera);
        player = new Player(worldState, camera, thumbstick);
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
        gameMap.update();
        thumbstick.update();
        player.update(delta);
//        Gdx.app.log("tapped", "xSpeed: " + thumbstick.xSpeed + ", ySpeed: " + thumbstick.ySpeed);

        game.batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);
        game.batch.begin();
        gameMap.draw(shape);
        player.draw(shape);
//        game.font.draw(game.batch, "Hello, Game!", 10, 400-10);
        thumbstick.draw();
        game.batch.end();
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
