package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import pepse.util.ColorSupplier;

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
