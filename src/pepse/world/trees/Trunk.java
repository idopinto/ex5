package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.CareTaker;
import pepse.util.ColorSupplier;
import pepse.util.BlockMemento;
import pepse.world.Block;

import java.awt.*;
import java.util.List;

public class Trunk {
    private static final Color TRUNK_COLOR =new Color(100, 50, 20);
    private static final String TRUNK_TAG = "trunk";



    static void createTrunk(GameObjectCollection gameObjects, Vector2 trunkLocation, int trunkHeight, CareTaker blockCareTaker)
    {
        int counter = 0;
        for (int currentHeightOfTree = 0; currentHeightOfTree < trunkHeight; currentHeightOfTree ++){
            Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
            Block trunkBlock = new Block(new Vector2(trunkLocation.x(),
                    trunkLocation.y() - Block.SIZE*counter), renderable);
            gameObjects.addGameObject(trunkBlock, Tree.TRUNK_LAYER);

            Tree.originator.setState("in",trunkBlock,Tree.TRUNK_LAYER);
            blockCareTaker.add(Tree.originator.saveStateToMemento());
//            cache.get((int)trunkLocation.x()).add(trunkBlock);
            trunkBlock.setTag(TRUNK_TAG);
            counter ++;
        }
    }





}
