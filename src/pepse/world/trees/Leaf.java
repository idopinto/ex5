package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class Leaf {

    private static final Color LEAF_COLOR =new Color(50, 200, 30);
    private static final String LEAF_TAG = "leaf";
    private Block leaf;


    public Block create(GameObjectCollection gameObjects, Vector2 leafTopLeftCorner, int layer){
        Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
        leaf = new Block(leafTopLeftCorner,renderable);
        gameObjects.addGameObject(leaf,layer);
        leaf.setTag(LEAF_TAG);
        new ScheduledTask(leaf,new Random().nextFloat() ,true,this::makeItMove);
        leaf.renderer().fadeOut(6);
        return leaf;
    }


    private void makeItMove()
    {
        new Transition<Float>(leaf,
                leaf.renderer()::setRenderableAngle,
                0f,
                50f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                3, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value

        new Transition<Vector2>(leaf,
                leaf::setDimensions,
                new Vector2(26,26),
                new Vector2(30,30),
                Transition.LINEAR_INTERPOLATOR_VECTOR, // use a cubic interpolator
                3, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value

//        new Transition<Vector2>(leaf,
//                leaf.renderer()::fadeOut,
//                new
//        )
    }

    private void makeItFall(){

    }


}
