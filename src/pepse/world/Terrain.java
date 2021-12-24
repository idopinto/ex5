package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.lang.Math;
/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;

    private final int groundHeightAtX0;
    private GameObjectCollection gameObjects;
    private int groundLayer;
    private Vector2 windowDimensions;
    private int seed;

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
        this.seed = seed;
        this.groundHeightAtX0 = (int) windowDimensions.y() - 100; //600
    }

    /**
     * This method return the ground height at a given location.
     * @param x A number.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x)
    {
//        return (float) Math.sin(x);
        return this.groundHeightAtX0;
    }

    /**
     * This method creates terrain in a given range of x-values.
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX)
    {
        int newMinX = minX;
        int newMaxX = maxX;
        int topYBlock;
        if (minX % Block.SIZE != 0) newMinX -= minX % Block.SIZE;
        if (maxX % Block.SIZE != 0) newMaxX -= maxX % Block.SIZE;
        // in inner for loop for different color each block
        Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        for (int xBlock = newMinX; xBlock <= newMaxX; xBlock+=Block.SIZE){
            topYBlock = ((int)(groundHeightAt(60)/Block.SIZE)) * Block.SIZE; // highest block for an X coordinate.
            for (int yBlock = topYBlock; yBlock < topYBlock + TERRAIN_DEPTH*Block.SIZE ; yBlock+=Block.SIZE){
                this.gameObjects.addGameObject(new Block(new Vector2(xBlock,yBlock), renderable), this.groundLayer);
            }
        }
    }

}
