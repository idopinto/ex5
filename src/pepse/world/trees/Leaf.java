package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class Leaf extends Block {

    private static final int FADEOUT_TIME = 15;
    private static final float HORIZONTAL_MOVEMENT_RANGE = 50;
    private static final float HORIZONTAL_MOVEMENT_CYCLE_LENGTH = 2;
    private static final int MAX_DEATH_TIME_SPAN = 20;
    private static final int MAX_LIFE_TIME_SPAN = 50;
    private static final int FALL_VELOCITY = 20;
    private final Random random = new Random();
    private final Vector2 initialPositionOfLeaf;
    private Transition<Float> horizontalTransition;
    private Transition<Float> movingAngle;
    private Transition<Vector2> movingDimensions;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner The location of the top-left corner of the created block
     * @param renderable    - A renderable to render as the block.
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        this.initialPositionOfLeaf = topLeftCorner;
        physics().setMass(5f);
        leafRoutine();

    }


    private void leafRoutine() {
        new ScheduledTask(this, random.nextFloat(), true, this::makeItMove);
        new ScheduledTask(this, random.nextInt(MAX_LIFE_TIME_SPAN), false, this::leafFallRoutine);
    }

    private void makeItMove() {
        this.movingAngle = new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                0f,
                15f,
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

    private void leafFallRoutine() {
        makeItFall();
        makeItFade();
    }


    private void makeItFade() {
        this.renderer().fadeOut(FADEOUT_TIME, this::deathRoutine);
    }


    private void deathRoutine() {
        new ScheduledTask(this, random.nextInt(MAX_DEATH_TIME_SPAN), false, () -> {
        });
    }


    private void makeItFall() {
        this.transform().setVelocityY(Vector2.DOWN.y() * FALL_VELOCITY);
        this.horizontalTransition = new Transition<Float>(
                this, // the game object being changed
                x -> {
                    this.transform().setVelocityX(x);
                }, // the method to call
                -HORIZONTAL_MOVEMENT_RANGE, // initial transition value
                HORIZONTAL_MOVEMENT_RANGE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a linear interpolator
                HORIZONTAL_MOVEMENT_CYCLE_LENGTH, // transition fully over cycleLength
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value
    }


    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("ground")) {
            this.transform().setVelocity(Vector2.ZERO);
            this.removeComponent(this.horizontalTransition);
            this.removeComponent(this.movingAngle);
            this.removeComponent(this.movingDimensions);
        }


    }


}
