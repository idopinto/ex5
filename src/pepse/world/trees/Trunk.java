package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class Trunk {
    private static final Color TRUNK_COLOR =new Color(100, 50, 20);
    private static final String TRUNK_TAG = "trunkBlock";



    static void createTrunk (Vector2 trunkLocation, int trunkHeight, int layer, GameObjectCollection gameObjects)
    {
        int counter = 0;
        for (int currentHeightOfTree = 0; currentHeightOfTree < trunkHeight;
             currentHeightOfTree ++){
            Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
            Block trunkBlock = new Block(new Vector2(trunkLocation.x(),
                    trunkLocation.y() - Block.SIZE*counter), renderable);
            gameObjects.addGameObject(trunkBlock, layer);
            trunkBlock.setTag(TRUNK_TAG);
            counter ++;
        }
    }





}
