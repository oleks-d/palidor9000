package com.mygdx.game.tools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.AbilityType;
import com.mygdx.game.enums.EffectID;
import com.mygdx.game.enums.State;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.sprites.activities.ActivityWithEffect;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.sprites.gameobjects.GameObject;
import com.mygdx.game.sprites.triggers.Trigger;
import com.mygdx.game.stuctures.Effect;

/**
 * Created by odiachuk on 12/18/17.
 */
public class WorldContactListener implements ContactListener {
    GameScreen gameScreen;
    public WorldContactListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        Creature creatureA;
        Creature creatureB;

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        ActivityWithEffect act;
        Creature target;
        Trigger trigger;

        switch (cDef) {
            case PalidorGame.CREATURE_BIT | PalidorGame.ACTIVITY_BIT:
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT) {
                    act = ((ActivityWithEffect) fixB.getUserData());
                    target = ((Creature) fixA.getUserData());
                } else {
                    act = ((ActivityWithEffect) fixA.getUserData());
                    target = ((Creature) fixB.getUserData());
                }

                //creature belongs to your fraction
                if (!act.isTargetACreator(target) && act.getCreator().getOrganization() != target.getOrganization()) { //&& !act.isTargetWasAlreadyProcessed(target)) {
                    if(!act.isTargetWasAlreadyProcessed(target)) {
                        if (target.shieldEffect == null) {// creature was not covered
                            if (target.getEffect(EffectID.DODGE) == null) { // creature dodges
                                for (Effect effect : act.activeEffects) {
                                    target.applyEffect(effect);
                                    if (!effect.id.isPositive()) {
                                        target.setIN_BATTLE(true);
                                        target.brain.addToListOfGlobalEnemies(act.createdBy);
                                        target.setCharmed(false);
                                    }
                                }
                            } else {
                                target.addStatusMessage("Damage avoided by dodge", Fonts.GOOD);
                            }
                        } else {
                            if(target.getState() == State.FALLING)
                                target.moveUp();
                            target.addStatusMessage("Shield absorbed damage", Fonts.GOOD);
                        }
                        act.addTargetToAlreadyProcessed(target);
                    }
                }
                act.onHit(target);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.ATTACK_BIT:
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT) {
                    act = ((ActivityWithEffect) fixB.getUserData());
                    target = ((Creature) fixA.getUserData());
                } else {
                    act = ((ActivityWithEffect) fixA.getUserData());
                    target = ((Creature) fixB.getUserData());
                }


                //creature belongs to your fraction
                if (!act.isTargetACreator(target) && act.getCreator().getOrganization() != target.getOrganization()) { //&& !act.isTargetWasAlreadyProcessed(target)) {
                    if(!act.isTargetWasAlreadyProcessed(target)) {
                        if (target.shieldEffect == null) {// creature was covered
                            if (target.getEffect(EffectID.DODGE) == null) { // creature dodges
                                for (Effect effect : act.activeEffects) {
                                    target.applyEffect(effect);
                                    if (!effect.id.isPositive()) {
                                        target.setIN_BATTLE(true);
                                        target.brain.addToListOfGlobalEnemies(act.createdBy);
                                        target.setCharmed(false);
                                    }
                                }
                                //backstub  TODO fix
                                if (act.createdBy.getAbilities().contains(AbilityID.BACKSTUB, false) && ((target.directionRight && act.createdBy.directionRight) || (!target.directionRight && !act.createdBy.directionRight)))
                                    target.applyEffect(new Effect(EffectID.POISON, 0.01f, 5f, 0f));

                            } else {
                                target.addStatusMessage("Damage avoided by dodge", Fonts.GOOD);
                            }
                        } else {
                            if(target.getState() == State.FALLING)
                                target.moveUp();
                            target.addStatusMessage("Shield absorbed damage", Fonts.GOOD);
                            act.createdBy.shake();
                            act.createdBy.applyEffect(target.shieldEffect); // stun if target was protected
                        }

                        act.addTargetToAlreadyProcessed(target);
                    }
                }
                act.onHit(target);
                break;

            case PalidorGame.GROUND_BIT | PalidorGame.ACTIVITY_BIT:
                if (fixA.getFilterData().categoryBits == PalidorGame.ACTIVITY_BIT) {
                    ((ActivityWithEffect) fixA.getUserData()).onHit();
                } else {
                   ((ActivityWithEffect) fixB.getUserData()).onHit();
                }
                break;

            case PalidorGame.CREATURE_BIT | PalidorGame.ITEM_BIT:
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).addToInventory(((GameItem) fixB.getUserData()));
                else
                    ((Creature) fixB.getUserData()).addToInventory(((GameItem) fixA.getUserData()));
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).touchObject(((GameObject) fixB.getUserData()));
                else
                    ((Creature) fixB.getUserData()).touchObject(((GameObject) fixA.getUserData()));
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.JUMP_LEFT: 
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setHasToJump(false, true);
                else
                    ((Creature) fixB.getUserData()).setHasToJump(false, true);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.JUMP_RIGHT: 
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setHasToJump(true, true);
                else
                    ((Creature) fixB.getUserData()).setHasToJump(true, true);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.STAND_POINT: 
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setStandStill(true);
                else
                    ((Creature) fixB.getUserData()).setStandStill(true);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.MOVE_RIGHT_POINT: 
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setMoveRight(true);
                else
                    ((Creature) fixB.getUserData()).setMoveRight(true);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.MOVE_LEFT_POINT: 
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setMoveLeft(true);
                else
                    ((Creature) fixB.getUserData()).setMoveLeft(true);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.TRIGGER_POINT:
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT) {
                    trigger = ((com.mygdx.game.sprites.triggers.Trigger) fixB.getUserData());
                    target = ((Creature) fixA.getUserData());
                } else {
                    trigger = ((com.mygdx.game.sprites.triggers.Trigger) fixA.getUserData());
                    target = ((Creature) fixB.getUserData());
                }

                target.activateTrigger(trigger);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.CREATURE_BIT:
                // not hidden
                creatureA = ((Creature) fixA.getUserData());
                creatureB = ((Creature) fixB.getUserData());

                creatureA.setInvisible(false);
                creatureB.setInvisible(false);

                //neighbor
                creatureA.setNeighbor(creatureB);
                creatureB.setNeighbor(creatureA);

                //
                creatureA.applyAura(creatureB);
                creatureB.applyAura(creatureA);

                break;
            case PalidorGame.GROUND_BIT | PalidorGame.CREATURE_BOTTOM:
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BOTTOM) {
                    ((Creature) fixA.getUserData()).onAGround(true);
                } else {
                    ((Creature) fixB.getUserData()).onAGround(true);
                }
                break;
            case PalidorGame.OBJECT_BIT | PalidorGame.CREATURE_BOTTOM:
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BOTTOM) {
                    ((Creature) fixA.getUserData()).onAGround(true);
                } else {
                    ((Creature) fixB.getUserData()).onAGround(true);
                }
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.CREATURE_BOTTOM:
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BOTTOM) {
                    //((Creature) fixA.getUserData()).moveUp();
                    ((Creature) fixA.getUserData()).onAGround(true);
                } else {
                    //((Creature) fixB.getUserData()).moveUp();
                    ((Creature) fixB.getUserData()).onAGround(true);
                }
                break;


//            case PalidorGame.GROUND_BIT | PalidorGame.ACTIVITY_BIT:
//                if (fixA.getFilterData().categoryBits == PalidorGame.GROUND_BIT){
//                    act = ((ActivityWithEffect) fixB.getUserData());
//                } else {
//                    act = ((ActivityWithEffect) fixA.getUserData());
//                }
//                act.onHit();
//                break;
//            case PalidorGame.GROUND_BIT | PalidorGame.CREATURE_BIT:
//                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT){
//                    ((Creature) fixA.getUserData()).setGroundLimits((PolygonShape)fixB.getShape());
//                } else {
//                    ((Creature) fixB.getUserData()).setGroundLimits((PolygonShape)fixA.getShape());
//                }

//                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case PalidorGame.CREATURE_BIT | PalidorGame.CREATURE_BIT:

                //neighbor
                ((Creature) fixA.getUserData()).setNeighbor(null);
                ((Creature) fixB.getUserData()).setNeighbor(null);
                break;

            case PalidorGame.CREATURE_BIT | PalidorGame.MOVE_LEFT_POINT: 
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setMoveLeft(false);
                else
                    ((Creature) fixB.getUserData()).setMoveLeft(false);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.MOVE_RIGHT_POINT: 
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setMoveRight(false);
                else
                    ((Creature) fixB.getUserData()).setMoveRight(false);
                break;
//            case PalidorGame.CREATURE_BIT | PalidorGame.STAND_POINT: 
//                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
//                    ((Creature) fixA.getUserData()).setStandStill(false);
//                else
//                    ((Creature) fixB.getUserData()).setStandStill(false);
//                break;
//            case PalidorGame.CREATURE_BIT | PalidorGame.JUMP_POINT: 
//                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
//                    ((Creature) fixA.getUserData()).setHasToJump(false);
//                else
//                    ((Creature) fixB.getUserData()).setHasToJump(false);
//                break;
        }


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
