package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.BlockCareTaker;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class Trunk {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final String TRUNK_TAG = "trunk";
    private static final String IN_STATE = "in";


    /**
     * Creates the trunk of a tree.
     *
     * @param gameObjects    The collection of objects.
     * @param trunkLocation  starting location of the first block of the tree which should be created.
     * @param trunkHeight    The number of blocks of Block.size size which should be created for the trunk.
     * @param blockCareTaker The CareTaker of a current x coordinate which contains the arrayList of mementos which
     *                       represents the blocks which appears at the x coordinates, including leaves' blocks.
     */
    static void createTrunk(GameObjectCollection gameObjects, Vector2 trunkLocation, int trunkHeight, BlockCareTaker blockCareTaker) {
        int counter = 0;
        for (int currentHeightOfTree = 0; currentHeightOfTree < trunkHeight; currentHeightOfTree++) {
            Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
            Block trunkBlock = new Block(new Vector2(trunkLocation.x(),
                    trunkLocation.y() - Block.SIZE * counter), renderable);
            gameObjects.addGameObject(trunkBlock, Tree.TRUNK_LAYER);

            Tree.originator.setBlockState(trunkBlock, Tree.TRUNK_LAYER, IN_STATE);
            blockCareTaker.add(Tree.originator.saveStateToMemento());
            trunkBlock.setTag(TRUNK_TAG);
            counter++;
        }
    }


}
