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

public class Leaf {

    private static final Color LEAF_COLOR =new Color(50, 200, 30);

    static Block create(GameObjectCollection gameObjects, Vector2 leafTopLeftCorner, int layer,float waitTime){
        Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
        Block leaf = new Block(leafTopLeftCorner,renderable);
        gameObjects.addGameObject(leaf,layer);
        leaf.setTag("leaf");

        new Transition<Float>(leaf,
                leaf.renderer()::setRenderableAngle,
                0f,
                15f,
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

        new ScheduledTask(leaf,waitTime,true,()->{});

        return leaf;
    }


}
