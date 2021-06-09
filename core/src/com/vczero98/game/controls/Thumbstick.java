package com.vczero98.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.HashMap;

class Point {
    int x;
    int y;
}

public class Thumbstick implements Tappable {
    private ShapeRenderer shape = new ShapeRenderer();
    private OrthographicCamera camera = new OrthographicCamera();
    private Color backColor = new Color(0.8f, 0.8f, 0.8f, 0.8f);
    private Color borderColor = new Color(0, 0, 0, 1);
    private static final int RADIUS = 60;
    private static final int MARGIN = 30;
    public float xSpeed = 0;
    public float ySpeed = 0;
    private int innerCircleCentreX = 0;
    private int innerCircleCentreY = 0;
    private boolean isActive = false;
    private final Point p = new Point();
    private final Point onCircle = new Point();
    private static final int VIEWPORT_WIDTH = 800;
    private static final int VIEWPORT_HEIGHT = 480;
    private Vector3 inputVector = new Vector3();
    private Vector3 unprojectedInputVector = new Vector3();

    public Thumbstick() {
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    public void update() {
        innerCircleCentreX = RADIUS + MARGIN;
        innerCircleCentreY = innerCircleCentreX;

        inputVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        unprojectedInputVector.set(camera.unproject(inputVector));

        int centre = RADIUS + MARGIN;
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
            Point onCircle = getPointOnCircle(p);
            innerCircleCentreX = onCircle.x;
            innerCircleCentreY = onCircle.y;
        }

        int deltaX = innerCircleCentreX - centre;
        int deltaY = innerCircleCentreY - centre;
        xSpeed = deltaX / (float) RADIUS;
        ySpeed = deltaY / (float) RADIUS;
    }

    private Point getPointOnCircle(Point point) {
        int centre = MARGIN + RADIUS;
        double a = (centre - (RADIUS * (centre - point.x))/Math.sqrt(Math.pow(centre - point.x,2)+Math.pow(centre - point.y, 2)));
        double b = (((centre - point.y)/(double) (centre - point.x))*(a - point.x)+point.y);
//        Point intercept = new Point();
        onCircle.x = (int) a;
        onCircle.y = (int) b;
        return onCircle;
    }

    public void draw() {
        camera.update();
        shape.setProjectionMatrix(camera.combined);
        for (int i = 0; i < 2; i++) {
            shape.begin(i == 0 ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line);
            shape.setColor(i == 0 ? backColor : borderColor);

            // Draw outer circle
            shape.circle(RADIUS + MARGIN, RADIUS + MARGIN, RADIUS);

            shape.circle(innerCircleCentreX, innerCircleCentreY, RADIUS * 0.6f);

            shape.end();
        }
    }

    public boolean isTapped() {
        return isActive;
    }

    private boolean isFingerOverThumbstick() {
        int centre = RADIUS + MARGIN;
        int distance = (int) getDistance(centre, centre, (int) unprojectedInputVector.x, (int) unprojectedInputVector.y);
//        Gdx.app.log("tapped", "distance: " + distance);
        return distance < RADIUS;
    }

    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public void dispose() {
        shape.dispose();
    }
}
