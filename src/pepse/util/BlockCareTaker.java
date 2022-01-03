package pepse.util;

import java.util.ArrayList;
import java.util.List;

/**
 * this class is part of Memento design pattern implementation
 * this class holds array list of Memento which represents Block and their state in the game.
 */

public class BlockCareTaker {


    /* Fields */
    private final List<BlockMemento> blockMementoList = new ArrayList<>();

    /**
     * gets blockMemento and adds it to the array list
     *
     * @param blockMemento block to add
     */
    public void add(BlockMemento blockMemento) {
        blockMementoList.add(blockMemento);
    }

    /**
     * @param index integer
     * @return the Memento stored in the array list at this index
     */
    public BlockMemento get(int index) {
        return blockMementoList.get(index);
    }

    /**
     * @return the size of the array list
     */
    public int size() {
        return blockMementoList.size();
    }


    /**
     * gets originator and index and replaces with new Memento the old memento. used merely to change State from 'in'
     * to 'out' and vice versa.
     *
     * @param originator originator instance to act on. assume that the desired state is saved already.
     * @param index      the index of the desired Memento to replace.
     */
    public void replaceMemento(BlockOriginator originator, int index) {
        blockMementoList.set(index,
                originator.saveStateToMemento());
    }
}

