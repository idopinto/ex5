package pepse.util;

import pepse.world.Block;

/**
 * This class is part of memento implementation.
 * Represents the originator which exclusively communicates with the BlockCareTaker.
 * his responsibilities are to get state from given memento and save the current state to new memento for the user.
 */
public class BlockOriginator {
    private String state;
    private Block block;
    private int layer;

    /**
     *  initial setter
     * @param state String which represents if the Block is currently in the gameObjects or not. can be "in","out"
     * @param block block instance
     * @param layer integer that represents the Block layer in the game.
     */
    public void setBlockState(Block block, int layer,String state){
        this.state = state;
        this.block = block;
        this.layer = layer;
    }

    /**
     * overloading setter to the current state of the block
     * @param state String which represents if the Block is currently in the gameObjects or not. can be "in","out"
     */
    public void setBlockState(String state){
        this.state = state;
    }

    /**
     * getter
     * @return the currently stored state
     */
    public String getState(){
        return state;
    }

    /**
     * getter
     * @return the currently stored block instance
     */
    public Block getBlock(){
        return block;
    }

    /**
     *  getter
     * @return the currently stored layer
     */
    public int getLayer(){
        return layer;
    }


    /**
     *
     * @return new Memento instance that created from the currently stored values.
     */
    public BlockMemento saveStateToMemento(){return new BlockMemento(this.block,this.layer,this.state);}

    /**
     *  save the current values of given blockMemento in the originator fields.
     * @param blockMemento instance of blockMemento
     */
    public void getStateFromMemento(BlockMemento blockMemento){
        state = blockMemento.getState();
        block= blockMemento.getBlock();
        layer = blockMemento.getLayer();
    }
}
