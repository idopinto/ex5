package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.util.CareTaker;
import pepse.util.BlockMemento;
import pepse.util.Originator;
import pepse.world.Block;

import java.lang.reflect.Member;
import java.util.List;
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
    private Map<Integer, CareTaker> cache;
    static final Originator originator = new Originator();


// added seed in constructor

    public Tree(GameObjectCollection gameObjects, UnaryOperator<Float> groundHeightFunc){
        this.gameObjects = gameObjects;
        this.groundHeightFunc = groundHeightFunc;
        this.random = new Random(seed);
    }

    /**
     * This method creates trees in a given range of x-values.
     * @param minX  The lower bound of the given range (will be rounded to a multiple of Block. SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block. SIZE).
     */
    public void createInRange(int minX, int maxX)
    {

        int bottomYBlock;
        int treeHeight;
        minX =  (minX % Block.SIZE != 0) ? minX - (minX % Block.SIZE) : minX;
        maxX =  (maxX % Block.SIZE != 0) ? maxX + (maxX % Block.SIZE) : maxX;

        Vector2 treeTopTopLeftCorner;
        for (int x = minX; x <= maxX; x += 3*Block.SIZE) {

            this.random = new Random(Objects.hash(x,seed));
            CareTaker careTaker = this.cache.get(x);
            if (needToPlant()){
                treeHeight = getRandomHeight();

                bottomYBlock = (int) (this.groundHeightFunc.apply((float)x)- Block.SIZE);
                treeTopTopLeftCorner = new Vector2(x - ((treeHeight/2f)*Block.SIZE),
                        (bottomYBlock - treeHeight*Block.SIZE) - (treeHeight/2f)*Block.SIZE);
                Trunk.createTrunk(this.gameObjects,new Vector2(x, bottomYBlock), treeHeight,careTaker);
                TreeTop.createTreeTop(this.gameObjects,treeTopTopLeftCorner,treeHeight,seed,careTaker);
            }

        }
    }


    private int getRandomHeight() {return this.random.nextInt(MAXIMAL_HEIGHT - MINIMAL_HEIGHT) + MINIMAL_HEIGHT;}

    private boolean needToPlant()
    {
        return random.nextInt(100) < 10;
    }

    public void setCache(Map<Integer, CareTaker> cache)
    {
        this.cache = cache;
    }
    public void setSeed(int seed)
    {
        this.seed = seed;
    }
}
