package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class Leaf {

    private static final Color LEAF_COLOR =new Color(50, 200, 30);

    static Block create(GameObjectCollection gameObjects, Vector2 leafTopLeftCorner, int layer){
        Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
        Block leaf =new Block(leafTopLeftCorner,renderable);
        gameObjects.addGameObject(leaf,layer);
        leaf.setTag("leaf");

        return leaf;
    }


}
