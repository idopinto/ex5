package pepse.world.trees;

import danogl.collisions.GameObjectCollection;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.BlockCareTaker;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class TreeTop {

    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final String LEAF_TAG = "leaf";
    private static final String IN_STATE = "in";


    /**
     * Creates the tree-top.
     *
     * @param gameObjects    The collection of objects.
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     * @param leafInRow      The height of the tree.
     * @param seed           The seed which according to it the random values will be generated.
     * @param blockCareTaker The CareTaker of a current x coordinate which contains the arrayList of mementos which
     *                       represents the blocks which appears at the x coordinates, including leaves' blocks.
     */
    static void createTreeTop(GameObjectCollection gameObjects, Vector2 topLeftCorner,
                              int leafInRow, int seed, BlockCareTaker blockCareTaker) {
        int x, y = (int) topLeftCorner.y();
        for (int i = 0; i < leafInRow; i++) {
            x = (int) topLeftCorner.x();
            for (int j = 0; j < leafInRow; j++) {
                Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
                Leaf leaf = new Leaf(gameObjects, new Vector2(x, y), renderable, seed);

                gameObjects.addGameObject(leaf, Tree.LEAF_LAYER);
                Tree.originator.setBlockState(leaf, Tree.LEAF_LAYER, IN_STATE);
                blockCareTaker.add(Tree.originator.saveStateToMemento());
                leaf.setTag(LEAF_TAG);
                x += Block.SIZE;
            }
            y += Block.SIZE;
        }
    }


}
