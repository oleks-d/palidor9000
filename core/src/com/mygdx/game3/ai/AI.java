package com.mygdx.game3.ai;

import com.mygdx.game3.enums.CreatureAction;
import com.mygdx.game3.screens.GameScreen;
import com.mygdx.game3.sprites.creatures.Creature;

/**
 * Created by odiachuk on 12/26/17.
 */
public class AI {
    public boolean isHasToJump() {
        return hasToJump;
    }

    public void setHasToJump(boolean hasToJump) {
        this.hasToJump = hasToJump;
    }

    boolean hasToJump;
    public CreatureAction getNextStep(Creature creature, GameScreen screen) {
        return CreatureAction.STOP;
    /*    float targetX = creature.getX(), targetY = creature.getY();
        CreatureAction result = CreatureAction.STOP;
        for(int i = 0; i<screen.levelmanager.CREATURES.size; i++){
            if(screen.levelmanager.CREATURES.get(i).isEnemy(creature)) {
                targetX = screen.levelmanager.CREATURES.get(i).getX();
                targetY = screen.levelmanager.CREATURES.get(i).getY();
                //result = new Random().nextBoolean() ? CreatureAction.MOVE_LEFT : CreatureAction.MOVE_RIGHT;
                if (targetX < creature.getX()) result = CreatureAction.MOVE_LEFT;
                else result = CreatureAction.MOVE_RIGHT;
            }
        }

        if(screen.hero.isEnemy(creature)) {
            targetX = screen.hero.getX();
            targetY = screen.hero.getY();
            //result = new Random().nextBoolean() ? CreatureAction.MOVE_LEFT : CreatureAction.MOVE_RIGHT;
            if (targetX < creature.getX())
                result = CreatureAction.MOVE_LEFT;
            else
                result = CreatureAction.MOVE_RIGHT;
        }

        if(isHasToJump() && targetY - 0.05 > creature.getY()){
            return CreatureAction.JUMP;
        }
        return result;
     */
    }
}
