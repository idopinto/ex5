package pepse;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
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
import java.util.ArrayList;
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
    private int lastAvatarLocation;
    private Map<Integer, ArrayList<Block>> cache;

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     * @param soundReader soundReader Contains a single method: readSound, which reads a wav file from disk.
     * @param inputListener inputListener Contains a single method: isKeyPressed, which returns whether a given key is currently pressed by the user or not.
     * @param windowController windowController Contains an array of helpful, self explanatory methods concerning the window.
     */
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                    UserInputListener inputListener, WindowController windowController) {
        // Override GameObject initializeGame
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        createBackground();
        this.minX = 0;
        this.maxX = (int) windowDimensions.x() + (int) windowDimensions.x() % Block.SIZE;
        this.cache = new HashMap<Integer, ArrayList<Block>>();
        generateInitialScenery();
        generateAvatar(inputListener, imageReader);
        this.lastAvatarLocation = (int) this.avatar.getCenter().x();
        gameObjects().layers().shouldLayersCollide(LEAF_LAYER, TOP_GROUND_LAYER, true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        generateWorldRight();
//        generateWorldLeft();
    }


    private void generateWorldRight() {
        int start = this.lastAvatarLocation;
//        System.out.println("last: "+ start);
//        System.out.println("center: "+ (int)this.avatar.getCenter().x());
        int distance = start - (int) this.avatar.getCenter().x();

        int end = this.maxX - distance;

        boolean movedRight = distance < 0;
//        System.out.println(movedRight);

        if (movedRight) {
            this.terrain.createInRange(end + distance, end);
//            this.trees.createInRange(end + distance, end);
            this.lastAvatarLocation = (int) this.avatar.getCenter().x();
            this.maxX += Block.SIZE;
            removeTerrainAndTreeAtX0(this.minX);
            this.minX += Block.SIZE;
        }

    }

    private void removeTerrainAndTreeAtX0(int x) {
        for (Block block : this.cache.get(x)) {
            gameObjects().removeGameObject(block,TOP_GROUND_LAYER + 2);
//            System.out.println("hi");
        }
//        for (Block block : this.cache.get(this.minX)) {
//            if (block == null) {
//                System.out.println("BOOM");
//            }
//            gameObjects().removeGameObject(block);
//        }
    }
//        for (GameObject gameObject: this.gameObjects().objectsInLayer(TOP_GROUND_LAYER))
//        {
//            if (gameObject.getCenter().x() < this.minX){
//                gameObjects().removeGameObject(gameObject,TOP_GROUND_LAYER);
//            }
//        }
//
//        for (GameObject gameObject: this.gameObjects().objectsInLayer(TOP_GROUND_LAYER+2))
//        {
//            if (gameObject.getCenter().x() < this.minX){
//                gameObjects().removeGameObject(gameObject,TOP_GROUND_LAYER + 2);
//            }
//        }
//
//        for (GameObject gameObject: this.gameObjects().objectsInLayer(LEAF_LAYER))
//        {
//            if (gameObject.getCenter().x() < this.minX){
//                gameObjects().removeGameObject(gameObject,LEAF_LAYER);
//            }
//        }
//
//        for (GameObject gameObject: this.gameObjects().objectsInLayer(TRUNK_LAYER))
//        {
//            if (gameObject.getCenter().x() < this.minX){
//                gameObjects().removeGameObject(gameObject,TRUNK_LAYER);
//            }
//        }


//        System.out.println(this.minX);
//        if (this.cache.get(this.minX) == null) {
//            System.out.println("BOOM");
//        }
//    private void generateWorldLeft()
//    {
//
//    }

    private void generateAvatar(UserInputListener inputListener, ImageReader imageReader) {
        Vector2 avatarInitialPosition = new Vector2(maxX / 2f, this.terrain.groundHeightAt(maxX / 2f) - Block.SIZE);
        this.avatar = Avatar.create(gameObjects(), AVATAR_LAYER, avatarInitialPosition, inputListener, imageReader);
        gameObjects().addGameObject(new GameObject(Vector2.ZERO, Vector2.ZERO, null), LEAF_LAYER);
        setCamera(new Camera(this.avatar,
                this.windowDimensions.mult(0.5f).subtract(avatarInitialPosition),
                this.windowDimensions,
                this.windowDimensions));
    }

    private void generateInitialScenery()
    {
        this.terrain = new Terrain(this.gameObjects(), TOP_GROUND_LAYER, windowDimensions, SEED); // initializing the terrain
        this.terrain.setCache(this.cache);
        terrain.createInRange(0, maxX); // terrain spread on the whole screen.

        this.trees = new Tree(this.gameObjects(),this.terrain::groundHeightAt,SEED);
        trees.createInRange(0, maxX);
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
