package com.mygdx.game.tools;

import com.mygdx.game.enums.GameObjectType;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.sprites.gameobjects.GameObject;

/**
 * Created by odiachuk on 1/24/18.
 */
public class ObjectTouchProcessor {

    public static void touchObject(Creature creature, GameObject object) {
        if (object.getType() == GameObjectType.DOOR) {
            if (creature.checkInInventory(object.getRequiredKey())) {
                object.destroyBody();
            }
        }
        if (object.getType() == GameObjectType.CHEST) {
            if (creature.checkInInventory(object.getRequiredKey())) {
                creature.getInventory().addAll(object.items);
                object.destroyBody();
            }
        }
        if (object.getType() == GameObjectType.SPIKE){
            creature.doDamage(5);
        }
    }
}
