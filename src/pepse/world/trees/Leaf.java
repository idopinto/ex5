package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import java.util.Random;


public class Leaf extends Block
{

    private static final int FADEOUT_TIME = 15;
    private static final float HORIZONTAL_MOVEMENT_RANGE = 20;
    private static final float HORIZONTAL_MOVEMENT_CYCLE_LENGTH = 2;
    private static final int MAX_DEATH_TIME_SPAN = 50;
    private static final int MAX_LIFE_TIME_SPAN = 100;
    private static final int FALL_VELOCITY = 30;
    private final Random random = new Random();
    private final Vector2 initialPositionOfLeaf;
    private Transition<Float> horizontalTransition;
    private Transition<Float> movingAngle;
    private Transition<Vector2> movingDimensions;
    private final float opaqueness;
    private final GameObjectCollection gameObjects;


    /**
     * Construct a new GameObject instance.
     *
     * @param gameObjects
     * @param topLeftCorner The location of the top-left corner of the created block
     * @param renderable    - A renderable to render as the block.
     */
    public Leaf(GameObjectCollection gameObjects, Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        this.gameObjects = gameObjects;

        // save initial state of the leaf
        this.opaqueness = this.renderer().getOpaqueness();
        this.initialPositionOfLeaf = this.getCenter();

        physics().setMass(5f);
        // start leaf routine
        leafRoutine();

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        this.transform().setVelocity(Vector2.ZERO);

        // Removing all the transitions made before
        this.removeComponent(this.horizontalTransition);
        this.removeComponent(this.movingAngle);
        this.removeComponent(this.movingDimensions);

        // updating in the next update the leaves to not move after erasing the horizontalTransition component.
        new ScheduledTask(this, 0.01f, false,
                () -> transform().setVelocity(Vector2.ZERO));

    }

    private void leafRoutine() {

        //  starts the vibrations of this leaf in the wind
        float waitTimeBeforeLeafVibrate = this.random.nextFloat();
        int waitTimeUntilLeafFall = this.random.nextInt(MAX_LIFE_TIME_SPAN);
        new ScheduledTask(this,waitTimeBeforeLeafVibrate , true, this::vibrationsRoutine);
        new ScheduledTask(this, waitTimeUntilLeafFall, false, this::fallingLeafRoutine);
    }

    private void vibrationsRoutine() {
        this.movingAngle = new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                0f,
                50f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                3, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value

        this.movingDimensions = new Transition<Vector2>(this,
                this::setDimensions,
                new Vector2(26, 26),
                new Vector2(30, 30),
                Transition.LINEAR_INTERPOLATOR_VECTOR, // use a cubic interpolator
                3, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value

    }

    private void fallingLeafRoutine() {
        gameObjects.removeGameObject(this,Tree.LEAF_LAYER);
        gameObjects.addGameObject(this,Tree.LEAF_LAYER + 2);

        // adding a falling leaf to the Layer which collides with the terrain.

        makeLeafFallToTheGround();
        makeLeafFadeOut();
    }


    private void makeLeafFadeOut() {
        int fadeOutTime = this.random.nextInt(FADEOUT_TIME);
        this.renderer().fadeOut(fadeOutTime, ()->{});
        new ScheduledTask(this, fadeOutTime+this.random.nextInt(5), false,
                this::returningToTreeTop);
    }



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


    private void returningToTreeTop() {

        // set the leaf to initial state
        this.renderer().fadeIn(0.1f);
        this.setCenter(this.initialPositionOfLeaf);
        this.renderer().setOpaqueness(this.opaqueness);

        // start again the life cycle
        leafRoutine();
    }




    /*

     */
    private void leafReBirth()
    {
        gameObjects.removeGameObject(this,Tree.LEAF_LAYER + 2);
        gameObjects.addGameObject(this, Tree.LEAF_LAYER);
        new ScheduledTask(this, this.random.nextInt(MAX_DEATH_TIME_SPAN), false, this::returningToTreeTop);

    }


}
