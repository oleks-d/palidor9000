package com.mygdx.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.EffectID;
import com.mygdx.game.sprites.activities.ActivityWithEffect;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.stuctures.Effect;


public class AbilityHandler {
    static public ActivityWithEffect getAbilityAndUseIt(com.mygdx.game.screens.GameScreen screen, Creature creature, com.mygdx.game.enums.AbilityID id) {
        com.mygdx.game.sprites.activities.ActivityWithEffect result = null;

        Array<com.mygdx.game.stuctures.Effect> activeEffects = new Array<Effect>();
        int directionPlus;
        Vector2 direction;

        creature.statusbar.addMessage(id.toString(), creature.existingTime + 1, Fonts.GOOD);

        switch (id) {
                case PUNCH:

                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 2f  + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE), 0f));

                direction = new Vector2(-(creature.direction.x * 1f), creature.direction.y * 1f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM,
                        creature.getBody().getPosition().y * PalidorGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);

                break;
            case SWORD_SWING:

                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.01f, 2f  + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));

                direction = new Vector2(-(creature.direction.x * 1f), creature.direction.y * 1f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM - creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);

                break;
            case SWORD_SMASH:

                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.01f, 2f  + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));

                direction = new Vector2(0,0);

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM,
                        creature.getBody().getPosition().y * PalidorGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);

                break;
            case SHOUT:
                creature.applyEffect(new Effect(EffectID.PLUS_CRUSH_DAMAGE, 15f, 1f, 0f));
                creature.applyEffect(new Effect(EffectID.PLUS_CUT_DAMAGE, 15f, 1f, 0f));
                break;
            case POWER_SHOUT: //TODO
                creature.applyEffect(new Effect(EffectID.PLUS_CRUSH_DAMAGE, 30f, 1f, 0f));
                creature.applyEffect(new Effect(EffectID.PLUS_CUT_DAMAGE, 30f, 1f, 0f));
                break;
            case COVER:
                creature.applyEffect(new Effect(EffectID.COVERED_BY_SHIELD, 1f, 1f, 0f));
                break;
            case DASH:

                activeEffects.add(new Effect(EffectID.STUNED, 0.01f, 0f, 0f));

                direction = new Vector2(-(creature.direction.x * 10f), creature.direction.y * 10f );
                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM ,
                        creature.getBody().getPosition().y * PalidorGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);

                //move creature
                creature.getBody().applyLinearImpulse(direction, creature.getBody().getWorldCenter(), true);

                break;
            case HASTE:
                creature.applyEffect(new Effect(EffectID.FAST, 15f, 2f, 0f));
                break;
            case LONGBOW_SHOT:

                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.1f, 1f  + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));

                direction = new Vector2(-(creature.direction.x * 10f), creature.direction.y * 10f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM - creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y * PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "arrow"); //TODO anim

                result.setCreatedBy(creature);
                break;

//            case FIREWALL:
//                Gdx.app.log("Ability", "Firewall");
//
//                    activeEffects = new Array<Effect>();
//                    activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 5f, 0f));
//                    activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 3f, 5f, 0f));
//
//                    //directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE) : -(PalidorGame.TILE_SIZE);
//                    //direction = new Vector2(creature.directionRight == true ? 5 : -5, 0);
//                direction = new Vector2(-(creature.direction.x * 5f), creature.direction.y * 5f );
//
//                    result = new ActivityWithEffect(
//                            screen,
//                            creature.getBody().getPosition().x * PalidorGame.PPM,
//                            creature.getBody().getPosition().y * PalidorGame.PPM,
//                            activeEffects,
//                            id.getActivityAreaType(),
//                            direction,
//                            "firewall"); //TODO anim
//
//                result.setCreatedBy(creature);
//            //        creature.setTimeSpentOnCast(0);
//            //    }
//                break;
//            case ICEWALL:
//                Gdx.app.log("Ability", "Icewall");
//
//                activeEffects = new Array<Effect>();
//                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 5f, 1f, 1f));
//                activeEffects.add(new Effect(EffectID.ICE_DAMAGE, 10f, 5f, 0f));
//
//                //directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE/2) : -(PalidorGame.TILE_SIZE/2);
//                //direction = new Vector2(creature.directionRight == true ? 5 : -5, 0);
//                direction = new Vector2(-(creature.direction.x * 5f), creature.direction.y * 5f );
//
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "icewall"); //TODO anim
//
//                result.setCreatedBy(creature);
//
//                break;
//            case PUNCH:
//                Gdx.app.log("Ability", "Punch");
//
//                //creature.setState(State.KICKING);
//
//                activeEffects = new Array<Effect>();
//                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 2f  + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE), 0f));
//
//                //directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE/2) : -(PalidorGame.TILE_SIZE/2);
//                //direction = new Vector2(creature.directionRight == true ? 0.1f : -0.1f, 0 );
//
//                direction = new Vector2(-(creature.direction.x * 0.1f), creature.direction.y * 0.1f );
//
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "soundwall"); //TODO anim
//
//                result.setCreatedBy(creature);
//
//                break;
//            case TARGET_SHOT:
//
//                activeEffects = new Array<Effect>();
//                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.1f, 1f  + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));
//
//                //directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE/2) : -(PalidorGame.TILE_SIZE/2);
//                direction = new Vector2(-(creature.direction.x * 10), creature.direction.y * 10 );  ;//new Vector2(creature.directionRight == true ? 10f : -10f, 0 );
////Gdx.app.log("direction", direction.x + "-" + direction.y );
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM,// + directionPlus,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "arrow"); //TODO anim
//
//                result.setCreatedBy(creature);
//                break;
//
//            case STOP_CAST:
//
//                activeEffects = new Array<Effect>();
//                activeEffects.add(new Effect(EffectID.STUNED, 0.01f, 0f, 0f));
//
//                //directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE/2) : -(PalidorGame.TILE_SIZE/2);
//                //direction = new Vector2(creature.directionRight == true ? 0.1f : -0.1f, 0 );
//
//                direction = new Vector2(-(creature.direction.x * 10f), creature.direction.y * 10f );
//
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM ,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "soundwall"); //TODO anim
//
//                result.setCreatedBy(creature);
//
//                break;
//            case STUN:
//
//                activeEffects = new Array<Effect>();
//                activeEffects.add(new Effect(EffectID.STUNED, 0.01f, 0f, 0f));
//
////                directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE/2) : -(PalidorGame.TILE_SIZE/2);
////                direction = new Vector2(creature.directionRight == true ? 0.1f : -0.1f, 0 );
//
//                direction = new Vector2(-(creature.direction.x * 10f), creature.direction.y * 10f );
//
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM ,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "soundwall"); //TODO anim
//
//                result.setCreatedBy(creature);
//
//                break;
//
//            case DASH:
//
//                activeEffects = new Array<Effect>();
//                activeEffects.add(new Effect(EffectID.STUNED, 0.01f, 0f, 0f));
//
//                //directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE/2) : -(PalidorGame.TILE_SIZE/2);
//                //direction = new Vector2(creature.directionRight == true ? 10f : -10f, 0 );
//                direction = new Vector2(-(creature.direction.x * 5f), creature.direction.y * 5f );
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM ,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "soundwall"); //TODO anim
//
//                result.setCreatedBy(creature);
//
//                creature.getBody().applyLinearImpulse(direction, creature.getBody().getWorldCenter(), true);
//
//                break;
//            case HASTE:
//                creature.applyEffect(new Effect(EffectID.FAST, 15f, 5f, 0f));
//                break;
//            case PUSH:
//                activeEffects = new Array<Effect>();
//
//                //directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE/2) : -(PalidorGame.TILE_SIZE/2);
//                activeEffects.add(creature.directionRight == true ? new Effect(EffectID.MOVE_RIGHT, 0.01f, 10f, 0f):new Effect(EffectID.MOVE_LEFT, 0.01f, 10f, 0f));
//
//                direction = new Vector2(creature.directionRight == true ? 5f : -5f, 0 );
//
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM ,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "soundwall"); //TODO anim
//
//                result.setCreatedBy(creature);
//                break;
//            case PULL:
//                activeEffects = new Array<Effect>();
//
//                //directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE/2) : -(PalidorGame.TILE_SIZE/2);
//                activeEffects.add(creature.directionRight == true ? new Effect(EffectID.MOVE_LEFT, 0.01f, 10f, 0f):new Effect(EffectID.MOVE_RIGHT, 0.01f, 10f, 0f));
//                //direction = new Vector2(creature.directionRight == true ? 10f : -10f, 0 );
//
//                direction = new Vector2(-(creature.direction.x * 0.1f), creature.direction.y * 0.1f );
//
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM ,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "soundwall"); //TODO anim
//
//                result.setCreatedBy(creature);
//                break;
        }

        creature.creatureAim.update(id.getActivityAreaType());
        return result;
    }

    //cast time can be shorter/longer for creature
    // TODO add creture affects on cast time
    public static float getAbilityCastTime(Creature creature, com.mygdx.game.enums.AbilityID ability) {
                return ability.getCastTime();
    }

    //cast time can be shorter/longer for creature
    // TODO add creture affects on cast time
    public static float getAbilityCooldownTime(Creature creature, com.mygdx.game.enums.AbilityID ability) {
        return ability.getCooldownTime();
    }

}
