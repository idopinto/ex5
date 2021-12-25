package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.util.Random;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {

    private final Random random = new Random();


    /**
     * This method creates trees in a given range of x-values.
     * @param minX  The lower bound of the given range (will be rounded to a multiple of Block. SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block. SIZE).
     */
    public void createInRange(int minX, int maxX)
    {
//        int newMinX = minX;
//        int newMaxX = maxX;
//        int topYBlock;
//        if (minX % Block.SIZE != 0) newMinX -= minX % Block.SIZE;
//        if (maxX % Block.SIZE != 0) newMaxX -= maxX % Block.SIZE;
//
//        for (int xBlock = newMinX; xBlock <= newMaxX; xBlock+=Block.SIZE){
//            topYBlock = (int) groundHeightAt(xBlock); // highest block for an X coordinate.
//            for (int yBlock = topYBlock; yBlock < topYBlock + (TERRAIN_DEPTH*Block.SIZE) ; yBlock+=Block.SIZE){
//                Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
//                Block block = new Block(new Vector2(xBlock,yBlock), renderable);
//                this.gameObjects.addGameObject(block, this.groundLayer);
//                block.setTag(GROUND_TAG);
//            }
//        }
    }

//    public int getRandomTrunkHeight()
//    {
//        return 0;
//    }
//
    public boolean needToPlant(int col)
    {
        return (float) random.nextInt(100) < 10;
    }
//
//    // maybe TreeLocationFactory for that return random location for the tree
//    // random in range(0,1)
//
//    // if it is random <= 0.1 then we return true for this location





}
