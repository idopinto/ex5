package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

public class TreeTop {


    static void createTreeTop(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,int  leafInRow)
    {
        int x = (int) topLeftCorner.x();
        int y = (int) topLeftCorner.y();

//        int x = trunkTopX - (LEAF_IN_ROW/2)*Block.SIZE;
//        int y = trunkTopY - (LEAF_IN_ROW/2)*Block.SIZE;
        for (int i = 0; i < leafInRow; i++) {
            for (int j = 0; j < leafInRow; j++) {
                Block leaf = Leaf.create(new Vector2(x,y));
                gameObjects.addGameObject(leaf,layer);
                x += Block.SIZE;
            }
            y +=Block.SIZE;
        }
    }


}
