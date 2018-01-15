package com.mygdx.game3.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game3.HuntersGame;
import com.mygdx.game3.enums.*;
import com.mygdx.game3.screens.GameScreen;
import com.mygdx.game3.enums.ActivityAreaType;
import com.mygdx.game3.sprites.activities.ActivityWithEffect;
import com.mygdx.game3.sprites.creatures.Creature;
import com.mygdx.game3.stuctures.Effect;


public class AbilityHandler {
    static public ActivityWithEffect getAbilityAndUseIt(GameScreen screen, Creature creature, AbilityID id) {
        ActivityWithEffect result = null;

        Array<Effect> activeEffects;
        int directionPlus;
        Vector2 direction;

        switch (id) {
            case FIREWALL:
                Gdx.app.log("Ability", "Firewall");

                    activeEffects = new Array<Effect>();
                    activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 5f, 0f));
                    activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 3f, 5f, 0f));

                    //directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE) : -(HuntersGame.TILE_SIZE);
                    //direction = new Vector2(creature.directionRight == true ? 5 : -5, 0);
                direction = new Vector2(-(creature.direction.x * 5f), creature.direction.y * 5f );

                    result = new ActivityWithEffect(
                            screen,
                            creature.getBody().getPosition().x * HuntersGame.PPM,
                            creature.getBody().getPosition().y * HuntersGame.PPM,
                            activeEffects,
                            id.getActivityAreaType(),
                            direction,
                            "firewall"); //TODO anim

                result.setCreatedBy(creature);
            //        creature.setTimeSpentOnCast(0);
            //    }
                break;
            case ICEWALL:
                Gdx.app.log("Ability", "Icewall");

                activeEffects = new Array<Effect>();
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 5f, 1f, 1f));
                activeEffects.add(new Effect(EffectID.ICE_DAMAGE, 10f, 5f, 0f));

                //directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE/2) : -(HuntersGame.TILE_SIZE/2);
                //direction = new Vector2(creature.directionRight == true ? 5 : -5, 0);
                direction = new Vector2(-(creature.direction.x * 5f), creature.direction.y * 5f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "icewall"); //TODO anim

                result.setCreatedBy(creature);

                break;
            case PUNCH:
                Gdx.app.log("Ability", "Punch");

                //creature.setState(State.KICKING);

                activeEffects = new Array<Effect>();
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 2f  + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE), 0f));

                //directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE/2) : -(HuntersGame.TILE_SIZE/2);
                //direction = new Vector2(creature.directionRight == true ? 0.1f : -0.1f, 0 );

                direction = new Vector2(-(creature.direction.x * 0.1f), creature.direction.y * 0.1f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);

                break;
            case TARGET_SHOT:

                activeEffects = new Array<Effect>();
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.1f, 1f  + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));

                //directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE/2) : -(HuntersGame.TILE_SIZE/2);
                direction = new Vector2(-(creature.direction.x * 10), creature.direction.y * 10 );  ;//new Vector2(creature.directionRight == true ? 10f : -10f, 0 );
//Gdx.app.log("direction", direction.x + "-" + direction.y );
                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM,// + directionPlus,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "arrow"); //TODO anim

                result.setCreatedBy(creature);
                break;

            case STOP_CAST:

                activeEffects = new Array<Effect>();
                activeEffects.add(new Effect(EffectID.STUNED, 0.01f, 0f, 0f));

                //directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE/2) : -(HuntersGame.TILE_SIZE/2);
                //direction = new Vector2(creature.directionRight == true ? 0.1f : -0.1f, 0 );

                direction = new Vector2(-(creature.direction.x * 10f), creature.direction.y * 10f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM ,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);

                break;
            case STUN:

                activeEffects = new Array<Effect>();
                activeEffects.add(new Effect(EffectID.STUNED, 0.01f, 0f, 0f));

//                directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE/2) : -(HuntersGame.TILE_SIZE/2);
//                direction = new Vector2(creature.directionRight == true ? 0.1f : -0.1f, 0 );

                direction = new Vector2(-(creature.direction.x * 10f), creature.direction.y * 10f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM ,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);

                break;

            case DASH:

                activeEffects = new Array<Effect>();
                activeEffects.add(new Effect(EffectID.STUNED, 0.01f, 0f, 0f));

                //directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE/2) : -(HuntersGame.TILE_SIZE/2);
                //direction = new Vector2(creature.directionRight == true ? 10f : -10f, 0 );
                direction = new Vector2(-(creature.direction.x * 5f), creature.direction.y * 5f );
                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM ,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);

                creature.getBody().applyLinearImpulse(direction, creature.getBody().getWorldCenter(), true);

                break;
            case HASTE:
                creature.applyEffect(new Effect(EffectID.FAST, 15f, 5f, 0f));
                break;
            case PUSH:
                activeEffects = new Array<Effect>();

                //directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE/2) : -(HuntersGame.TILE_SIZE/2);
                activeEffects.add(creature.directionRight == true ? new Effect(EffectID.MOVE_RIGHT, 0.01f, 10f, 0f):new Effect(EffectID.MOVE_LEFT, 0.01f, 10f, 0f));

                direction = new Vector2(creature.directionRight == true ? 5f : -5f, 0 );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM ,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);
                break;
            case PULL:
                activeEffects = new Array<Effect>();

                //directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE/2) : -(HuntersGame.TILE_SIZE/2);
                activeEffects.add(creature.directionRight == true ? new Effect(EffectID.MOVE_LEFT, 0.01f, 10f, 0f):new Effect(EffectID.MOVE_RIGHT, 0.01f, 10f, 0f));
                //direction = new Vector2(creature.directionRight == true ? 10f : -10f, 0 );

                direction = new Vector2(-(creature.direction.x * 0.1f), creature.direction.y * 0.1f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM ,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);
                break;
        }

        creature.creatureAim.update(id.getActivityAreaType());
        return result;
    }

    //cast time can be shorter/longer for creature
    // TODO add creture affects on cast time
    public static float getAbilityCastTime(Creature creature, AbilityID ability) {
                return ability.getCastTime();
    }

    //cast time can be shorter/longer for creature
    // TODO add creture affects on cast time
    public static float getAbilityCooldownTime(Creature creature, AbilityID ability) {
        return ability.getCooldownTime();
    }

}
