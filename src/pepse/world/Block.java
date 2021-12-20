package pepse.world;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a single block (larger objects can be created from blocks).
 */
public class Block extends danogl.GameObject {

    /**
     * Size of a single block.
     */
     public static final int SIZE = 30;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Block(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

//    /**
//     * @param topLeftCorner The location of the top-left corner of the created block
//     * @param renderable - A renderable to render as the block.
//     */
//    public Block(Vector2 topLeftCorner, Renderable renderable)
//    {
//          API constructor without the dimensions' parameter
//    }

}
