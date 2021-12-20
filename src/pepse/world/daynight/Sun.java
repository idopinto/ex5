package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;

/**
 * Represents the sun - moves across the sky in an elliptical path.
 */
public class Sun {
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
        return null;
    }
}
