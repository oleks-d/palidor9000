package com.mygdx.game.ai;

import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.AbilityType;
import com.mygdx.game.enums.CreatureAction;
import com.mygdx.game.sprites.creatures.Creature;

import java.util.Random;

import static com.mygdx.game.PalidorGame.PPM;

/**
 * Created by odiachuk on 12/26/17.
 */
public class AI {
    private boolean moveLeft;
    private boolean moveRight;
    private boolean standStill;
    boolean hasToJump;
    boolean enemyIsNear = false;
    double timeToChangeDirection;


    public void getNextStep(Creature creature, com.mygdx.game.screens.GameScreen screen) {

        // TODO redesign - May I punch enemy? if not - do my business

        boolean enemyIsNear = false;
        float targetX = creature.getX(), targetY = creature.getY();

        CreatureAction result = CreatureAction.STOP;

        // look for creatures nearby
        for(int i = 0; i<screen.levelmanager.CREATURES.size; i++){
            Creature mob = screen.levelmanager.CREATURES.get(i);
            if(mob.isEnemy(creature) //is enemy
            && !mob.isHidden() // not hidden
            && ( mob.getBody().getPosition().x + creature.sight/PPM < creature.getBody().getPosition().x && mob.getBody().getPosition().x - creature.sight/PPM > creature.getBody().getPosition().x)) { // visible
                targetX = screen.levelmanager.CREATURES.get(i).getX();
                targetY = screen.levelmanager.CREATURES.get(i).getY();
                enemyIsNear = true;
                //result = new Random().nextBoolean() ? CreatureAction.MOVE_LEFT : CreatureAction.MOVE_RIGHT;
            }
        }

        if(screen.hero.isEnemy(creature)
                && !screen.hero.isHidden()
                && ( screen.hero.getBody().getPosition().x < creature.getBody().getPosition().x + creature.sight/PPM && screen.hero.getBody().getPosition().x > creature.getBody().getPosition().x  - creature.sight/PPM ))
        { //&& screen.hero.getToughness() < creature.getToughness()) {
            targetX = screen.hero.getX();
            targetY = screen.hero.getY();
            enemyIsNear = true;
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

        if(enemyIsNear) {

            if (targetX < creature.getX())    //move/atack left
            {
                creature.directionRight = false;
                creature.direction.set(-1, 0);
                if (targetY - 0.10 < creature.getY() && targetY + 0.10 > creature.getY()) // is reachable
                if (targetX > creature.getX() - PalidorGame.TILE_SIZE / PPM             // is close
                        && targetX > creature.getX() - PalidorGame.TILE_SIZE / PPM)
                    result = CreatureAction.CLOSE_ATACK;
                else
                    result = CreatureAction.RANGE_ATACK;
            } else if (targetX > creature.getX()) {        //move/atack right
                {
                    creature.directionRight = true;
                    creature.direction.set(1, 0);
                    if(targetY - 0.10 < creature.getY() && targetY + 0.10 > creature.getY()) // is reachable
                    if (targetX < creature.getX() + PalidorGame.TILE_SIZE / PPM             // is close
                            && targetX > creature.getX() - PalidorGame.TILE_SIZE / PPM)
                        result = CreatureAction.CLOSE_ATACK;
                    else
                        result = CreatureAction.RANGE_ATACK;

                }
            } else
                result = CreatureAction.STOP;


            if (isHasToJump() && targetY - 0.05 > creature.getY()) {
                result = CreatureAction.JUMP;
                //setHasToJump(false);
            }
        } else {
            if(moveLeft)
                result = CreatureAction.MOVE_LEFT;
            else if(moveRight)
                result = CreatureAction.MOVE_RIGHT;
            else if(standStill)
                result = CreatureAction.STOP;
            else
                result = CreatureAction.WALK;

        }

        //creature.direction.set(-(targetX - creature.getBody().getPosition().x), targetY - creature.getBody().getPosition().y );
//        if(targetX > creature.getBody().getPosition().x) {
//            creature.direction.set(1, 0);
//            creature.directionRight = true;
//        }else {
//            creature.direction.set(-1, 0);
//            creature.directionRight = false;
//        }
        //creature.direction.clamp(1,1);
        //creature.direction.set(creature.targetVector.x > 1 ? 1: (creature.targetVector.x < -1 ? -1f : creature.targetVector.x), creature.targetVector.y > 1 ? 1:(creature.targetVector.y < -1 ? -1f : creature.targetVector.y));




        if(creature.abilityToCast == AbilityID.NONE)
        switch (result){
            case STOP:
                creature.stop();
                break;
            case MOVE_LEFT:
                    creature.move(false);
                break;
            case MOVE_RIGHT:
                    creature.move(true);
                break;
            case JUMP:
                creature.jump();
                break;
            case RANGE_ATACK:
                if(creature.findAbility(AbilityType.LONG_RANGE_ATACK) != null) {
                    creature.useAbility(creature.findAbility(AbilityType.LONG_RANGE_ATACK));
                    creature.weaponSprite.isMoving = true;
                }
                    else if(creature.findAbility(AbilityType.CLOSE_RANGE_ATACK) != null) {
                    creature.move(creature.directionRight);
                }

                break;
            case CLOSE_ATACK:
                if(creature.findAbility(AbilityType.CLOSE_RANGE_ATACK) != null) {
                    creature.useAbility(creature.findAbility(AbilityType.CLOSE_RANGE_ATACK));
                    creature.weaponSprite.isMoving = true;
                }else
                    creature.move(creature.directionRight);
//                    else if(creature.findAbility(AbilityType.BUFF) != null)
//                    creature.useAbility(creature.findAbility(AbilityType.BUFF));
//                        else if (creature.findAbility(AbilityType.DEBUFF)!=null)
//                    creature.useAbility(creature.findAbility(AbilityType.DEBUFF));
                break;
            case WALK:
                if(creature.existingTime > timeToChangeDirection) {
                    creature.directionRight = (new Random().nextBoolean());
                    timeToChangeDirection = creature.existingTime + 2f;
                } creature.move(creature.directionRight);
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

    public void setStandStill(boolean standStill) {
        this.standStill = standStill;
    }

    public boolean isHasToJump() {
        return hasToJump;
    }
    public void setHasToJump(boolean hasToJump) {
        this.hasToJump = hasToJump;
    }

}
