package pepse;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;

/**
 * The main class of the simulator.
 */
public class PepseGameManager extends danogl.GameManager{

    private static final int NIGHT_CYCLE_LENGTH = 30;
    private static final int SUN_CYCLE_LENGTH = 30;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final int TOP_GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final int LEAF_LAYER =  Layer.STATIC_OBJECTS + 8;
    private static final int SEED = 100;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final float AVATAR_INITIAL_X_POS = 600;
    private Counter energyCounter;
    private Avatar avatar;
    private Terrain terrain;
    private int farMargin;
     private int maxX;
    private int closeMargin;
    private int minX;
//    private int seed;
    private Vector2 windowDimensions;
    private Tree trees;


    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     * @param soundReader soundReader Contains a single method: readSound, which reads a wav file from disk.
     * @param inputListener inputListener Contains a single method: isKeyPressed, which returns whether a given key is currently pressed by the user or not.
     * @param windowController windowController Contains an array of helpful, self explanatory methods concerning the window.
     */
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                    UserInputListener inputListener, WindowController windowController)
    {
        // Override GameObject initializeGame
        super.initializeGame(imageReader,soundReader,inputListener,windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        this.minX = 0;
        this.maxX = (int) windowDimensions.x();
        this.terrain = new Terrain(this.gameObjects(), TOP_GROUND_LAYER, windowDimensions, SEED);
        this.trees = new Tree(this.gameObjects(),this.terrain::groundHeightAt,SEED);

        createBackground();
        generateInitialScenery();
        generateAvatar(inputListener,imageReader);
        gameObjects().layers().shouldLayersCollide(LEAF_LAYER, TOP_GROUND_LAYER, true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        generateWorldRight();
        generateWorldLeft();


    }


    private void generateWorldRight()
    {
        int start = this.maxX, extraTerrainCols = 3 * Block.SIZE;
        int diffBetweenMaxToAvatar = start - (int)this.avatar.getCenter().x();

        int end = start + (this.farMargin - diffBetweenMaxToAvatar + extraTerrainCols);
//        int end = start + (int)this.windowDimensions.x();
        if (diffBetweenMaxToAvatar < this.farMargin)
        {
            this.terrain.createInRange(start, end);
//            this.trees.createInRange(start, end);
            this.maxX += this.farMargin - diffBetweenMaxToAvatar;
//            this.maxX += end - start -extraTerrainCols;
            this.minX += this.farMargin - diffBetweenMaxToAvatar;

        }
//        if (this.avatar.getCenter().x() < this.closeMargin){
//            this.minX -= this.closeMargin - this.avatar.getCenter().x();
//        }
        removeOldObjects();
    }

    private void removeOldObjects() {

        for (GameObject gameObject: gameObjects())
        {
            if ((gameObject instanceof Block) && (gameObject.getCenter().x() < this.minX)){

                gameObjects().removeGameObject(gameObject,TOP_GROUND_LAYER);
                gameObjects().removeGameObject(gameObject,TOP_GROUND_LAYER + 2);

            }
        }
    }

    private void generateWorldLeft()
    {

    }

    private void generateAvatar(UserInputListener inputListener,ImageReader imageReader)
    {
        //        gameObjects().addGameObject(new GameObject(Vector2.ZERO,Vector2.ZERO,null),FALLING_LEAF_LAYER);

        Vector2 avatarInitialPosition = new Vector2(maxX/2f, this.terrain.groundHeightAt(maxX/2f)-Block.SIZE);
        this.avatar = Avatar.create(gameObjects(),AVATAR_LAYER, avatarInitialPosition, inputListener, imageReader);
        this.closeMargin = (int)this.avatar.getCenter().x();
        this.farMargin = this.maxX - this.closeMargin;


        setCamera(new Camera(this.avatar,
                this.windowDimensions.mult(0.5f).subtract(avatarInitialPosition),
                this.windowDimensions,
                this.windowDimensions));

    }


//    private void setInitialGameSeed()
//    {
//
//        //        Random random = new Random();
//        //        int seed = random.nextInt(500);
//        this.seed = 25;
//    }


    private void generateInitialScenery()
    {
        // initializing the terrain
        this.terrain = new Terrain(this.gameObjects(), TOP_GROUND_LAYER, windowDimensions, SEED);
        // terrain spread on the whole screen.
        terrain.createInRange(minX, maxX);

        this.trees = new Tree(this.gameObjects(),this.terrain::groundHeightAt,SEED);
        trees.createInRange(minX, maxX);
    }


    private void createBackground()
    {
        Sky.create(this.gameObjects(),windowDimensions, SKY_LAYER);
        Night.create(this.gameObjects(), NIGHT_LAYER,windowDimensions,NIGHT_CYCLE_LENGTH);
        GameObject sun = Sun.create(this.gameObjects(), SUN_LAYER, windowDimensions, SUN_CYCLE_LENGTH);
        SunHalo.create(this.gameObjects(),SUN_HALO_LAYER, sun,SUN_HALO_COLOR);

    }

    /**
     * Runs the entire simulation.
     * @param args args
     */
    public static void main(String[] args)
    {
        new PepseGameManager().run();
    }

}
