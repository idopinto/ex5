package pepse.util;

import java.util.ArrayList;
import java.util.List;

public class RandomObjCaretaker {
    private List<RandomObjsMemento> mementoList = new ArrayList<RandomObjsMemento>();

    public void add(RandomObjsMemento state){
        mementoList.add(state);
    }

    public RandomObjsMemento get(int index){
        return mementoList.get(index);
    }
}
