package pepse.util;

import java.util.Random;

public class RandomObjOriginator {
    private Random random;

    public void setRandom(Random random){
        this.random = random;
    }

    public Random getRandom(){
        return random;
    }

    public RandomObjsMemento saveStateToMemento(){
        return new RandomObjsMemento(random);
    }

    public void getStateFromMemento(RandomObjsMemento memento){
        random = memento.getState();
    }
}
