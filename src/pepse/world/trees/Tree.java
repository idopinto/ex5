package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.util.BlockCareTaker;
import pepse.util.BlockOriginator;
import pepse.world.Block;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.UnaryOperator;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {

    private static final int MINIMAL_HEIGHT = 6; //means four blocks
    private static final int MAXIMAL_HEIGHT = 10;

    static final int TRUNK_LAYER = Layer.STATIC_OBJECTS + 6;
    static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 8;

    private final GameObjectCollection gameObjects;
    private final UnaryOperator<Float> groundHeightFunc;
    private int seed;
    private Random random;
    private Map<Integer, BlockCareTaker> cache;
    static final BlockOriginator originator = new BlockOriginator();


// added seed in constructor

    /**
     * Construcor.
     *
     * @param gameObjects      - The collection of objects.
     * @param groundHeightFunc - A function which gets a float (x coordinate) and returns the height of the terrain
     *                         at that x coordinate.
     */
    public Tree(GameObjectCollection gameObjects, UnaryOperator<Float> groundHeightFunc) {
        this.gameObjects = gameObjects;
        this.groundHeightFunc = groundHeightFunc;
        this.random = new Random(seed);
    }

    /**
     * This method creates trees in a given range of x-values.
     *
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block. SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block. SIZE).
     */
    public void createInRange(int minX, int maxX) {

        int bottomYBlock;
        int treeHeight;
        minX = (minX % Block.SIZE != 0) ? minX - (minX % Block.SIZE) : minX;
        maxX = (maxX % Block.SIZE != 0) ? maxX + (maxX % Block.SIZE) : maxX;

        Vector2 treeTopTopLeftCorner;
        for (int x = minX; x <= maxX; x += 3 * Block.SIZE) {

            this.random = new Random(Objects.hash(x, seed));
            BlockCareTaker careTaker = this.cache.get(x);
            if (needToPlant()) {
                treeHeight = getRandomHeight();

                bottomYBlock = (int) (this.groundHeightFunc.apply((float) x) - Block.SIZE);
                treeTopTopLeftCorner = new Vector2(x - ((treeHeight / 2f) * Block.SIZE),
                        (bottomYBlock - treeHeight * Block.SIZE) - (treeHeight / 2f) * Block.SIZE);
                Trunk.createTrunk(this.gameObjects, new Vector2(x, bottomYBlock), treeHeight, careTaker);
                TreeTop.createTreeTop(this.gameObjects, treeTopTopLeftCorner, treeHeight, seed, careTaker);
            }

        }
    }


    /**
     * The function sets a random height for a tree.
     *
     * @return The height of the tree in Block.Size value.
     */
    private int getRandomHeight() {
        return this.random.nextInt(MAXIMAL_HEIGHT - MINIMAL_HEIGHT) +
                MINIMAL_HEIGHT;
    }

    /**
     * Decides whether a tree should be planted in the current x coordinate.
     *
     * @return True if should a tree be planted, false otherwise.
     */
    private boolean needToPlant() {
        return random.nextInt(100) < 10;
    }

    /**
     * Stets the hashMap which contains the x coordinates as keys and BlockCareTaker as values as th field variable.
     *
     * @param cache the hashMap which contains the x coordinates as keys and BlockCareTaker as values.
     */
    public void setCache(Map<Integer, BlockCareTaker> cache) {
        this.cache = cache;
    }

    /**
     * Sets the seed which according to it the random values will be generated.
     *
     * @param seed The seed of the game.
     */
    public void setSeed(int seed) {
        this.seed = seed;
    }
}
