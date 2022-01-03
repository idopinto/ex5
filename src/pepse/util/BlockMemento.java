package pepse.util;

import pepse.world.Block;

/**
 * This class is part of Memento implementation.
 * Represents Memento which is snapshot of block, and  it's state in the game.
 * Creation of Memento is the responsibility of the BlockOriginator.
 */
public class BlockMemento {
    /* Fields */
    private final Block block;
    private final int layer;
    private final String state; // can be 'in','out'

    /**
     * Constructor
     *
     * @param block Block instance
     * @param layer integer that represents the Block layer in the game.
     * @param state String which represents if the Block is currently in the gameObjects or not. can be "in","out"
     */
    public BlockMemento(Block block, int layer, String state) {
        this.block = block;
        this.layer = layer;
        this.state = state;
    }

    /**
     * getter
     *
     * @return state of the Block (in or out)
     */
    public String getState() {
        return state;
    }

    /**
     * getter
     *
     * @return layer of the block
     */
    public int getLayer() {
        return layer;
    }

    /**
     * getter
     *
     * @return the block instance
     */
    public Block getBlock() {
        return block;
    }
}
