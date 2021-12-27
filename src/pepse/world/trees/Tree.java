package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.Random;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {

    private static final int MINIMAL_TREE_HEIGHT = 4; //means four blocks
    private static final int MAXIMAL_TREE_HEIGHT = 10;

    static final int TRUNK_LAYER = Layer.STATIC_OBJECTS + 6;
    static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 8;

    private final Random random = new Random();
    private final GameObjectCollection gameObjects;
    private final UnaryOperator<Float> groundHeightFunc;



    public Tree(GameObjectCollection gameObjects, UnaryOperator<Float> groundHeightFunc){
        this.gameObjects = gameObjects;
        this.groundHeightFunc = groundHeightFunc;
    }

    /**
     * This method creates trees in a given range of x-values.
     * @param minX  The lower bound of the given range (will be rounded to a multiple of Block. SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block. SIZE).
     */
    public void createInRange(int minX, int maxX)
    {
        int newMinX = minX;
        int newMaxX = maxX;
        int bottomYBlock;
        int topOfTheTree;
        if (minX % Block.SIZE != 0) newMinX -= minX % Block.SIZE;
        if (maxX % Block.SIZE != 0) newMaxX -= maxX % Block.SIZE;

        Vector2 treeTopTopLeftCorner;
        for (int xBlock = newMinX; xBlock <= newMaxX; xBlock+=Block.SIZE) {
            if (needToPlant()){
                topOfTheTree = getRandomTrunkHeight();
                bottomYBlock = (int) (this.groundHeightFunc.apply((float)xBlock)- Block.SIZE);
                treeTopTopLeftCorner = new Vector2(xBlock - ((topOfTheTree/2f)*Block.SIZE),
                        (bottomYBlock - topOfTheTree*Block.SIZE) - (topOfTheTree/2f)*Block.SIZE);
                Trunk.createTrunk(this.gameObjects,new Vector2(xBlock, bottomYBlock), topOfTheTree);
                TreeTop.createTreeTop(this.gameObjects,treeTopTopLeftCorner,topOfTheTree);
            }

        }
    }


    private int getRandomTrunkHeight()
    {
        return this.random.nextInt(MAXIMAL_TREE_HEIGHT-MINIMAL_TREE_HEIGHT) + MINIMAL_TREE_HEIGHT;
    }

    private boolean needToPlant()
    {
        return (float) random.nextInt(100) < 10;
    }

}
