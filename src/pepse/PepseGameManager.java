package pepse;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.Random;

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
    private static final int FALLING_LEAF_LAYER =  Layer.STATIC_OBJECTS + 10;

    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final float AVATAR_INITIAL_X_POS = 200;


//    PepseGameManager(String windowTitle, Vector2 windowDimensions) {
//        super(windowTitle, windowDimensions);
//    }

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
        Vector2 windowDimensions = windowController.getWindowDimensions();

        // Create game objects
        Sky.create(this.gameObjects(),windowDimensions, SKY_LAYER);
        Night.create(this.gameObjects(), NIGHT_LAYER,windowDimensions,NIGHT_CYCLE_LENGTH);
        GameObject sun = Sun.create(this.gameObjects(),SUN_LAYER,windowDimensions,SUN_CYCLE_LENGTH);
        SunHalo.create(this.gameObjects(),SUN_HALO_LAYER,sun,SUN_HALO_COLOR);

        Random random = new Random();
        int seed = random.nextInt(500);
        Terrain terrain = new Terrain(this.gameObjects(), TOP_GROUND_LAYER, windowDimensions, seed); // initializing the terrain
        terrain.createInRange(0, (int) windowDimensions.x()); // terrain spread on the whole screen.

        Tree trees = new Tree(this.gameObjects(),terrain::groundHeightAt);
        trees.createInRange(0, (int) windowDimensions.x());


        Vector2 avatarInitialPosition = new Vector2(AVATAR_INITIAL_X_POS,terrain.groundHeightAt(AVATAR_INITIAL_X_POS)-Block.SIZE);
        Avatar avatar = Avatar.create(gameObjects(),AVATAR_LAYER,avatarInitialPosition,inputListener,imageReader);
        gameObjects().addGameObject(new GameObject(Vector2.ZERO,Vector2.ZERO,null),FALLING_LEAF_LAYER);
        gameObjects().layers().shouldLayersCollide(FALLING_LEAF_LAYER, TOP_GROUND_LAYER, true);

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
