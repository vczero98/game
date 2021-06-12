package com.vczero98.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vczero98.game.controls.Thumbstick;
import com.vczero98.game.world.Player;
import com.vczero98.game.world.WorldState;

public class GameScreen implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera gameCamera = new OrthographicCamera();
    private final OrthographicCamera uiCamera = new OrthographicCamera();
    private final Stage gameStage;
    private final Stage uiStage;
    private final GameMap gameMap;
    private final ShapeRenderer shape = new ShapeRenderer();
    private final Thumbstick thumbstick;
    private final Player player;

    public GameScreen(final MyGdxGame game) {
        this.game = game;

//        gamePort = new ExtendViewport(800, 480, gameCamera);
        gameStage = new Stage(new FillViewport(800, 480, gameCamera));
        uiStage = new Stage(new ScreenViewport(uiCamera));

        thumbstick = new Thumbstick(uiCamera);

        //800, 480, gameCamera
        gameCamera.setToOrtho(false, 800, 480);
//        uiCamera.setToOrtho(false, );

        WorldState worldState = new WorldState(123);
        if (worldState.getDebugMode()) {
            gameCamera.zoom = 15;
            Gdx.app.log("Camera", "Camera x: " + gameCamera.position.x + ", y: " + gameCamera.position.y);
        }

        gameMap = new GameMap(worldState, gameCamera);
        player = new Player(worldState, gameCamera, thumbstick);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        ScreenUtils.clear(0,0,0.2f, 1);

        player.update(delta);
        gameMap.update();
        thumbstick.update();
        gameCamera.update();
        uiCamera.update();

//        Gdx.app.log("tapped", "xSpeed: " + thumbstick.xSpeed + ", ySpeed: " + thumbstick.ySpeed);

        gameStage.getViewport().apply();
        game.batch.setProjectionMatrix(gameCamera.combined);
        shape.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        gameMap.draw(shape);
        player.draw(shape);
//        game.font.draw(game.batch, "Hello, Game!", 10, 400-10);
        uiStage.getViewport().apply();
        thumbstick.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height);
        uiCamera.setToOrtho(false, width, height);
        uiStage.getViewport().update(width, height);
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
