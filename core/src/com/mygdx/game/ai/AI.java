package com.mygdx.game.ai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.AbilityType;
import com.mygdx.game.enums.CreatureAction;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.tools.ConditionProcessor;

import java.util.HashMap;
import java.util.Random;

import static com.mygdx.game.PalidorGame.PPM;

/**
 * Created by odiachuk on 12/26/17.
 */
public class AI {
    private final Array<Creature> listOfEnemies;

    private boolean moveLeft;
    private boolean moveRight;
    private boolean standStill;
    boolean hasToJump;
    boolean enemyIsNear = false;
    double timeToChangeDirection;


    public char[] steps = null;
    char currentStep;
    double timeForNextStep;
    float speedInCurrentStep = 1;
    float timeMovementTakes = 1;
    int currentStepNumber;

    GameScreen screen;
    Creature creature;
    String program;

    public AI(Creature creature, GameScreen screen, String program){
        this.screen = screen;
        this.creature = creature;
        this.program = program;

        currentStepNumber = 0;

        listOfEnemies = new Array<Creature>();

        if(program != null && !program.equals("")) {
            steps = program.toCharArray();
        }
    }

    private boolean isVisible(Creature mob) {
        return (mob.isEnemy(creature) //is enemy
                && !mob.isHidden() // not hidden
          //      && ( mob.getBody().getPosition().x + creature.sight/PPM < creature.getBody().getPosition().x && mob.getBody().getPosition().x - creature.sight/PPM > creature.getBody().getPosition().x)); // visible
                && ( mob.getBody().getPosition().x < creature.getBody().getPosition().x + creature.sight/PPM && mob.getBody().getPosition().x > creature.getBody().getPosition().x  - creature.sight/PPM ));

        };

    private boolean isReachable(Creature creature) {
        if((creature.getY() - 0.30 < creature.getY() && creature.getY() + 0.30 > creature.getY()))
            return true;
        return false;

    }

    private int getDistance(Creature mob) {
        if(isReachable(mob))
            return Math.abs(Math.round(creature.getX() - mob.getX()));
        else
            return Math.abs(Math.round(creature.getX() - mob.getX()))+10;
    }

    public void getNextStep( float delta) {

        int curDistanceToEnemy =0;
        int shortestDistanceToEnemy =0;

        float targetX = 0, targetY = 0;
        listOfEnemies.clear();

        CreatureAction result = CreatureAction.STOP;

        //define nearby situation :

        //check for neighbors
        if(creature.getNeighbor() != null && creature.getNeighbor().isEnemy(creature)){
            targetX = creature.getNeighbor().getX();
            targetY = creature.getNeighbor().getY();

        } else {

        // look for creatures nearby
        for (int i = 0; i < screen.levelmanager.CREATURES.size; i++) {
            Creature mob = screen.levelmanager.CREATURES.get(i);
            if (isVisible(mob)) {
                listOfEnemies.add(mob);
            }
        }

        for (int i = 0; i < screen.levelmanager.SUMMONED_CREATURES.size; i++) {
            Creature mob = screen.levelmanager.SUMMONED_CREATURES.get(i);
            if (isVisible(mob)) {
                listOfEnemies.add(mob);
            }
        }

        if (isVisible(screen.hero)) { //&& screen.hero.getToughness() < creature.getToughness()) {
            listOfEnemies.add(screen.hero);
        }

        //check all enemies
        for (Creature mob : listOfEnemies) {
            curDistanceToEnemy = getDistance(mob);
            if (shortestDistanceToEnemy ==0 || curDistanceToEnemy < shortestDistanceToEnemy) {
                shortestDistanceToEnemy = curDistanceToEnemy;
                targetX = mob.getX();
                targetY = mob.getY();
            }
        }
    }



        // if target was found
        if(targetX != 0) {

            if (targetX < creature.getX())    //move/atack left
            {
                creature.directionRight = false;
                creature.direction.set(-1, 0);
                if (targetY - 0.10 < creature.getY() && targetY + 0.10 > creature.getY()) { // is reachable
                    if (targetX > creature.getX() - PalidorGame.TILE_SIZE / PPM             // is close
                            && targetX > creature.getX() - PalidorGame.TILE_SIZE / PPM) {
                        result = CreatureAction.CLOSE_ATACK;
                    } else
                        result = CreatureAction.RANGE_ATACK;
                } else  result = CreatureAction.MOVE_LEFT;
            } else if (targetX > creature.getX()) {        //move/atack right
                {
                    creature.directionRight = true;
                    creature.direction.set(1, 0);
                    if(targetY - 0.10 < creature.getY() && targetY + 0.10 > creature.getY()) {// is reachable
                        if (targetX < creature.getX() + PalidorGame.TILE_SIZE / PPM             // is close
                                && targetX > creature.getX() - PalidorGame.TILE_SIZE / PPM)
                            result = CreatureAction.CLOSE_ATACK;
                        else
                            result = CreatureAction.RANGE_ATACK;
                    } else  result = CreatureAction.MOVE_RIGHT;

                }

            }else
                result = CreatureAction.STOP;


            if (isHasToJump() && targetY - 0.05 > creature.getY()) {
                result = CreatureAction.JUMP;
                setHasToJump(false);
            }

            //reset program

            program = null;
            steps = null;


        } else // follow program (if any)
            if(program != null && !"".equals(program)) {
                result = nextStepByProgram(delta);

            }

        if(creature.abilityToCast == AbilityID.NONE)
        switch (result) {
            case STOP:
                creature.stop();
                break;
            case MOVE_LEFT:
                if (!moveRight)
                    creature.move(false);
                break;
            case MOVE_RIGHT:
                if (!moveLeft)
                    creature.move(true);
                break;
            case JUMP:
                creature.jump();
                break;
            case RANGE_ATACK:
                if (creature.findAbility(AbilityType.LONG_RANGE_ATACK) != null) {
                    creature.useAbility(creature.findAbility(AbilityType.LONG_RANGE_ATACK));
                    creature.weaponSprite.isMoving = true;
                } else if (creature.findAbility(AbilityType.CLOSE_RANGE_ATACK) != null) {
                    if (creature.directionRight) {
                        if (!moveLeft)
                            creature.move(creature.directionRight);
                    } else {
                        if (!moveRight)
                            creature.move(creature.directionRight);
                    }
                }

                break;
            case CLOSE_ATACK:
                if (creature.findAbility(AbilityType.CLOSE_RANGE_ATACK) != null) {
                    creature.useAbility(creature.findAbility(AbilityType.CLOSE_RANGE_ATACK));
                    creature.weaponSprite.isMoving = true;
                } else //move to enemy
                    if (creature.directionRight) {
                        if (!moveLeft)
                            creature.move(creature.directionRight);
                    } else {
                        if (!moveRight)
                            creature.move(creature.directionRight);
                    }
                break;
            case WALK:
                if (creature.existingTime > timeToChangeDirection) {
                    creature.directionRight = (new Random().nextBoolean());
                    timeToChangeDirection = creature.existingTime + 2f;
                }
                creature.move(creature.directionRight);
                break;
            }

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


    private CreatureAction nextStepByProgram(float dt) {
        CreatureAction result = CreatureAction.STOP;

        if (currentStepNumber >= steps.length)
            currentStepNumber = 0;

        currentStep = steps[currentStepNumber];

        if (currentStep == 'r') {
            result =  CreatureAction.MOVE_RIGHT;
        }
        if (currentStep == 'l') {
            result =  CreatureAction.MOVE_LEFT;

        }

        if (creature.existingTime >= timeForNextStep) {
            currentStepNumber++;
            timeForNextStep = creature.existingTime + timeMovementTakes;



            if (currentStep == 'j') {
                result = CreatureAction.JUMP;
                timeForNextStep = creature.existingTime + timeMovementTakes;
            }


            if (currentStep == 'n') {//nothing
                timeForNextStep = creature.existingTime + 1;
            }

        }

        //TODO trigger processing
//        if(currentStep =='t') {//reset
//            if(!ConditionProcessor.conditionSatisfied(screen.hero, activationTrigger)) {
//                timeForNextStep = existingTime + 0.5;
//                direction.set(0, 0.01f);
//                currentStepNumber--;
//            }
//        }

        return result;
    }

}
