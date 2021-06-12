package com.vczero98.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.HashMap;

class Point {
    int x;
    int y;
}

public class Thumbstick extends Actor implements Tappable {
    private final ShapeRenderer shape = new ShapeRenderer();
    private final OrthographicCamera camera;
    private final Color backColor = new Color(0.8f, 0.8f, 0.8f, 0.8f);
    private final Color borderColor = new Color(0, 0, 0, 1);
    private int radius = 0;
    private int margin = 0;
    public float xSpeed = 0;
    public float ySpeed = 0;
    private int innerCircleCentreX = 0;
    private int innerCircleCentreY = 0;
    private boolean isActive = false;
    private final Point p = new Point();
    private final Point onCircle = new Point();
    private static final int VIEWPORT_WIDTH = 800;
    private static final int VIEWPORT_HEIGHT = 480;
    private final Vector3 inputVector = new Vector3();
    private final Vector3 unprojectedInputVector = new Vector3();

    public Thumbstick(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void update() {
        radius = (int) (camera.viewportHeight / 7.5f);
        margin = (int) (camera.viewportHeight / 8f);

        innerCircleCentreX = radius + margin;
        innerCircleCentreY = innerCircleCentreX;

        inputVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        unprojectedInputVector.set(camera.unproject(inputVector));

        int centre = radius + margin;
//        Gdx.app.log("tapped", "x: " + x + ", y:" + y);

        // Draw inner circle
        if (!Gdx.input.isTouched()) {
            isActive = false;
        } else if (isFingerOverThumbstick()) {
            isActive = true;
            innerCircleCentreX = (int) unprojectedInputVector.x;
            innerCircleCentreY = (int) unprojectedInputVector.y;
        } else if (isActive) {
            p.x = (int) unprojectedInputVector.x;
            p.y = (int) unprojectedInputVector.y;
            if (p.x == centre) {
                // Fix a glitch where the thumbstick is min/max X direction and the getPointOnCircle formula breaks
                if (p.y > centre) {
                    innerCircleCentreX = centre;
                    innerCircleCentreY = centre + radius;
                } else {
                    innerCircleCentreX = centre;
                    innerCircleCentreY = centre - radius;
                }
            } else {
                Point onCircle = getPointOnCircle(p);
                innerCircleCentreX = onCircle.x;
                innerCircleCentreY = onCircle.y;
            }
        }

        int deltaX = innerCircleCentreX - centre;
        int deltaY = innerCircleCentreY - centre;
        xSpeed = deltaX / (float) radius;
        ySpeed = deltaY / (float) radius;
    }

    private Point getPointOnCircle(Point point) {
        int centre = margin + radius;
        double a = (centre - (radius * (centre - point.x))/Math.sqrt(Math.pow(centre - point.x,2)+Math.pow(centre - point.y, 2)));
        double b = (((centre - point.y)/(double) (centre - point.x))*(a - point.x)+point.y);
//        Point intercept = new Point();
        onCircle.x = (int) a;
        onCircle.y = (int) b;
        return onCircle;
    }

    public void draw() {
        shape.setProjectionMatrix(camera.combined);
        for (int i = 0; i < 2; i++) {
            shape.begin(i == 0 ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line);
            shape.setColor(i == 0 ? backColor : borderColor);

            // Draw outer circle
            shape.circle(radius + margin, radius + margin, radius);

            shape.circle(innerCircleCentreX, innerCircleCentreY, radius * 0.6f);

            shape.end();
        }
    }

    public boolean isTapped() {
        return isActive;
    }

    private boolean isFingerOverThumbstick() {
        int centre = radius + margin;
        int distance = (int) getDistance(centre, centre, (int) unprojectedInputVector.x, (int) unprojectedInputVector.y);
//        Gdx.app.log("tapped", "distance: " + distance);
        return distance < radius;
    }

    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public void dispose() {
        shape.dispose();
    }
}
