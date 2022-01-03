package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.*;

import java.awt.*;
import java.util.Map;

/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
//    private static final int MAXIMUM_HEIGHT_OF_TERRAIN = 600;
    private static final String GROUND_TAG = "ground";

    private final float groundHeightAtX0;
    private final PerlinNoise myPerl;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private Map<Integer, CareTaker> cache;
    private Originator originator;



    /**
     * Constructor.
     * @param gameObjects The collection of all participating game objects.
     * @param groundLayer The number of the layer to which the created ground objects should be added.
     * @param windowDimensions The dimensions of the windows.
     * @param seed  A seed for a random number generator.
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed)
    {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = windowDimensions.y() *(2/3f);
        this.myPerl = new PerlinNoise(seed);
        this.originator = new Originator();


    }

    /**
     * This method return the ground height at a given location.
     * @param x A number.
     * @return The ground height at the given location.
     */

    public float groundHeightAt(float x)
    {
        float result = (float) (28 * Block.SIZE * this.myPerl.noise(x/Block.SIZE));
        if (result < 0) {return this.groundHeightAtX0 + Block.SIZE;}
        else if (this.groundHeightAtX0 + result > windowDimensions.y()) {return windowDimensions.y() - 3 * Block.SIZE;}
        return this.groundHeightAtX0 + result;
//        return 500;
    }


    /**
     * This method creates terrain in a given range of x-values.
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    /**
     * This method creates terrain in a given range of x-values.
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX)
    {
        int topY, layer;
        minX =  (minX % Block.SIZE != 0) ? minX - (minX % Block.SIZE) : minX;
        maxX =  (maxX % Block.SIZE != 0) ? maxX + (maxX % Block.SIZE) : maxX;

        for (int x = minX; x <= maxX ; x+=Block.SIZE){
            // if x in hashmap - running on the array list and adding to the game. then quite the function.
            topY = (int) groundHeightAt(x); // highest block for an X coordinate.
            layer = this.groundLayer; // top ground layer
            CareTaker blockCareTaker = new CareTaker();
            for (int y = topY; y < topY + (TERRAIN_DEPTH * Block.SIZE) ; y += Block.SIZE){

                Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(x,y), renderable);
                // the first couple of blocks in each column should be in groundLayer else in groundLayer+2
                if ((y != topY) && (y != topY + Block.SIZE)) {layer = this.groundLayer + 2;}
                this.gameObjects.addGameObject(block, layer);

                originator.setBlockState("in",block,layer);
                blockCareTaker.add(originator.saveStateToMemento());
                block.setTag(GROUND_TAG);
            }
            this.cache.put(x, blockCareTaker);
        }
    }

    public void setCache(Map<Integer, CareTaker> cache)
    {
        this.cache = cache;
    }

}