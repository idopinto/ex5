package pepse.util;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {
    private List<BlockMemento> blockMementoList = new ArrayList<BlockMemento>();

    public void add(BlockMemento state){
        blockMementoList.add(state);
    }
    public void saveState(Originator originator, int index)
    {
        blockMementoList.set(index ,originator.saveStateToMemento());
    }
    public BlockMemento get(int index){
        return blockMementoList.get(index);
    }
    public int getMementoListSize(){return blockMementoList.size();}
}

