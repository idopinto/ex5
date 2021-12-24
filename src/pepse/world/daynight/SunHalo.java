package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the halo of sun.
 */
public class SunHalo {

    /**
     * This function creates a halo around a given object that represents the sun.
     * The halo will be tied to the given sun, and will always move with it.
     * @param gameObjects The collection of all participating game objects.
     * @param layer The number of the layer to which the created halo should be added.
     * @param sun  A game object representing the sun (it will be followed by the created game object).
     * @param color  The color of the halo.
     * @return A new game object representing the sun's halo.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer, GameObject sun, Color color)
    {
        GameObject halo = new GameObject( Vector2.ZERO,sun.getDimensions().mult(2), new OvalRenderable(color));
        halo.setCenter(sun.getCenter());
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(halo, layer);
        halo.setTag("halo");
        return halo;
    }
}
