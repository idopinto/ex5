package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class TreeTop {

    private static final Color LEAF_COLOR =new Color(50, 200, 30);
    private static final String LEAF_TAG = "leaf";


    static void createTreeTop(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,int  leafInRow)
    {
        int x ;
        int y = (int) topLeftCorner.y();
        for (int i = 0; i < leafInRow; i++) {
            x = (int) topLeftCorner.x();
            for (int j = 0; j < leafInRow; j++) {
                Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
                Leaf leaf = new Leaf(new Vector2(x,y),renderable);
                gameObjects.addGameObject(leaf,layer);

                leaf.setTag(LEAF_TAG);
                x += Block.SIZE;
            }
            y +=Block.SIZE;
        }
    }


}
