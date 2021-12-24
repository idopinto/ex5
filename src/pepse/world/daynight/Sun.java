package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun - moves across the sky in an elliptical path.
 */
public class Sun {
    private static final Color SUN_COLOR = Color.YELLOW ;
    private static final int SUN_SIZE = 200;
    private static final float FULL_CYCLE = 360;
    private float sunCenter;

    /**
     *This function creates a yellow circle that moves in the sky in an elliptical path (in camera coordinates).
     * @param gameObjects  The collection of all participating game objects.
     * @param layer The number of the layer to which the created sun should be added.
     * @param windowDimensions The dimensions of the windows.
     * @param cycleLength The amount of seconds it should take the created game object to complete a full cycle.
     * @return A new game object representing the sun.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer, Vector2 windowDimensions, float cycleLength)
    {
        GameObject sun = new GameObject( new Vector2(windowDimensions.x()/2, windowDimensions.y()/3),
                new Vector2(SUN_SIZE,SUN_SIZE), new OvalRenderable(SUN_COLOR));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag("sun");

        // f: [0,360] -> void
        // f(x) = sun.setCenter(calc(x))

        new Transition<>(
                sun, // the game object being changed
                angle-> {
//                    sun.setCenter(windowDimensions.normalized().add(Vector2.RIGHT.mult(windowDimensions.x()/2).rotated(angle)));
                    sun.setCenter( windowDimensions.mult(0.5f).add(Vector2.UP).mult(1).rotated(angle));
                }, // the method to call
                0f, // initial transition value
                FULL_CYCLE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a linear interpolator
                cycleLength, // transition fully over cycleLength
                Transition.TransitionType.TRANSITION_LOOP,
                null); // nothing further to execute upon reaching final value
        return sun;
    }

//    /*
//        this method calculates the new sun position
//     */
//    private static Vector2 calcSunPosition(Vector2 windowDimensions, float angleInSky,GameObject sun)
//    {
//        Vector2 newSunCenter = sun.getCenter();
//
//    }
}
