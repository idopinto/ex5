package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

public class TreeTop {


    static void createTreeTop(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,int  leafInRow)
    {
        int x ;
        int y = (int) topLeftCorner.y();

        for (int i = 0; i < leafInRow; i++) {
            x = (int) topLeftCorner.x();
            for (int j = 0; j < leafInRow; j++) {
                Leaf.create(gameObjects,new Vector2(x,y),layer);
                x += Block.SIZE;
            }
            y +=Block.SIZE;
        }
    }


}
