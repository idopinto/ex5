package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.awt.*;

public class Energy extends GameObject{

    /**
     * Display a graphic object on the game window showing a numeric count of energy left.
     */
        /* Constants */
        private static final TextRenderable textRenderable = new TextRenderable("");

    /* Class Fields */
    private final Counter energyCounter;

        /**
         * Construct a new GameObject instance.
         *
         * @param topLeftCorner        Position of the object, in window coordinates (pixels).
         *                             Note that (0,0) is the top-left corner of the window.
         * @param dimensions           Width and height in window coordinates.
         * @param gameObjectCollection global game object collection.
         */
        public Energy(Counter energyCounter, Vector2 topLeftCorner, Vector2 dimensions,
                                  GameObjectCollection gameObjectCollection) {
            super(topLeftCorner, dimensions, textRenderable);
            this.energyCounter = energyCounter;
            textRenderable.setString(String.format("Energy: %d ", this.energyCounter.value()/2));
            textRenderable.setColor(Color.BLACK);
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            if(energyCounter.value() > 0) {
                String textString = String.format("Energy: %d ",this.energyCounter.value()/2);
                textRenderable.setString(textString);
            }
        }
    }

