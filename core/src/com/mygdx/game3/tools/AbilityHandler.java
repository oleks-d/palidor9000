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
            case FIREBALL:
                Gdx.app.log("Ability", "Fireball");

                    activeEffects = new Array<Effect>();
                    activeEffects.add(new Effect(EffectID.DAMAGE, 0.01f, 5f, 0f));
                    activeEffects.add(new Effect(EffectID.IN_FIRE, 3f, 5f, 0f));

                    directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE) : -(HuntersGame.TILE_SIZE);
                    direction = new Vector2(creature.directionRight == true ? 5 : -5, 0);

                    result = new ActivityWithEffect(
                            screen,
                            creature.getBody().getPosition().x * HuntersGame.PPM + directionPlus,
                            creature.getBody().getPosition().y * HuntersGame.PPM,
                            activeEffects,
                            ActivityAreaType.SPRAY,
                            direction,
                            "firewall"); //TODO anim

                result.setCreatedBy(creature);
            //        creature.setTimeSpentOnCast(0);
            //    }
                break;
            case ICEBALL:
                Gdx.app.log("Ability", "Iceball");

                activeEffects = new Array<Effect>();
                activeEffects.add(new Effect(EffectID.DAMAGE, 5f, 1f, 1f));
                activeEffects.add(new Effect(EffectID.IN_ICE, 10f, 5f, 0f));

                directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE) : -(HuntersGame.TILE_SIZE);
                direction = new Vector2(creature.directionRight == true ? 5 : -5, 0);

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM + directionPlus,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        ActivityAreaType.SPRAY,
                        direction,
                        "icewall"); //TODO anim

                result.setCreatedBy(creature);

                break;
            case PUNCH:
                Gdx.app.log("Ability", "Punch");

                creature.setState(State.KICKING);

                activeEffects = new Array<Effect>();
                activeEffects.add(new Effect(EffectID.DAMAGE, 0.01f, 2f, 0f));

                directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE) : -(HuntersGame.TILE_SIZE);
                direction = new Vector2(creature.directionRight == true ? 0.1f : -0.1f, 0 );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM + directionPlus,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        ActivityAreaType.BOX,
                        direction,
                        "soundwall"); //TODO anim

                result.setCreatedBy(creature);

                break;
            case ARROW:

                Gdx.app.log("Ability", "Arrow");

                creature.setState(State.KICKING);

                activeEffects = new Array<Effect>();
                activeEffects.add(new Effect(EffectID.DAMAGE, 0.1f, 1f, 0f));

                directionPlus = creature.directionRight == true ? (HuntersGame.TILE_SIZE) : -(HuntersGame.TILE_SIZE);
                direction = new Vector2(creature.directionRight == true ? 10f : -10f, 0 );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * HuntersGame.PPM + directionPlus,
                        creature.getBody().getPosition().y * HuntersGame.PPM,
                        activeEffects,
                        ActivityAreaType.ARROW,
                        direction,
                        "arrow"); //TODO anim

                result.setCreatedBy(creature);
                break;
        }

        return result;
    }

    public static float getAbilityCastTime(Creature creature, AbilityID ability) {
        switch (ability) {
            case ICEBALL:
                return 2;
            case FIREBALL:
                return 2;
            default:
                return 0.1f;
        }
    }
}
