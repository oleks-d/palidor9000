package com.mygdx.game.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.EffectID;
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
                    if(target.shieldEffect == null) {// creature was covered
                        if(target.getEffect(EffectID.DODGE) == null) { // creature dodges
                            for (Effect effect : act.activeEffects) {
                                target.applyEffect(effect);
                                if (!effect.id.isPositive()) {
                                    target.setIN_BATTLE(true);
                                }
                            }
                        } else {
                            target.addStatusMessage("Damage avoided by dodge", Fonts.GOOD);
                        }
                    } else {
                        target.addStatusMessage("Shield absorbed damage", Fonts.GOOD);
                    }

                    act.onHit();
                }
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
                    if(target.shieldEffect == null) {// creature was covered
                        if(target.getEffect(EffectID.DODGE) == null) { // creature dodges
                            for (Effect effect : act.activeEffects) {
                                target.applyEffect(effect);
                                if (!effect.id.isPositive()) {
                                    target.setIN_BATTLE(true);
                                }
                            }
                        } else {
                            target.addStatusMessage("Damage avoided by dodge", Fonts.GOOD);
                        }
                    } else {
                        target.addStatusMessage("Shield absorbed damage", Fonts.GOOD);
                        act.createdBy.shake();
                        act.createdBy.applyEffect(target.shieldEffect); // stun if target was protected
                    }
                    //   act.addTargetToAlreadyProcessed(target);
                    act.onHit();
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
            case PalidorGame.CREATURE_BIT | PalidorGame.JUMP_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setHasToJump(true);
                else
                    ((Creature) fixB.getUserData()).setHasToJump(true);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.STAND_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setStandStill(true);
                else
                    ((Creature) fixB.getUserData()).setStandStill(true);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.MOVE_RIGHT_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setMoveRight(true);
                else
                    ((Creature) fixB.getUserData()).setMoveRight(true);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.MOVE_LEFT_POINT: // TODO make sure it is correct way
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
                ((Creature) fixA.getUserData()).setInvisible(false);
                ((Creature) fixB.getUserData()).setInvisible(false);

                //neighbor
                ((Creature) fixA.getUserData()).setNeighbor(((Creature) fixB.getUserData()));
                ((Creature) fixB.getUserData()).setNeighbor(((Creature) fixA.getUserData()));
                break;
//            case PalidorGame.GROUND_BIT | PalidorGame.ACTIVITY_BIT:
//                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT){
//                    act = ((ActivityWithEffect) fixB.getUserData());
//                } else {
//                    act = ((ActivityWithEffect) fixA.getUserData());
//                }
//                act.onHit();
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

//            case PalidorGame.CREATURE_BIT | PalidorGame.NO_RIGHT_POINT: // TODO make sure it is correct way
//                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
//                    ((Creature) fixA.getUserData()).setMoveLeft(false);
//                else
//                    ((Creature) fixB.getUserData()).setMoveLeft(false);
//                break;
//            case PalidorGame.CREATURE_BIT | PalidorGame.NO_LEFT_POINT: // TODO make sure it is correct way
//                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
//                    ((Creature) fixA.getUserData()).setMoveRight(false);
//                else
//                    ((Creature) fixB.getUserData()).setMoveRight(false);
//                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.STAND_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setStandStill(false);
                else
                    ((Creature) fixB.getUserData()).setStandStill(false);
                break;
            case PalidorGame.CREATURE_BIT | PalidorGame.JUMP_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == PalidorGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setHasToJump(false);
                else
                    ((Creature) fixB.getUserData()).setHasToJump(false);
                break;
        }


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
