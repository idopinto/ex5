package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.util.Energy;

import java.awt.event.KeyEvent;
import java.util.function.UnaryOperator;

/**
 * An avatar can move around the world.
 * Fields inherited from class danogl.GameObject
 * freeCalculationsVector
 */
public class Avatar extends danogl.GameObject {

    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 500;
    private static final String AVATAR_TAG = "avatar";
    private static final String GROUND_TAG = "ground";
    private static final String TRUNK_LAYER = "trunk";
    private static final String[] WALK_ANIMATION = {"assets/run_0.png", "assets/run_1.png",
            "assets/run_2.png", "assets/run_3.png", "assets/run_4.png", "assets/run_5.png"};
    private static final String[] JUMP_ANIMATION = {"assets/jump_0.png", "assets/jump_1.png",
            "assets/jump_2.png", "assets/jump_3.png"};
    private static final String[] FLY_ANIMATION = {"assets/swim_0.png", "assets/swim_1.png",
            "assets/swim_2.png", "assets/swim_3.png", "assets/swim_4.png", "assets/swim_5.png"};
    private static final String FALLING_ANIMATION = "assets/x_1.png";
    private static final String STANDING_ANIMATION = "assets/idle_1.png";
    private static final int INITIAL_ENERGY_COUNTER_VALUE = 200;
    private static final float ENERGY_Y_COORDINATE = 20;
    private static final float ENERGY_DISPLAYER_SIZE = 30;
    private static final float AVATAR_DIMENSIONS = 80;
    private final Renderable fallingRenderable;
    private final AnimationRenderable animationRenderableFly;
    private final AnimationRenderable animationRenderableJump;
    private final AnimationRenderable animationRenderableWalk;
    private static UserInputListener inputListener;
    private static ImageReader imageReader;
    private static Counter energyCounter;
    private static boolean hasNoEnergy;
    private final Renderable standingRenderable;
    private UnaryOperator<Float> groundHeightFunc;


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
        this.animationRenderableWalk = new AnimationRenderable(WALK_ANIMATION,
                imageReader, true, 0.1f);

        this.animationRenderableJump = new AnimationRenderable(JUMP_ANIMATION, Avatar.imageReader,
                true, 0.5f);
        this.animationRenderableFly = new AnimationRenderable(FLY_ANIMATION, Avatar.imageReader,
                true, 0.1f);
        this.fallingRenderable = Avatar.imageReader.readImage(FALLING_ANIMATION, true);
        this.standingRenderable = Avatar.imageReader.readImage(STANDING_ANIMATION,true);
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera.
     * The can stand, walk, jump and fly, and never reaches the end of the world.
     *
     * @param gameObjects   The collection of all participating game objects.
     * @param layer         The number of the layer to which the created avatar should be added.
     * @param topLeftCorner The location of the top-left corner of the created avatar.
     * @param inputListener Used for reading input from the user.
     * @param imageReader   Used for reading images from disk or from within a jar.
     * @return A newly created representing the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader) {
        Avatar.inputListener = inputListener;
        Avatar.imageReader = imageReader;
        Avatar.energyCounter = new Counter(INITIAL_ENERGY_COUNTER_VALUE);
        Energy energy = new Energy(Avatar.energyCounter, new Vector2(0, ENERGY_Y_COORDINATE),
                new Vector2(ENERGY_DISPLAYER_SIZE, ENERGY_DISPLAYER_SIZE));
        energy.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(energy, Layer.BACKGROUND);

        Avatar.hasNoEnergy = false;

        Avatar avatar = new Avatar(topLeftCorner, new Vector2(AVATAR_DIMENSIONS, AVATAR_DIMENSIONS),
                imageReader.readImage(STANDING_ANIMATION, true));
        gameObjects.addGameObject(avatar, layer);
        avatar.setTag(AVATAR_TAG);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.transform().setAccelerationY(GRAVITY);

        return avatar;

    }

    /**
     * runs every frame update.
     *
     * @param deltaTime Time between updates. For internal use by game engine.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.renderer().setRenderable(this.standingRenderable);
        float xVel = 0;

        hittingTheGround();

        xVel = movingLeft(xVel);
        xVel = movingRight(xVel);
        // Update X velocity
        transform().setVelocityX(xVel);

        jump();
        fly();
        fallingFromZeroEnergy();
    }


    /**
     * Defines what happens when the character has no energy at all.
     */
    private void fallingFromZeroEnergy() {
        if (energyCounter.value() == 0) {
            this.renderer().setRenderable(this.fallingRenderable);
        }
    }

    /**
     * Defines what happen when the avatar falls and hits the ground.
     */
    private void hittingTheGround() {
        if ((this.transform().getVelocity().y() < VELOCITY_Y) ||
                (this.getCenter().y() > this.groundHeightFunc.apply(this.getCenter().x()))) {
            this.transform().setVelocityY(VELOCITY_Y);
            this.transform().setCenter(this.getCenter().x(),
                    this.groundHeightFunc.apply(this.getCenter().x()) - Block.SIZE);
        }

    }


    /**
     * Defines what happens when the avatar flies.
     */
    private void fly() {
        // Fly with (Space + Shift)
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && !hasNoEnergy) {
            transform().setVelocityY(VELOCITY_Y);
            energyCounter.decrement();
            this.renderer().setRenderable(this.animationRenderableFly);
            if (energyCounter.value() == 0) {
                hasNoEnergy = true;
                transform().setVelocityY(0);
            }
        }
    }


    /**
     * Defines what happens when the avatar jumps.
     */
    private void jump() {
        // Jump only if on the ground
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && (getVelocity().y() == 0)) {
            transform().setVelocityY(VELOCITY_Y);
            this.renderer().setRenderable(this.animationRenderableJump);
        }
    }

    /**
     * Checks if the avatar moves right. If it does, sets the new velocity accordingly.
     *
     * @param xVel The delta of velocity of the avatar.
     */
    private float movingRight(float xVel) {
        // Move right
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            this.renderer().setRenderable(this.animationRenderableWalk);
            this.renderer().setIsFlippedHorizontally(false);
        }
        return xVel;
    }

    /**
     * Checks if the avatar moves left. If it does, sets the new velocity accordingly.
     *
     * @param xVel The delta of velocity of the avatar.
     */
    private float movingLeft(float xVel) {
        // Move left
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            this.renderer().setRenderable(this.animationRenderableWalk);
            this.renderer().setIsFlippedHorizontally(true);
        }
        return xVel;
    }


    /**
     * Called on every frame of a collision with a given object, including the first.
     *
     * @param other     The collision partner.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);
        if (hasNoEnergy) {
            hasNoEnergy = false;
        }
        if (((other.getTag().equals(GROUND_TAG) || other.getTag().equals(TRUNK_LAYER)) && (energyCounter.value() < 200))) {
            energyCounter.increment();
        }
    }

    /**
     * Defines the behavior of the Avatar on collision.
     *
     * @param other     - The GameObject which the leaf is set to collide with.
     * @param collision - Information about what does the collision contains.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(GROUND_TAG) || other.getTag().equals(TRUNK_LAYER)) {
            this.setVelocity(Vector2.ZERO);
        }
    }

    /**
     * Sets the height of the avatar when created.
     *
     * @param groundHeightFunc Gets a float (x coordinate) and returns the height of the ground
     *                         in the current coordinate.
     */
    public void setGroundHeightFunc(UnaryOperator<Float> groundHeightFunc) {
        this.groundHeightFunc = groundHeightFunc;
    }

}
