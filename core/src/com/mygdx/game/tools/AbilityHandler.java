package com.mygdx.game.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.ActivityAreaType;
import com.mygdx.game.enums.EffectID;
import com.mygdx.game.enums.State;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.activities.ActivityWithEffect;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.stuctures.Effect;


public class AbilityHandler {
    static public Array<ActivityWithEffect> getAbilityAndUseIt(GameScreen screen, Creature creature, AbilityID id) {
        Array<ActivityWithEffect> results = new Array<ActivityWithEffect>();
        ActivityWithEffect result = null;

        Array<Effect> activeEffects = new Array<Effect>();
        int directionPlus;
        Vector2 direction;

        creature.statusbar.addMessage(id.getName(), creature.existingTime + 1, Fonts.GOOD);

        switch (id) {
                case POWERJUMP:
                    //if(creature.getState()== State.JUMPING)
                        creature.stopMovement();
                        creature.moveUp();
                    break;
            case FLY:
                //if(creature.getState()== State.FALLING)
                    //creature.moveUp(3);
                creature.stopMovement();
                creature.applyEffect(new Effect(EffectID.NO_MASS, 2f, 0.1f, 0f));
                break;
            case ANIMAL_PUNCH:
            case PUNCH:

                    if(creature.directionRight)
                        activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                    else
                        activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));
                    activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 2f, 0f));
                    activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 2f  + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE), 0f));


                direction = new Vector2((creature.direction.x * 1f), creature.direction.y * 1f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE/2,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall");

                result.setCreatedBy(creature);
                    results.add(result);

                break;
            case POWERPUNCH:

                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 3f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 3f, 0f));
                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 1f  + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 1f), creature.direction.y * 1f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall2");

                result.setCreatedBy(creature);
                results.add(result);

                break;


            case UPPERPUNCH:

                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));
                activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 3f, 0f));
                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 1f  + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 1f), creature.direction.y * 1f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall3");

                result.setCreatedBy(creature);
                results.add(result);

                break;


            case ANIMAL_POKE:

                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));
                activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.01f, 2f  + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 1f), creature.direction.y * 1f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall");

                result.setCreatedBy(creature);
                results.add(result);

                break;
            case ANIMAL_CLAW:
            case SWORD_SWING:
                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));

                activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.01f, 1f  + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));
                direction = new Vector2((creature.direction.x * 1f), creature.direction.y * 1f );
                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall3");
                result.setCreatedBy(creature);
                results.add(result);

                break;
            case SWORD_SMASH2:
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.01f, 2f, 0f));
            case SWORD_SMASH:
                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));
                activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.01f, 2f  + 2*creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));
                direction = new Vector2(3,0);

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall2");

                result.setCreatedBy(creature);
                results.add(result);
                break;


            case HUMMER_SWING:
                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));
                activeEffects.add(new Effect(EffectID.STUNED, 0.3f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 1f  + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE), 0f));
                direction = new Vector2((creature.direction.x * 1f), creature.direction.y * 1f );
                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall2");
                result.setCreatedBy(creature);
                results.add(result);
                break;
            case HUMMER_SMASH2:
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 2f,0f));
            case HUMMER_SMASH:
                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 2f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 2f, 0f));
                activeEffects.add(new Effect (EffectID.STUNED, 3,0,0));
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 1f  + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE), 0f));
                direction  = new Vector2((creature.direction.x * 1f), creature.direction.y * 1f );
                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall2");
                result.setCreatedBy(creature);
                results.add(result);
                screen.shake(2);
                break;
            case RAGE:
                creature.applyEffect(new Effect(EffectID.PLUS_CRUSH_DAMAGE, 15f, 3f, 0f));
                creature.applyEffect(new Effect(EffectID.PLUS_CUT_DAMAGE, 15f, 3f, 0f));
                break;

            case MAGIC_RAGE:
                creature.applyEffect(new Effect(EffectID.PLUS_ICE_DAMAGE, 15f, 3f, 0f));
                creature.applyEffect(new Effect(EffectID.PLUS_FIRE_DAMAGE, 15f, 3f, 0f));
                break;



            case COVER:
                creature.applyEffect(new Effect(EffectID.COVERED_BY_SHIELD, 1f, 1f, 0f));
                creature.shieldEffect = new Effect (EffectID.STUNED, 2,0,0);
                break;

            case FIRESHIELD:
                creature.applyEffect(new Effect(EffectID.COVERED_BY_FIRE_SHIELD, 1f, 1f, 0f));
                creature.shieldEffect = new Effect (EffectID.FIRE_DAMAGE, 3,3,0);
                break;


            case BARSKIN:
                creature.applyEffect(new Effect(EffectID.MINUS_CRUSH_DAMAGE, 15f, 3f, 0f));
                creature.applyEffect(new Effect(EffectID.MINUS_CUT_DAMAGE, 15f, 3f, 0f));
                creature.applyEffect(new Effect(EffectID.MINUS_FIRE_DAMAGE, 15f, 3f, 0f));
                creature.applyEffect(new Effect(EffectID.MINUS_ICE_DAMAGE, 15f, 3f, 0f));
                break;
            case FULLPROTECTION:
                creature.applyEffect(new Effect(EffectID.IMMUNE_CRUSH_DAMAGE, 15f, 6f, 0f));
                creature.applyEffect(new Effect(EffectID.IMMUNE_CUT_DAMAGE, 15f, 6f, 0f));
                creature.applyEffect(new Effect(EffectID.IMMUNE_FIRE_DAMAGE, 15f, 6f, 0f));
                creature.applyEffect(new Effect(EffectID.IMMUNE_ICE_DAMAGE, 15f, 6f, 0f));
                break;

            case HASTE:
                creature.applyEffect(new Effect(EffectID.FAST, 15f, 10f, 0f));
                break;

//            case DODGE: // TODO not used
//                creature.applyEffect(new Effect(EffectID.DODGE, 1f, 1f, 0f));
//                //creature.shieldEffect = new Effect (EffectID.STUNED, 3,0,0);
//                break;

            case ANIMAL_DASH:
            case DASH:

                activeEffects.add(new Effect(EffectID.STUNED, 0.5f, 0f, 0f));

                direction = new Vector2((creature.direction.x * 5f), creature.direction.y * 5f );
                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM ,
                        creature.getBody().getPosition().y * PalidorGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall");

                result.setCreatedBy(creature);
                results.add(result);

                //move creature
                creature.applyEffect(new Effect(EffectID.NO_MASS, 0.5f, 0f, 0f));
                creature.getBody().applyLinearImpulse(direction, creature.getBody().getWorldCenter(), true);

                break;


            case LONGBOW_SHOT:

                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));
                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.1f, 1f  + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 5f), creature.direction.y * 5f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y * PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "arrow"); 

                result.setCreatedBy(creature);
                results.add(result);
                break;
            case SLING_SHOT:

                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.1f, 1f  + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 3.5f), 3.5f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y * PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "arrow_rock"); 

                result.setCreatedBy(creature);
                results.add(result);
                break;
            case ACID_SHOT:

                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.POISON, 1f, 1f, 0f));

                direction = new Vector2((creature.direction.x * 5f), creature.direction.y * 5f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y * PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "arrow_acid"); 

                result.setCreatedBy(creature);
                results.add(result);
                break;
            case SPIKE_SHOT:

                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.1f, 2f + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 5f), creature.direction.y * 5f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y * PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "arrow_spike"); 

                result.setCreatedBy(creature);
                results.add(result);
                break;
            case TURREL_PIU:

                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.1f, 2f + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 5f), creature.direction.y * 5f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y * PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "arrow");

                result.setCreatedBy(creature);
                results.add(result);
                break;
            case TRIPLE_SHOT:

                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));
                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.1f, 1f  + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE), 0f));

                direction = new Vector2((creature.directionRight?1:-1) * 5f,0f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * 0.5f * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y * 0.5f * PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "arrow"); 
                result.setCreatedBy(creature);
                results.add(result);

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * 1.5f * PalidorGame.TILE_SIZE ,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y * 1.5f * PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "arrow");
                result.setCreatedBy(creature);
                results.add(result);

                break;



            case FIREWALL:

                    activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));
                activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 3f, 2f  + creature.getEffectsSum(EffectID.PLUS_FIRE_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 3f), creature.direction.y * 3f );

                    result = new ActivityWithEffect(
                            screen,
                            creature.getBody().getPosition().x * PalidorGame.PPM,
                            creature.getBody().getPosition().y * PalidorGame.PPM,
                            activeEffects,
                            id.getActivityAreaType(),
                            direction,
                            "firewall"); 

                result.setCreatedBy(creature);
                results.add(result);

                break;
            case FIREBALL:

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 1f + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE) , 0f));
                activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 3f, 2f  + creature.getEffectsSum(EffectID.PLUS_FIRE_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 3.5f),  0 );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x* PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "firewall"); 

                result.setCreatedBy(creature);
                results.add(result);
                break;


            case ICEWALL:

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                if(creature.directionRight)
                    activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 1f, 0f));
                else
                    activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 1f, 0f));
                activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 3f, 2f  + creature.getEffectsSum(EffectID.PLUS_FIRE_DAMAGE), 0f));

                direction = new Vector2((creature.direction.x * 3f), 0 );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM,
                        creature.getBody().getPosition().y * PalidorGame.PPM,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "icewall");

                result.setCreatedBy(creature);
                results.add(result);

                break;
            case ICESTORM:

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CUT_DAMAGE, 0.01f, 1f + creature.getEffectsSum(EffectID.PLUS_CUT_DAMAGE) , 0f));
                activeEffects.add(new Effect(EffectID.ICE_DAMAGE, 3f, 2f  + creature.getEffectsSum(EffectID.PLUS_ICE_DAMAGE), 0f));
                activeEffects.add(new Effect(EffectID.SLOW, 3f, 2f, 0f));

                direction = new Vector2((creature.direction.x * 3.5f),  0f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x* PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "icewall");

                result.setCreatedBy(creature);
                results.add(result);
                break;

            case MASK1:
                creature.startAnimation(creature.hideAnimation);
                creature.applyEffect(new Effect(EffectID.INVISIBLE, 5f, 0f, 0f));
                break;
            case MASK2:
                creature.startAnimation(creature.hideAnimation);
                creature.applyEffect(new Effect(EffectID.INVISIBLE, 10f, 0f, 0f));
                break;
            case MASK3:
                creature.startAnimation(creature.hideAnimation);
                creature.applyEffect(new Effect(EffectID.INVISIBLE, 15f, 0f, 0f));
                break;
            case PICKPOCKET:

                activeEffects.add(new Effect(EffectID.THROW_FROM_INVENTORY, 0.01f, 1f, 0f));

                direction = new Vector2((creature.direction.x * 1f), creature.direction.y * 1f );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x * PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "soundwall");

                result.setCreatedBy(creature);
                //creature.applyEffect(new Effect(EffectID.INVISIBLE, 0f, 0f, 0f));
                results.add(result);

                break;

            case SUMMON_MARK:
                    creature.summonedCreature = "mark";
                    screen.levelmanager.createSummonedCreature(screen, creature.getX()*PalidorGame.PPM + creature.direction.x* PalidorGame.TILE_SIZE , creature.getY()*PalidorGame.PPM, "mark", creature);
                break;
            case SUMMON_ROLF:
                creature.summonedCreature = "animal_rolf";
                screen.levelmanager.createSummonedCreature(screen, creature.getX()*PalidorGame.PPM - creature.direction.x* PalidorGame.TILE_SIZE , creature.getY()*PalidorGame.PPM, "animal_rolf", creature);

                break;
            case SUMMON_DEMON:
                creature.summonedCreature = "demon_1";
                screen.levelmanager.createSummonedCreature(screen, creature.getX()*PalidorGame.PPM + creature.direction.x* PalidorGame.TILE_SIZE , creature.getY()*PalidorGame.PPM, "demon_1", creature);
                break;
            case SUMMON_BAD_DEMON:
                creature.summonedCreature = "demon_1";
                screen.levelmanager.createCreature(screen, creature.getX()*PalidorGame.PPM + creature.direction.x* PalidorGame.TILE_SIZE , creature.getY()*PalidorGame.PPM, "demon_1", 9);
                break;

//            case STOP_CAST:
//
//                activeEffects = new Array<Effect>();
//                activeEffects.add(new Effect(EffectID.STUNED, 0.01f, 0f, 0f));
//
//                //directionPlus = creature.directionRight == true ? (PalidorGame.TILE_SIZE/2) : -(PalidorGame.TILE_SIZE/2);
//                //direction = new Vector2(creature.directionRight == true ? 0.1f : -0.1f, 0 );
//
//                direction = new Vector2((creature.direction.x * 10f), creature.direction.y * 10f );
//
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM ,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "soundwall2");
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
//                direction = new Vector2((creature.direction.x * 10f), creature.direction.y * 10f );
//
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM ,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "soundwall2");
//
//                result.setCreatedBy(creature);
//
//                break;
//
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
//                        "soundwall2");
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
//                direction = new Vector2((creature.direction.x * 0.1f), creature.direction.y * 0.1f );
//
//                result = new ActivityWithEffect(
//                        screen,
//                        creature.getBody().getPosition().x * PalidorGame.PPM ,
//                        creature.getBody().getPosition().y * PalidorGame.PPM,
//                        activeEffects,
//                        id.getActivityAreaType(),
//                        direction,
//                        "soundwall2");
//
//                result.setCreatedBy(creature);
//                break;


            case ACID_SPLASH:

                activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
                activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.01f, 1f + creature.getEffectsSum(EffectID.PLUS_CRUSH_DAMAGE) , 0f));
                activeEffects.add(new Effect(EffectID.POISON, 3f, 2f, 0f));

                direction = new Vector2(0,  3 );

                result = new ActivityWithEffect(
                        screen,
                        creature.getBody().getPosition().x * PalidorGame.PPM + creature.direction.x* PalidorGame.TILE_SIZE,
                        creature.getBody().getPosition().y * PalidorGame.PPM + creature.direction.y* PalidorGame.TILE_SIZE,
                        activeEffects,
                        id.getActivityAreaType(),
                        direction,
                        "firewall");

                result.setCreatedBy(creature);
                results.add(result);
                break;
        }

        //creature.creatureAim.update(id.getActivityAreaType());
        return results;
    }

    //cast time can be shorter/longer for creature
    // TODO add creature effects on cast time
    public static float getAbilityCastTime(Creature creature, AbilityID ability) {
                return ability.getCastTime();
    }

    //cast time can be shorter/longer for creature
    // TODO add creature effects on cast time
    public static float getAbilityCooldownTime(Creature creature, AbilityID ability) {
        return ability.getCooldownTime();
    }



    public static void explosion(GameScreen screen, Creature creature, float x, float y) {

        Array<ActivityWithEffect> results = new Array<ActivityWithEffect>();
        ActivityWithEffect result = null;

        Array<Effect> activeEffects = new Array<Effect>();
        Vector2 direction;

        activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 3f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 0.1f, 1f, 0f));

        direction = new Vector2(3f,0f);

        result = new ActivityWithEffect(
                screen,
                x + creature.direction.x* PalidorGame.TILE_SIZE,
                y + creature.direction.y* PalidorGame.TILE_SIZE,
                activeEffects,
                ActivityAreaType.SPRAY,
                direction,
                "firewall", false); 

        result.setCreatedBy(creature);
        results.add(result);

        activeEffects = new Array<Effect>();
        activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 3f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 0.1f, 1f, 0f));

        direction = new Vector2(3,3);

        result = new ActivityWithEffect(
                screen,
                x + creature.direction.x* PalidorGame.TILE_SIZE,
                y + creature.direction.y* PalidorGame.TILE_SIZE,
                activeEffects,
                ActivityAreaType.SPRAY,
                direction,
                "firewall", false); 

        result.setCreatedBy(creature);
        results.add(result);

        activeEffects = new Array<Effect>();
        activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 3f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 0.1f, 1f, 0f));

        direction = new Vector2(3,-3);

        result = new ActivityWithEffect(
                screen,
                x + creature.direction.x* PalidorGame.TILE_SIZE,
                y + creature.direction.y* PalidorGame.TILE_SIZE,
                activeEffects,
                ActivityAreaType.SPRAY,
                direction,
                "firewall", false); 

        result.setCreatedBy(creature);
        results.add(result);

        activeEffects = new Array<Effect>();
        activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_RIGHT, 0.1f, 3f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 0.1f, 1f, 0f));

        direction = new Vector2(0,3);

        result = new ActivityWithEffect(
                screen,
                x + creature.direction.x* PalidorGame.TILE_SIZE,
                y + creature.direction.y* PalidorGame.TILE_SIZE,
                activeEffects,
                ActivityAreaType.SPRAY,
                direction,
                "firewall", false); 

        result.setCreatedBy(creature);
        results.add(result);

        activeEffects = new Array<Effect>();
        activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 3f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 0.1f, 1f, 0f));

        direction = new Vector2(0,-3 );

        result = new ActivityWithEffect(
                screen,
                x + creature.direction.x* PalidorGame.TILE_SIZE,
                y + creature.direction.y* PalidorGame.TILE_SIZE,
                activeEffects,
                ActivityAreaType.SPRAY,
                direction,
                "firewall", false); 

        result.setCreatedBy(creature);
        results.add(result);

        activeEffects = new Array<Effect>();
        activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 3f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 0.1f, 1f, 0f));

        direction = new Vector2(-3, 3);

        result = new ActivityWithEffect(
                screen,
                x + creature.direction.x* PalidorGame.TILE_SIZE,
                y + creature.direction.y* PalidorGame.TILE_SIZE,
                activeEffects,
                ActivityAreaType.SPRAY,
                direction,
                "firewall", false); 

        result.setCreatedBy(creature);
        results.add(result);

        activeEffects = new Array<Effect>();
        activeEffects.add(new Effect(EffectID.STUNED, 0.1f, 0.1f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 3f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 0.1f, 1f, 0f));

        direction = new Vector2(-3, 0);

        result = new ActivityWithEffect(
                screen,
                x + creature.direction.x* PalidorGame.TILE_SIZE,
                y + creature.direction.y* PalidorGame.TILE_SIZE,
                activeEffects,
                ActivityAreaType.SPRAY,
                direction,
                "firewall", false); 

        result.setCreatedBy(creature);
        results.add(result);

        activeEffects = new Array<Effect>();
        activeEffects.add(new Effect(EffectID.STUNED, 0.2f, 0.1f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_LEFT, 0.1f, 3f, 0f));
        activeEffects.add(new Effect(EffectID.MOVE_UP, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.CRUSH_DAMAGE, 0.1f, 1f, 0f));
        activeEffects.add(new Effect(EffectID.FIRE_DAMAGE, 0.1f, 1f, 0f));

        direction = new Vector2(-3, -3 );

        result = new ActivityWithEffect(
                screen,
                x + creature.direction.x* PalidorGame.TILE_SIZE,
                y + creature.direction.y* PalidorGame.TILE_SIZE,
                activeEffects,
                ActivityAreaType.SPRAY,
                direction,
                "firewall", false); 

        result.setCreatedBy(creature);
        results.add(result);


        screen.activitiesToCreate.addAll(results);
    }


    public static Animation getAnimation(GameScreen screen, AbilityID abilityToCast, String spritesheetRegion) {
        switch(abilityToCast) {
            //case LONGBOW_SHOT:
            //return screen.animationHelper.getAnimationByID(spritesheetRegion, 0.3f, 4, 5);
            //case PUNCH:
            //return screen.animationHelper.getAnimationByID(spritesheetRegion, 0.3f, 2, 3);
            //case FIREWALL:
            //return screen.animationHelper.getAnimationByID(spritesheetRegion, 0.3f, 6, 7);
            //case ANIMAL_PUNCH:
            //return screen.animationHelper.getAnimationByID(spritesheetRegion, 0.3f, 1, 2, 3);
            case TURREL_PIU:
                return screen.animationHelper.getAnimationByID(spritesheetRegion, abilityToCast.getCastTime()/4, 0, 0, 2, 0);
            case APPEAR:
                return screen.animationHelper.getAnimationByID("appearing", abilityToCast.getCastTime()/3, 0, 1, 2);
            default:
                //return screen.animationHelper.getAnimationByID(spritesheetRegion, 0.1f, 0, 1);
                return screen.animationHelper.getAnimationByID(spritesheetRegion, abilityToCast.getCastTime()/4, 4, 4, 4, 5);
        }
    }
}
