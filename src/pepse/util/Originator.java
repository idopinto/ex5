package pepse.util;

import pepse.world.Block;

public class Originator {
    private String state;
    private Block block;
    private int layer;

    public void setBlockState(String state, Block block, int layer){
        this.state = state;
        this.block = block;
        this.layer = layer;
    }

    public void setBlockState(String state){
        this.state = state;
    }
    public void setBlockState(int layer)
    {
        this.layer = layer;
    }


    public String getState(){
        return state;
    }

    public Block getBlock(){
        return block;
    }

    public int getLayer(){
        return layer;
    }


    public BlockMemento saveStateToMemento(){return new BlockMemento(this.block,this.layer,this.state);}

    public void getStateFromMemento(BlockMemento blockMemento){
        state = blockMemento.getState();
        block= blockMemento.getBlock();
        layer = blockMemento.getLayer();
    }
}
