package com.mygdx.game.ai;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.AbilityType;
import com.mygdx.game.enums.CreatureAction;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.creatures.Creature;

import java.util.Random;

import static com.mygdx.game.PalidorGame.PPM;

/**
 * Created by odiachuk on 12/26/17.
 */
public class AI {

    Random randomizer = new Random();

//    static int STATE_STAND = 0;
//    static int STATE_EXECUTEPROGRAM = 1;
//    static int STATE_ATTACK = 2;
//    static int STATE_RUNAWAY =3;
//    static int STATE_SLEEP = 4;

    private final BehaviourPattern pattern;
    //int state = STATE_STAND;
    int currentActionStepNumber = 0;
    int maxNumberOfActionSteps = 0;


    CreatureAction repeatAction = null;


    private Array<Creature> listOfCurrentEnemies;
    private Array<Creature> globalListOfEnemies;
    
    private boolean moveLeft;
    private boolean moveRight;

    boolean hasToJump;
    boolean jumpRight;

    private boolean standStill;

    boolean enemyIsNear = false;
    double timeToChangeDirection;


    public char[] steps = null;
    char currentStep;
    double timeForNextStep;
    float speedInCurrentStep = 1;
    float timeMovementTakes = 1;
    int currentStepNumber;

    GameScreen screen;
    Creature creatureOwner;
    String program;

    String DEFAULT_PROGRAM = "lwrwnw";

    String organization;

    public AI(Creature creature, GameScreen screen, String program, BehaviourPattern pattern){
        this.screen = screen;
        this.creatureOwner = creature;
        organization = String.valueOf(creatureOwner.getOrganization());
        this.program = program;
        this.pattern =  pattern;

        currentStepNumber = 0;

        if(pattern == null){
            currentActionStepNumber = 0;
            maxNumberOfActionSteps = 0;
        } else {
            currentActionStepNumber = 0;
            maxNumberOfActionSteps = pattern.steps.length;
        }

        listOfCurrentEnemies = new Array<Creature>();
        globalListOfEnemies = new Array<Creature>();

        if(program != null && !program.equals("") ) {
            //state = STATE_EXECUTEPROGRAM;
            steps = program.toCharArray();
        } else {
            if(!creature.isMachine) {
                this.program = DEFAULT_PROGRAM;
                steps = DEFAULT_PROGRAM.toCharArray();
            }
        }
    }

    private boolean isVisible(Creature mob) {
        return ( !mob.destroyed
                && !mob.isHidden() // not hidden
          //      && ( mob.getBody().getPosition().x + creatureOwner.sight/PPM < creatureOwner.getBody().getPosition().x && mob.getBody().getPosition().x - creatureOwner.sight/PPM > creatureOwner.getBody().getPosition().x)); // visible
                && ( mob.getBody().getPosition().x < creatureOwner.getBody().getPosition().x + creatureOwner.sight/PPM && mob.getBody().getPosition().x > creatureOwner.getBody().getPosition().x  - creatureOwner.sight/PPM ));

        };

    private boolean isReachable(Creature mob) {
        if((creatureOwner.getY() - 0.05 < mob.getY() && creatureOwner.getY() + 0.05 > mob.getY()))
            return true;
        return false;

    }

    private int getDistance(Creature mob) {
        if(isReachable(mob))
            return Math.abs(Math.round(creatureOwner.getX() - mob.getX()));
        else
            return Math.abs(Math.round(creatureOwner.getX() - mob.getX()))+10;
    }

    public void getNextStep( float delta) {

        if (creatureOwner.abilityToCast == AbilityID.NONE && !creatureOwner.isCharmed()) {


            int curDistanceToEnemy = 0;
            int shortestDistanceToEnemy = 0;

            float targetX = 0, targetY = 0;
            listOfCurrentEnemies.clear();

            CreatureAction result = CreatureAction.STOP;

            //define nearby situation :

            //check for neighbors
            if (creatureOwner.getNeighbor() != null &&  isEnemy(creatureOwner.getNeighbor())) {
                targetX = creatureOwner.getNeighbor().getX();
                targetY = creatureOwner.getNeighbor().getY();
                //state = STATE_ATTACK;
                creatureOwner.getNeighbor().setIN_BATTLE(true);

            } else {

                // look for creatures nearby
                for (int i = 0; i < screen.levelmanager.CREATURES.size; i++) {
                    Creature mob = screen.levelmanager.CREATURES.get(i);
                    if (isVisible(mob) && isEnemy(mob)) {
                        listOfCurrentEnemies.add(mob);
                    }
                }

                for (int i = 0; i < screen.levelmanager.SUMMONED_CREATURES.size; i++) {
                    Creature mob = screen.levelmanager.SUMMONED_CREATURES.get(i);
                    if (isVisible(mob) && isEnemy(mob)) {
                        listOfCurrentEnemies.add(mob);
                    }
                }

                if (isVisible(screen.hero) && isEnemy(screen.hero)) { //&& screen.hero.getToughness() < creatureOwner.getToughness()) {
                    listOfCurrentEnemies.add(screen.hero);
                }

                //check all enemies
                for (Creature mob : listOfCurrentEnemies) {
                    curDistanceToEnemy = getDistance(mob);
                    if (shortestDistanceToEnemy == 0 || curDistanceToEnemy < shortestDistanceToEnemy) {
                        shortestDistanceToEnemy = curDistanceToEnemy;
                        targetX = mob.getX();
                        targetY = mob.getY();
                        //state = STATE_ATTACK;
                        mob.setIN_BATTLE(true);
                    }
                }
            }


            // if target was found
            if (targetX != 0) {

                if (targetX < creatureOwner.getX() - 0.1)    //move/atack left
                {
                    creatureOwner.directionRight = false;
                    creatureOwner.direction.set(-1, 0);
                    if (targetY - 0.10 < creatureOwner.getY() && targetY + 0.10 > creatureOwner.getY()) { // is reachable
                        if (targetX > creatureOwner.getX() - 1.5 * PalidorGame.TILE_SIZE / PPM             // is close
                                && targetX > creatureOwner.getX() - 1.5 * PalidorGame.TILE_SIZE / PPM) {
                            result = CreatureAction.CLOSE_ATACK;
                        } else
                            result = CreatureAction.RANGE_ATACK;
                    } else result = CreatureAction.MOVE_LEFT;
                } else if (targetX > creatureOwner.getX() + 0.1) {        //move/atack right
                    {
                        creatureOwner.directionRight = true;
                        creatureOwner.direction.set(1, 0);
                        if (targetY - 0.10 < creatureOwner.getY() && targetY + 0.10 > creatureOwner.getY()) {// is reachable
                            if (targetX < creatureOwner.getX() + 1.5 * PalidorGame.TILE_SIZE / PPM             // is close
                                    && targetX > creatureOwner.getX() - 1.5 * PalidorGame.TILE_SIZE / PPM)
                                result = CreatureAction.CLOSE_ATACK;
                            else
                                result = CreatureAction.RANGE_ATACK;
                        } else result = CreatureAction.MOVE_RIGHT;

                    }

                } else
                    result = CreatureAction.WALK;


                if (isHasToJump() && targetY - 0.10 > creatureOwner.getY()) {
                    if (targetX > creatureOwner.getX() && jumpRight) { // on right
                        result = CreatureAction.JUMP_RIGHT;
                    } else if (targetX < creatureOwner.getX() && !jumpRight) { //on left
                        result = CreatureAction.JUMP_LEFT;
                    }
                    setHasToJump(false, false);
                }


                //reset program

                resetProgram();


            } else // follow program (if any)
                if (program != null && !"".equals(program)) {
                    result = nextStepByProgram(delta);

                }

//        if("".equals(pattern)) {

            if (creatureOwner.stats.health.current < creatureOwner.stats.health.base / 3)
                result = CreatureAction.ALMOST_DEAD;

            if (creatureOwner.abilityToCast == AbilityID.NONE)
                switch (result) {
                    case STOP:
                        creatureOwner.stop();
                        break;
                    case MOVE_LEFT:
                        if (!moveRight)
                            creatureOwner.move(false);
                        break;
                    case MOVE_RIGHT:
                        if (!moveLeft)
                            creatureOwner.move(true);
                        break;
                    case JUMP_LEFT:
                        creatureOwner.move(false);
                        creatureOwner.jump();
                        break;
                    case JUMP_RIGHT:
                        creatureOwner.move(true);
                        creatureOwner.jump();
                        break;
                    case RANGE_ATACK:
//                if(pattern!=null) {
//                    if( pattern.getSteps()[currentActionStepNumber].getCondition().equals("D")) {
//                        creatureOwner.useAbility(pattern.getSteps()[currentActionStepNumber].ability);
//
//                    } else {
//
//                        if (creatureOwner.directionRight) {
//                            if (!moveLeft)
//                                creatureOwner.move(creatureOwner.directionRight);
//                        } else {
//                            if (!moveRight)
//                                creatureOwner.move(creatureOwner.directionRight);
//                        }
//                    }
//                    currentActionStepNumber++;
//                    if (currentActionStepNumber == maxNumberOfActionSteps)
//                        currentActionStepNumber = 0;
//
//                } else {

                        if (creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK) != null) {
                            creatureOwner.useAbility(creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK));
                            creatureOwner.weaponSprite.isMoving = true;
                        } else if (creatureOwner.findAbility(AbilityType.CLOSE_RANGE_ATACK) != null) {
                            if (creatureOwner.directionRight) {
                                if (!moveLeft)
                                    creatureOwner.move(creatureOwner.directionRight);
                            } else {
                                if (!moveRight)
                                    creatureOwner.move(creatureOwner.directionRight);
                            }
                        }
//                }

                        break;
                    case CLOSE_ATACK:
//                if(pattern!=null) {
//                    if( pattern.getSteps()[currentActionStepNumber].getCondition().equals("")) {
//
//                        creatureOwner.useAbility(pattern.getSteps()[currentActionStepNumber].ability);
//                    }
//                    currentActionStepNumber++;
//                    if(currentActionStepNumber == maxNumberOfActionSteps)
//                        currentActionStepNumber = 0;
//
//                } else {
                        if (creatureOwner.findAbility(AbilityType.CLOSE_RANGE_ATACK) != null) {
                            creatureOwner.useAbility(creatureOwner.findAbility(AbilityType.CLOSE_RANGE_ATACK));
                            creatureOwner.weaponSprite.isMoving = true;
                        }
//                    else //move to enemy
//                        if (creatureOwner.directionRight) {
//                            if (!moveLeft)
//                                creatureOwner.move(creatureOwner.directionRight);
//                        } else {
//                            if (!moveRight)
//                                creatureOwner.move(creatureOwner.directionRight);
//                        }
//                }
                        break;
                    case ALMOST_DEAD:
                        if (creatureOwner.findAbility(AbilityType.BUFF) != null) {
                            creatureOwner.useAbility(creatureOwner.findAbility(AbilityType.BUFF));
                            creatureOwner.weaponSprite.isMoving = true;
                        } else //move from enemy
                            if (!creatureOwner.directionRight) {
                                if (!moveLeft)
                                    creatureOwner.move(creatureOwner.directionRight);
                            } else {
                                if (!moveRight)
                                    creatureOwner.move(creatureOwner.directionRight);
                            }
//                if(pattern!=null) {
//                    if (pattern.getSteps()[currentActionStepNumber].getCondition().equals("HP")) {
//                        creatureOwner.useAbility(pattern.getSteps()[currentActionStepNumber].ability);
//                    }
//                    currentActionStepNumber++;
//                    if (currentActionStepNumber == maxNumberOfActionSteps)
//                        currentActionStepNumber = 0;
//                }
                        break;
                    case WALK:
                        if (creatureOwner.existingTime > timeToChangeDirection) {
                            creatureOwner.directionRight = (new Random().nextBoolean());
                            timeToChangeDirection = creatureOwner.existingTime + 2f;
                        }
                        creatureOwner.move(creatureOwner.directionRight);
                        break;
                }

//        //Gdx.app.log(creatureOwner.directionRight + "", result.toString());
//        } else {
//
//            if(pattern.getSteps()[currentActionStepNumber].getCondition().equals("HP")) {
//                if(creatureOwner.stats.health.current<creatureOwner.stats.health.base/4)
//                    if(creatureOwner.checkCooldownExpired(pattern.getSteps()[currentActionStepNumber].ability)) ;
//                        creatureOwner.useAbility(pattern.getSteps()[currentActionStepNumber].ability);
//            }else
//                if(creatureOwner.checkCooldownExpired(pattern.getSteps()[currentActionStepNumber].ability)) ;
//                    creatureOwner.useAbility(pattern.getSteps()[currentActionStepNumber].ability);
//
//            currentActionStepNumber++;
//                    if (currentActionStepNumber == maxNumberOfActionSteps)
//                        currentActionStepNumber = 0;
//        }
        }
    }



    public void getNextStepByProgram( float delta) {


        // follow program (if any)
        CreatureAction result = CreatureAction.WAIT;

        if (program != null && !"".equals(program) ) {
                if (repeatAction == null)
                    result = nextStepByProgram(delta);
                else
                    result = repeatAction;
        }

        if (creatureOwner.abilityToCast == AbilityID.NONE)
            switch (result) {
                case STOP:
                    creatureOwner.stop();
                    break;
                case MOVE_LEFT:
                        creatureOwner.moveRight(-1f);
                    break;
                case MOVE_RIGHT:
                        creatureOwner.moveRight(1f);
                    break;
                case MOVE_DOWN:
                    creatureOwner.moveUp(-1f);
                    break;
                case MOVE_UP:
                    creatureOwner.moveUp(1f);
                    break;
                case JUMP_LEFT:
                    creatureOwner.move(false);
                    creatureOwner.jump();
                    break;
                case JUMP_RIGHT:
                    creatureOwner.move(true);
                    creatureOwner.jump();
                    break;
                case RANGE_ATACK:
                    if (creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK) != null) {
                        creatureOwner.useAbility(creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK));
                    }
                    break;
                case RANGE_ATACK_UP:
                    creatureOwner.direction = PalidorGame.upVector;
                    if (creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK) != null) {
                        creatureOwner.useAbility(creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK));
                        repeatAction = null;
                    } else
                        repeatAction = result;
                    break;
                case RANGE_ATACK_DOWN:
                    creatureOwner.direction = PalidorGame.downVector;
                    if (creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK) != null) {
                        creatureOwner.useAbility(creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK));
                        repeatAction = null;
                    } else
                        repeatAction = result;
                    break;
                case RANGE_ATACK_RIGHT:
                    creatureOwner.directionRight = true;
                    creatureOwner.direction = PalidorGame.rightVector;
                    if (creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK) != null) {
                        creatureOwner.useAbility(creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK));
                        repeatAction = null;
                    } else
                        repeatAction = result;
                    break;
                case RANGE_ATACK_LEFT:
                    creatureOwner.directionRight = false;
                    creatureOwner.direction = PalidorGame.leftVector;
                    if (creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK) != null) {
                        creatureOwner.useAbility(creatureOwner.findAbility(AbilityType.LONG_RANGE_ATACK));
                        repeatAction = null;
                    } else
                        repeatAction = result;
                    break;
                case CLOSE_ATACK:
                    if (creatureOwner.findAbility(AbilityType.CLOSE_RANGE_ATACK) != null) {
                        creatureOwner.useAbility(creatureOwner.findAbility(AbilityType.CLOSE_RANGE_ATACK));
                    }
                    break;
                case HIDE:
                    creatureOwner.setInvisible(true);
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
    public void setHasToJump(boolean right, boolean hasToJump) {
        this.jumpRight = right;
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
        if (currentStep == 'd') {
            result =  CreatureAction.MOVE_DOWN;
        }
        if (currentStep == 'u') {
            result =  CreatureAction.MOVE_UP;
        }

        if (creatureOwner.existingTime >= timeForNextStep) {
            currentStepNumber++;
            timeForNextStep = creatureOwner.existingTime + timeMovementTakes;


            if (currentStep == 'c') {
                result =  CreatureAction.CLOSE_ATACK;
                timeForNextStep = creatureOwner.existingTime + timeMovementTakes;
            }
            if (currentStep == 'a') {
                result =  CreatureAction.RANGE_ATACK;
                timeForNextStep = creatureOwner.existingTime + timeMovementTakes;
            }

            if (currentStep == '}') {
                result =  CreatureAction.RANGE_ATACK_RIGHT;
                timeForNextStep = creatureOwner.existingTime + timeMovementTakes;
            }
            if (currentStep == '{') {
                result =  CreatureAction.RANGE_ATACK_LEFT;
                timeForNextStep = creatureOwner.existingTime + timeMovementTakes;
            }
            if (currentStep == '^') {
                result =  CreatureAction.RANGE_ATACK_UP;
                timeForNextStep = creatureOwner.existingTime + timeMovementTakes;
            }
            if (currentStep == '_') {
                result =  CreatureAction.RANGE_ATACK_DOWN;
                timeForNextStep = creatureOwner.existingTime + timeMovementTakes;
            }

            if (currentStep == 'j') {
                result = CreatureAction.JUMP_LEFT;
                timeForNextStep = creatureOwner.existingTime + timeMovementTakes;
            }

            if (currentStep == 'i') {
                result = CreatureAction.JUMP_RIGHT;
                timeForNextStep = creatureOwner.existingTime + timeMovementTakes;
            }

            if (currentStep == 'w') {//nothing for random ammount of time
                timeForNextStep = creatureOwner.existingTime + randomizer.nextInt(3);
            }
            if (currentStep == 'n') {//nothing
                timeForNextStep = creatureOwner.existingTime + 1;
            }
            if (currentStep == '-') {//hide
                timeForNextStep = creatureOwner.existingTime + 1;
                result = CreatureAction.HIDE;
            }
            if (currentStep == 'z') {//nothing
                timeForNextStep = creatureOwner.existingTime + 1;
                program = "";
                result = CreatureAction.STOP;
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

    public void resetProgram() {
        if(!creatureOwner.isMachine) {
            program = DEFAULT_PROGRAM;
            steps = DEFAULT_PROGRAM.toCharArray();
        }
    }

    public void resetProgram(String prog) {
        this.program = prog;
        steps = program.toCharArray();
    }

    public boolean isEnemy(Creature mob) {
        if(globalListOfEnemies.contains(mob,true) || screen.hero.getGlobalState(organization).contains(String.valueOf(mob.getOrganization())))
            return true;
        return false;
    }

    public void addToListOfGlobalEnemies(Creature mob){
        globalListOfEnemies.add(mob);
    }

    public void removeFromListOfGlobalEnemies(Creature mob){
        globalListOfEnemies.removeValue(mob,true);
    }
}
