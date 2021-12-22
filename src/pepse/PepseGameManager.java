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

/**
 * The main class of the simulator.
 */
public class PepseGameManager extends danogl.GameManager{

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
        super.initializeGame(imageReader,soundReader,inputListener,windowController);
        Sky.create(this.gameObjects(),windowController.getWindowDimensions(), Layer.BACKGROUND);
        Night.create(this.gameObjects(), Layer.FOREGROUND,windowController.getWindowDimensions(),30);
        Terrain terrain = new Terrain(this.gameObjects(), Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(),10); // initializing the terrain
        terrain.createInRange(0, (int) windowController.getWindowDimensions().x()); // terrain spread on the whole screen.


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
