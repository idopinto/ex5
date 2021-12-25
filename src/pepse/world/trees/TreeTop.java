package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

public class TreeTop {

    private static final int LEAF_IN_ROW = 8;

    static void createTreeTop(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner, Random random)
    {
        int x = (int) topLeftCorner.x();
        int y = (int) topLeftCorner.y();
//        int x = trunkTopX - (LEAF_IN_ROW/2)*Block.SIZE;
//        int y = trunkTopY - (LEAF_IN_ROW/2)*Block.SIZE;
        for (int i = 0; i < LEAF_IN_ROW; i++) {
            for (int j = 0; j < LEAF_IN_ROW; j++) {
                if (needToPutLeaf(random))
                {
                    Block leaf = Leaf.create(new Vector2(x,y));
                    gameObjects.addGameObject(leaf,layer);
                }
                x += Block.SIZE;
            }
            y +=Block.SIZE;
        }
    }

    static boolean needToPutLeaf(Random random)
    {
        return (float) random.nextInt(100) < 10;
    }

}
