package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.PerlinNoise;
import java.awt.*;
import java.lang.Math;
import java.util.Random;

/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private static final int MAXIMUM_HEIGHT_OF_TERRAIN = 600;
    private static final String GROUND_TAG = "ground";

    private final int groundHeightAtX0;
    private final PerlinNoise myPerl;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final Random random = new Random();

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
        this.myPerl = new PerlinNoise(seed);
        this.groundHeightAtX0 = MAXIMUM_HEIGHT_OF_TERRAIN;
    }

    /**
     * This method return the ground height at a given location.
     * @param x A number.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x)
    {
        float result = (float) (Block.SIZE *this.myPerl.noise(x/Block.SIZE)*20);
        if (result < 0) return this.groundHeightAtX0 + this.random.nextInt(
                (int) windowDimensions.y() - this.groundHeightAtX0 - 100);
        else if (this.groundHeightAtX0 + result > windowDimensions.y()) return windowDimensions.y() - 60;
        return this.groundHeightAtX0 + result;
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

        for (int xBlock = newMinX; xBlock <= newMaxX; xBlock+=Block.SIZE){
            topYBlock = (int) groundHeightAt(xBlock); // highest block for an X coordinate.
            for (int yBlock = topYBlock; yBlock < topYBlock + (TERRAIN_DEPTH*Block.SIZE) ; yBlock+=Block.SIZE){
                Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(xBlock,yBlock), renderable);
                this.gameObjects.addGameObject(block, this.groundLayer);
                block.setTag(GROUND_TAG);
            }
        }
    }

}
