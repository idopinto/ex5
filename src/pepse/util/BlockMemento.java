package pepse.util;

import pepse.world.Block;

public class BlockMemento {
    private Block block;
    private int layer;
    private String state; // in , out

    public BlockMemento(Block block, int layer, String state){
        this.block = block;
        this.layer = layer;
        this.state = state;
    }

    public String getState(){
        return state;
    }
    public int getLayer(){
        return layer;
    }

    public Block getBlock(){
        return block;
    }


}
