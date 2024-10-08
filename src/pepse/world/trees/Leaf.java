package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Objects;
import java.util.Random;


public class Leaf extends Block {

    /* Vibration Constants */
    private static final float ANGLE_INITIAL_VAL = 0f;
    private static final float ANGLE_FINAL_VAL = 50f;
    private static final float VIBRATION_DURATION = 3;
    private static final float MIN_DIMENSION = 26;

    /* Horizontal movement of leaf while falling constants */
    private static final float HORIZONTAL_MOVEMENT_RANGE = 20f;
    private static final float HORIZONTAL_MOVEMENT_CYCLE_LENGTH = 2f;

    /* Fade-out Constants */
    private static final int FADEOUT_TIME = 15;

    private static final int MAX_DEATH_TIME_SPAN = 50;
    private static final int MAX_LIFE_TIME_SPAN = 100;
    private static final int FALL_VELOCITY = 30;


    private final Vector2 initialPositionOfLeaf;
    private Transition<Float> horizontalTransition;
    private Transition<Float> angleVibrationEffect;
    private Transition<Vector2> dimVibrationEffect;
    private final float opaqueness;
    private final GameObjectCollection gameObjects;
    private final Random random;


    /**
     * Construct a new GameObject instance.
     *
     * @param gameObjects
     * @param topLeftCorner The location of the top-left corner of the created block
     * @param renderable    - A renderable to render as the block.
     */
    public Leaf(GameObjectCollection gameObjects, Vector2 topLeftCorner, Renderable renderable, int seed) {
        super(topLeftCorner, renderable);

        this.gameObjects = gameObjects;
        this.random = new Random(Objects.hash(this.getCenter().x() * this.getCenter().y(), seed));

        // save initial state of the leaf
        this.opaqueness = this.renderer().getOpaqueness();
        this.initialPositionOfLeaf = this.getCenter();
        physics().setMass(5f);
        // start leaf routine
        leafRoutine();
    }


    /**
     * Defines the behavior of the leaf on collision. The leaf stops from moving from side to side.
     *
     * @param other     - The GameObject which the leaf is set to collide with.
     * @param collision - Information about what does the collision contains.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // Removing all the transitions made before
        this.removeComponent(this.horizontalTransition);
        this.removeComponent(this.angleVibrationEffect);
        this.removeComponent(this.dimVibrationEffect);

        // updating in the next update the leaves to not move after erasing the horizontalTransition component.
        new ScheduledTask(this, 0.01f, false,
                () -> transform().setVelocity(Vector2.ZERO));
    }

    /**
     * The behavior of a leaf after being created. Waits a while and then starts to vibrate and fall.
     */
    private void leafRoutine() {

        //  starts the vibrations of this leaf in the wind
        float waitTimeBeforeLeafVibrate = this.random.nextFloat();
        int waitTimeUntilLeafFall = this.random.nextInt(MAX_LIFE_TIME_SPAN);
        new ScheduledTask(this, waitTimeBeforeLeafVibrate, true, this::vibrationsRoutine);
        new ScheduledTask(this, waitTimeUntilLeafFall, false, this::fallingLeafRoutine);
    }


    /**
     * Sets the way the leaf will sway in the wind when it's on the tree.
     */
    private void vibrationsRoutine() {
        this.angleVibrationEffect = new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                ANGLE_INITIAL_VAL,
                ANGLE_FINAL_VAL,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                VIBRATION_DURATION,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value

        this.dimVibrationEffect = new Transition<Vector2>(this,
                this::setDimensions,
                new Vector2(MIN_DIMENSION, MIN_DIMENSION),
                new Vector2(Block.SIZE, Block.SIZE),
                Transition.LINEAR_INTERPOLATOR_VECTOR, // use a cubic interpolator
                VIBRATION_DURATION, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value

    }

    /**
     * Calls the functions which set the way the leaf fall and fade out from the game.
     */
    private void fallingLeafRoutine() {
        makeLeafFallToTheGround();
        makeLeafFadeOut();
    }


    /**
     * Fades the leaf from the game. After the disappearance of the leaf, calls to the returningToTreeTop function
     * which is responsible for returning the leaf the tree top.
     */
    private void makeLeafFadeOut() {
        this.renderer().fadeOut(FADEOUT_TIME,
                () -> new ScheduledTask(
                        this, this.random.nextInt(MAX_DEATH_TIME_SPAN),
                        false,
                        this::returningToTreeTop));
    }


    /**
     * Sets the way the leaf falls of the tree,
     * as well as setting the way the leaf will sway in the wind while falling.
     */
    private void makeLeafFallToTheGround() {

        this.transform().setVelocityY(Vector2.DOWN.y() * FALL_VELOCITY);
        this.horizontalTransition = new Transition<Float>(this,
                x -> this.transform().setVelocityX(x), // the method to call
                -HORIZONTAL_MOVEMENT_RANGE, // initial transition value
                HORIZONTAL_MOVEMENT_RANGE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a linear interpolator
                HORIZONTAL_MOVEMENT_CYCLE_LENGTH, //
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value
    }


    /**
     * Responsible for returning the leaf to the tree-top. After that, calling the leafRoutine function to start the
     * cycle of the life of the leaf all over again.
     */
    private void returningToTreeTop() {

        this.setCenter(this.initialPositionOfLeaf);
        this.renderer().setOpaqueness(this.opaqueness);

        // start again the life cycle
        leafRoutine();
    }


}
