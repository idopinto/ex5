package pepse;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
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
    private static final float WINDOW_WIDTH = 1500;
    private static final float WINDOW_HEIGHT = 840;


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
        Sky.create(this.gameObjects(),windowDimensions, Layer.BACKGROUND);
        Night.create(this.gameObjects(), Layer.FOREGROUND,windowDimensions,NIGHT_CYCLE_LENGTH);
        GameObject sun = Sun.create(this.gameObjects(),Layer.BACKGROUND,windowDimensions,SUN_CYCLE_LENGTH);
        SunHalo.create(this.gameObjects(),Layer.BACKGROUND + 10,sun,new Color(255, 255, 0, 20));

        Random random = new Random();
        int seed = random.nextInt(500);
        Terrain terrain = new Terrain(this.gameObjects(), Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(), seed); // initializing the terrain

        terrain.createInRange(0, (int) windowDimensions.x()); // terrain spread on the whole screen.
        Tree trees = new Tree(this.gameObjects(), terrain::groundHeightAt);
        trees.createInRange(0, (int) windowDimensions.x());
        gameObjects().layers().shouldLayersCollide(Layer.STATIC_OBJECTS+10, Layer.STATIC_OBJECTS,
                true);
    }

    /**
     * Runs the entire simulation.
     * @param args args
     */
    public static void main(String[] args)
    {
//        PepseGameManager game = new PepseGameManager("pepse",new Vector2(WINDOW_WIDTH,WINDOW_HEIGHT));
//        game.run();

        new PepseGameManager().run();
    }

}
