package com.mygdx.game.ai;

import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityType;
import com.mygdx.game.enums.CreatureAction;
import com.mygdx.game.sprites.creatures.Creature;

import static com.mygdx.game.PalidorGame.PPM;

/**
 * Created by odiachuk on 12/26/17.
 */
public class AI {
    private boolean moveLeft;
    private boolean moveRight;

    public boolean isHasToJump() {
        return hasToJump;
    }

    public void setHasToJump(boolean hasToJump) {
        this.hasToJump = hasToJump;
    }

    boolean hasToJump;
    public void getNextStep(Creature creature, com.mygdx.game.screens.GameScreen screen) {

        //return CreatureAction.STOP;
        float targetX = creature.getX(), targetY = creature.getY();
        CreatureAction result = CreatureAction.STOP;
        for(int i = 0; i<screen.levelmanager.CREATURES.size; i++){
            if(screen.levelmanager.CREATURES.get(i).isEnemy(creature)
            && !screen.levelmanager.CREATURES.get(i).isHidden()) {
                targetX = screen.levelmanager.CREATURES.get(i).getX();
                targetY = screen.levelmanager.CREATURES.get(i).getY();
                //result = new Random().nextBoolean() ? CreatureAction.MOVE_LEFT : CreatureAction.MOVE_RIGHT;
            }
        }

        if(screen.hero.isEnemy(creature) && !screen.hero.isHidden() ){ //&& screen.hero.getToughness() < creature.getToughness()) {
            targetX = screen.hero.getX();
            targetY = screen.hero.getY();
            //result = new Random().nextBoolean() ? CreatureAction.MOVE_LEFT : CreatureAction.MOVE_RIGHT;
        }

//        if (targetX < creature.getX())    //move/atack left
//            if(targetY - 0.10 < creature.getY() && targetY + 0.10 > creature.getY()) {
//                creature.directionRight = false;
//                if(targetX > creature.getX() - PalidorGame.TILE_SIZE/PPM)
//                    result = CreatureAction.CLOSE_ATACK;
//                else
//                    result = CreatureAction.RANGE_ATACK;
//            } else
//                result = CreatureAction.MOVE_LEFT;
//         else if(targetX > creature.getX()){        //move/atack right
//            if (targetY - 0.10 < creature.getY() && targetY + 0.10 > creature.getY()) {
//                creature.directionRight = true;
//                if (targetX < creature.getX() + PalidorGame.TILE_SIZE / PPM)
//                    result = CreatureAction.CLOSE_ATACK;
//                else
//                    result = CreatureAction.RANGE_ATACK;
//            } else {
//                result = CreatureAction.MOVE_RIGHT;
//            }
//        }    else
//            result = CreatureAction.STOP;


        if (targetX < creature.getX())    //move/atack left
             {
                creature.directionRight = false;
                if(targetX > creature.getX() - PalidorGame.TILE_SIZE/PPM && ((targetY < creature.getY() + PalidorGame.TILE_SIZE/PPM || (targetY > creature.getY() - PalidorGame.TILE_SIZE/PPM))))
                    result = CreatureAction.CLOSE_ATACK;
                else
                    result = CreatureAction.RANGE_ATACK;
            }
        else if(targetX > creature.getX()){        //move/atack right
             {
                creature.directionRight = true;
                if (targetX < creature.getX() + PalidorGame.TILE_SIZE / PPM && ((targetY < creature.getY() - PalidorGame.TILE_SIZE/PPM || (targetY > creature.getY() - PalidorGame.TILE_SIZE/PPM))))
                    result = CreatureAction.CLOSE_ATACK;
                else
                    result = CreatureAction.RANGE_ATACK;

            }
        }    else
            result = CreatureAction.STOP;


        if(isHasToJump() && targetY - 0.05 > creature.getY()){
            result = CreatureAction.JUMP;
        }

        creature.direction.set(-(targetX - creature.getBody().getPosition().x), targetY - creature.getBody().getPosition().y );
        creature.direction.clamp(1,1);
        //creature.direction.set(creature.targetVector.x > 1 ? 1: (creature.targetVector.x < -1 ? -1f : creature.targetVector.x), creature.targetVector.y > 1 ? 1:(creature.targetVector.y < -1 ? -1f : creature.targetVector.y));




        if(creature.abilityToCast == com.mygdx.game.enums.AbilityID.NONE)
        switch (result){
            case MOVE_LEFT:
                if(!moveRight)
                    creature.move(false);
                else{
                    creature.move(true);
                }
                break;
            case MOVE_RIGHT:
                if(!moveLeft)
                    creature.move(true);
                else {
                    creature.move(false);
                }
                break;
            case JUMP:
                creature.jump();
                break;
            case RANGE_ATACK:
                if(creature.findAbility(AbilityType.LONG_RANGE_ATACK) != null)
                    creature.useAbility(creature.findAbility(AbilityType.LONG_RANGE_ATACK));
                    else if(creature.findAbility(AbilityType.CLOSE_RANGE_ATACK) != null) {
                    if (moveRight) {
                        creature.move(true);
                    } else if (moveLeft) {
                        creature.move(false);
                    } else creature.move(creature.directionRight);
                }

                break;
            case CLOSE_ATACK:
                if(creature.findAbility(AbilityType.CLOSE_RANGE_ATACK) != null)
                    creature.useAbility(creature.findAbility(AbilityType.CLOSE_RANGE_ATACK));
                    else if(creature.findAbility(AbilityType.BUFF) != null)
                    creature.useAbility(creature.findAbility(AbilityType.BUFF));
                        else if (creature.findAbility(AbilityType.DEBUFF)!=null)
                    creature.useAbility(creature.findAbility(AbilityType.DEBUFF));
                break;
        }

        //return result;

    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }
}
