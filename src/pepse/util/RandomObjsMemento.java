package pepse.util;

import java.util.Random;

public class RandomObjsMemento {
    private final Random random;

    public RandomObjsMemento(Random random){
        this.random = random;
    }

    public Random getState(){
        return this.random;
    }
}
