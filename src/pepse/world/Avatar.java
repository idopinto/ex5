package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 *
 * An avatar can move around the world.
 * Fields inherited from class danogl.GameObject
 * freeCalculationsVector
 */
public class Avatar extends danogl.GameObject
{

    private static final float VELOCITY_X = 200;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 500;
    private static final String AVATAR_TAG = "avatar";
    private static final String GROUND_TAG = "ground";
    private static final String FIRST_TREETOP_LAYER = "top";
    private static UserInputListener inputListener;
    private static ImageReader imageReader;
    private static Counter energyCounter;
    private static boolean hasNoEnergy;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera.
     * The can stand, walk, jump and fly, and never reaches the end of the world.
     * @param gameObjects The collection of all participating game objects.
     * @param layer The number of the layer to which the created avatar should be added.
     * @param topLeftCorner The location of the top-left corner of the created avatar.
     * @param inputListener Used for reading input from the user.
     * @param imageReader  Used for reading images from disk or from within a jar.
     * @return A newly created representing the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader)
    {
        Avatar.inputListener = inputListener;
        Avatar.imageReader = imageReader;
        Avatar.energyCounter = new Counter(200);
        Energy energy = new Energy(Avatar.energyCounter, new Vector2(0,20), new Vector2(30, 30), gameObjects);
        energy.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(energy, Layer.BACKGROUND);

        Avatar.hasNoEnergy = false;
        Avatar avatar = new Avatar(topLeftCorner,new Vector2(40,40),new OvalRenderable(Color.BLUE));
        gameObjects.addGameObject(avatar, layer);
        avatar.setTag(AVATAR_TAG);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.transform().setAccelerationY(GRAVITY);

        return avatar;

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;

        //TODO: Should be able to rest on a treetop
        //TODO: Updating the energy parameter of the avatar according to its current state.

        if (this.transform().getVelocity().y() < VELOCITY_Y){
            this.transform().setVelocityY(VELOCITY_Y);
        }

        // Move left
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            xVel -= VELOCITY_X;
        // Move right
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            xVel += VELOCITY_X;
        // Update X velocity
        transform().setVelocityX(xVel);

        // Jump only if on the ground
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && (getVelocity().y() == 0))
        {
            transform().setVelocityY(VELOCITY_Y);

        }
        // Fly with (Space + Shift)
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)&&!hasNoEnergy) {
            transform().setVelocityY(VELOCITY_Y);
            energyCounter.decrement();
            if (energyCounter.value() == 0)
            {
                hasNoEnergy = true;
                transform().setVelocityY(0);
//                energyCounter.reset();
            }
        }

    }

    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);
        if (hasNoEnergy) {
            hasNoEnergy = false;
        }
        if ((other.getTag().equals(GROUND_TAG) && (energyCounter.value() < 200)))
        {
            energyCounter.increment();
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(GROUND_TAG) || other.getTag().equals(FIRST_TREETOP_LAYER)){
            this.setVelocity(Vector2.ZERO);
        }
    }
}
