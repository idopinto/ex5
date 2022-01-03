package pepse;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.util.CareTaker;
import pepse.util.Originator;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The main class of the simulator.
 */
public class PepseGameManager extends danogl.GameManager {

    private static final int NIGHT_CYCLE_LENGTH = 30;
    private static final int SUN_CYCLE_LENGTH = 30;

    /* Layers constants*/
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final int TOP_GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 8;
    static final int TRUNK_LAYER = Layer.STATIC_OBJECTS + 6;

    private static final int SEED = 25;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int AVATAR_LAYER = Layer.DEFAULT;

    private Avatar avatar;
    private Terrain terrain;
    private int maxX;
    private int minX;
    private Vector2 windowDimensions;
    private Tree trees;
    private Map<Integer, CareTaker> cache;
    private Originator originator;

//    public PepseGameManager(String windowTitle, Vector2 windowDimensions) {
//        super(windowTitle, windowDimensions);
//    }

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     * @param soundReader      soundReader Contains a single method: readSound, which reads a wav file from disk.
     * @param inputListener    inputListener Contains a single method: isKeyPressed, which returns whether a given key
     *                         is currently pressed by the user or not.
     * @param windowController windowController Contains an array of helpful, self explanatory methods concerning the window.
     */
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        // Override GameObject initializeGame
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        createBackground();

        /*ADD here*/
        this.cache = new HashMap<Integer, CareTaker>();
        this.originator = new Originator();
        this.terrain = new Terrain(gameObjects(), TOP_GROUND_LAYER, windowDimensions, SEED); // initializing the terrain
        this.terrain.setCache(cache);
        this.trees = new Tree(gameObjects(), terrain::groundHeightAt);
        this.trees.setSeed(SEED); trees.setCache(cache);
        generateAvatar(inputListener, imageReader);

        this.minX = (int) (windowDimensions.x() / 2 - (windowDimensions.x()));
        this.maxX = (int) (windowDimensions.x() * 1.5);
        generateSceneryInRange(minX, maxX);

        gameObjects().layers().shouldLayersCollide(LEAF_LAYER, TOP_GROUND_LAYER, true);
        gameObjects().layers().shouldLayersCollide(TRUNK_LAYER, AVATAR_LAYER, true);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        generateWorldLeft();
        generateWorldRight();
    }


    private void generateWorldLeft() {
        int halfScreen = (int) windowDimensions.x() / 2;
        if (Math.abs(this.avatar.getCenter().x() - minX) < halfScreen) {
            if (this.cache.containsKey(minX - Block.SIZE)) {
                System.out.println("------------------------");
                System.out.println("Terrain Restored in range [ " + (minX - halfScreen) + ", " + minX + " ]");
                restoreSceneryInRange(minX - halfScreen, minX);
            } else {
                System.out.println("------------------------");
                System.out.println("Terrain Created in range [ " + (minX - halfScreen) + ", " + minX + " ]");
                generateSceneryInRange(minX - halfScreen, minX);
            }

            removeSceneryInRange(maxX - halfScreen, maxX);
            this.maxX -= halfScreen;
            System.out.println("Terrain Removed in range [ " + (maxX - halfScreen) + ", " + maxX + " ]");
            System.out.println("------------------------");
            this.minX -= halfScreen;
        }
    }




    private void generateWorldRight() {
        int halfScreen = (int) windowDimensions.x() / 2;
        if (Math.abs(this.avatar.getCenter().x() - maxX) < halfScreen) {
            if (this.cache.containsKey(maxX+Block.SIZE)) {

                System.out.println("------------------------");
                System.out.println("Terrain Restored in range [ " + (maxX) + ", " + (maxX+halfScreen )+ " ]");
                restoreSceneryInRange(maxX ,maxX+ halfScreen);

            } else {
                System.out.println("------------------------");
                System.out.println("Terrain Created in range [ " + maxX + ", " + (maxX + halfScreen) + " ]");
                generateSceneryInRange(maxX, maxX + halfScreen);
            }
            this.maxX += halfScreen;

            removeSceneryInRange(minX, minX + halfScreen);
            System.out.println("Terrain Removed in range [ " + minX + ", " + (minX + halfScreen) + " ]");
            System.out.println("------------------------");
            this.minX += halfScreen;
        }
    }


    private void restoreSceneryInRange(int minX0,int maxX0) {
        for (int i = minX0; i < maxX0; i+=Block.SIZE) {
            if (this.cache.containsKey(i)) {
                restoreSceneryFromCacheAt(i);
            }
        }
    }

    private void restoreSceneryFromCacheAt(int nextCol) {
        CareTaker careTaker = this.cache.get(nextCol);

        for (int i = 0; i < careTaker.size(); i++) {
            originator.getStateFromMemento(careTaker.get(i));
            if (originator.getState().equals("out"))
            {
                gameObjects().addGameObject(originator.getBlock(), originator.getLayer());
                originator.setState("in");
                careTaker.saveState(originator,i);
            }
        }

    }

    private void removeSceneryInRange(int low, int high) throws NullPointerException {
        for (int i = low; i < high; i+=Block.SIZE) {
            if (this.cache.containsKey(i)) {
                CareTaker careTaker = this.cache.get(i);
                for (int j = 0; j < careTaker.size(); j++) {
                    originator.getStateFromMemento(careTaker.get(j));
                    if (originator.getState().equals("in"))
                    {
                        gameObjects().removeGameObject(originator.getBlock(), originator.getLayer());
                        originator.setState("out");
                        careTaker.saveState(originator,j);
                    }
                }
                }

            }

        }




    private void generateAvatar(UserInputListener inputListener, ImageReader imageReader) {
        Vector2 avatarInitialPosition = new Vector2(windowDimensions.x() / 2f,
                this.terrain.groundHeightAt(maxX / 2f) - Block.SIZE);
        this.avatar = Avatar.create(gameObjects(), AVATAR_LAYER, avatarInitialPosition, inputListener, imageReader);
        gameObjects().addGameObject(new GameObject(Vector2.ZERO, Vector2.ZERO, null), LEAF_LAYER);
        setCamera(new Camera(this.avatar,
                this.windowDimensions.mult(0.5f).subtract(avatarInitialPosition),
                this.windowDimensions,
                this.windowDimensions));
    }

    private void generateSceneryInRange(int minX0, int maxX0) {

        terrain.createInRange(minX0, maxX0); // terrain spread on the whole screen.
        trees.createInRange(minX0, maxX0);
    }

    private void createBackground() {
        Sky.create(this.gameObjects(), windowDimensions, SKY_LAYER);
        Night.create(this.gameObjects(), NIGHT_LAYER, windowDimensions, NIGHT_CYCLE_LENGTH);
        GameObject sun = Sun.create(this.gameObjects(), SUN_LAYER, windowDimensions, SUN_CYCLE_LENGTH);
        SunHalo.create(this.gameObjects(), SUN_HALO_LAYER, sun, SUN_HALO_COLOR);
    }

    /**
     * Runs the entire simulation.
     * @param args args
     */
    public static void main(String[] args) {
//        new PepseGameManager("pepse", new Vector2(900, 900)).run();
        new PepseGameManager().run();

    }

}
