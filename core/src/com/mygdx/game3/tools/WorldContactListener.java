package com.mygdx.game3.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game3.HuntersGame;
import com.mygdx.game3.sprites.creatures.Creature;
import com.mygdx.game3.sprites.gameobjects.GameItem;
import com.mygdx.game3.screens.GameScreen;
import com.mygdx.game3.sprites.activities.ActivityWithEffect;
import com.mygdx.game3.stuctures.Effect;
import com.mygdx.game3.sprites.triggers.Trigger;

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
            case HuntersGame.CREATURE_BIT | HuntersGame.ACTIVITY_BIT:
                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT) {
                    act = ((ActivityWithEffect) fixB.getUserData());
                    target = ((Creature) fixA.getUserData());
                } else {
                    act = ((ActivityWithEffect) fixA.getUserData());
                    target = ((Creature) fixB.getUserData());
                }

                if (!act.isTargetACreator(target) && act.getCreator().getOrganization() != target.getOrganization()) { //&& !act.isTargetWasAlreadyProcessed(target)) {
                    for (Effect effect : act.activeEffects) {
                        target.applyEffect(effect);
                        if(!effect.id.isPositive()){
                            target.setIN_BATTLE(true);
                        }
                    }
                    //   act.addTargetToAlreadyProcessed(target);
                    act.onHit();
                }
                break;
            case HuntersGame.CREATURE_BIT | HuntersGame.ITEM_BIT:
                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).addToInventory(((GameItem) fixB.getUserData()));
                else
                    ((Creature) fixB.getUserData()).addToInventory(((GameItem) fixA.getUserData()));
                break;
            case HuntersGame.CREATURE_BIT | HuntersGame.JUMP_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setHasToJump(true);
                else
                    ((Creature) fixB.getUserData()).setHasToJump(true);
                break;
//            case HuntersGame.CREATURE_BIT | HuntersGame.CREATURE_BIT: // TODO make sure it is correct way
//                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT) {
//                    ((Creature) fixA.getUserData()).jump();
//                }
//                else {
//                    ((Creature) fixB.getUserData()).jump();
//                }
//                break;
            case HuntersGame.CREATURE_BIT | HuntersGame.NO_RIGHT_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setMoveLeft(true);
                else
                    ((Creature) fixB.getUserData()).setMoveLeft(true);
                break;
            case HuntersGame.CREATURE_BIT | HuntersGame.NO_LEFT_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setMoveRight(true);
                else
                    ((Creature) fixB.getUserData()).setMoveRight(true);
                break;
            case HuntersGame.CREATURE_BIT | HuntersGame.TRIGGER_POINT:
                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT) {
                    trigger = ((Trigger) fixB.getUserData());
                    target = ((Creature) fixA.getUserData());
                } else {
                    trigger = ((Trigger) fixA.getUserData());
                    target = ((Creature) fixB.getUserData());
                }

                target.activateTrigger(trigger);


//            case HuntersGame.OBJECT_BIT | HuntersGame.ACTIVITY_BIT:
//                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT){
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
            case HuntersGame.CREATURE_BIT | HuntersGame.NO_RIGHT_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setMoveLeft(false);
                else
                    ((Creature) fixB.getUserData()).setMoveLeft(false);
                break;
            case HuntersGame.CREATURE_BIT | HuntersGame.NO_LEFT_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT)
                    ((Creature) fixA.getUserData()).setMoveRight(false);
                else
                    ((Creature) fixB.getUserData()).setMoveRight(false);
                break;
            case HuntersGame.CREATURE_BIT | HuntersGame.JUMP_POINT: // TODO make sure it is correct way
                if (fixA.getFilterData().categoryBits == HuntersGame.CREATURE_BIT)
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
