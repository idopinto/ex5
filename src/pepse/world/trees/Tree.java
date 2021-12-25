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

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {

    private static final int MINIMAL_TREE_HEIGHT = 4; //means four blocks
    private static final int MAXIMAL_TREE_HEIGHT = 10;

    private final Random random = new Random();
    private final GameObjectCollection gameObjects;
    private Terrain terrain;



    public Tree (GameObjectCollection gameObjects, Terrain terrain){
        this.gameObjects = gameObjects;
        this.terrain = terrain;
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
        if (minX % Block.SIZE != 0) newMinX -= minX % Block.SIZE;
        if (maxX % Block.SIZE != 0) newMaxX -= maxX % Block.SIZE;
        int leafInRow;
        int topOfTheTree;
        Vector2 treeTopTopLeftCorner;
        for (int xBlock = newMinX; xBlock <= newMaxX; xBlock+=Block.SIZE) {
            if (needToPlant()){
                topOfTheTree = getRandomTrunkHeight();
                leafInRow = (int)topOfTheTree/2;
                bottomYBlock = (int) terrain.groundHeightAt(xBlock)- Block.SIZE +
                        (int) terrain.groundHeightAt(xBlock)%Block.SIZE;
                treeTopTopLeftCorner = new Vector2(xBlock - (leafInRow/2f)*Block.SIZE,
                        (bottomYBlock + topOfTheTree*Block.SIZE) - (leafInRow/2f)*Block.SIZE);
                Trunk.createTrunk(new Vector2(xBlock, bottomYBlock), topOfTheTree, Layer.STATIC_OBJECTS+10,
                        this.gameObjects);
                TreeTop.createTreeTop(this.gameObjects, Layer.BACKGROUND+50,treeTopTopLeftCorner,leafInRow);
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
//
//    // maybe TreeLocationFactory for that return random location for the tree
//    // random in range(0,1)
//
//    // if it is random <= 0.1 then we return true for this location





}
