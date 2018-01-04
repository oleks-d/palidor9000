package com.mygdx.game3.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game3.enums.EffectID;
import com.mygdx.game3.sprites.creatures.Creature;

import static com.mygdx.game3.enums.EffectID.*;

public class EffectsHandler {
    public static void applyEffectUseIt(Creature creature, EffectID id, float magnitude){
        switch (id){
            case CUT_DAMAGE:
                if(creature.getEffect(EffectID.IMMUNE_CUT_DAMAGE) == null)
                    if(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_CUT_DAMAGE) == null) {  // if has no shield
                        int damageValue = creature.getEffect(EffectID.WEEKNESS_CUT_DAMAGE) == null ?
                            (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_CUT_DAMAGE)))
                            :
                            (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_CUT_DAMAGE))) * 2  /// multiply if has weekness
                            ;

                            if(damageValue > 0) // if was not stoped by armor/buffs
                                creature.stats.health.current = creature.stats.health.current - damageValue;
                    }else { // if had shield remove shield
                        Gdx.app.log(CUT_DAMAGE.toString(),"Has shield");
                        creature.removeEffectByID(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_CUT_DAMAGE).id);
                    }
                else
                    Gdx.app.log(CUT_DAMAGE.toString(),"Immune");
                    // creature is immune to this type of damage
                break;
            case CRUSH_DAMAGE:
                if(creature.getEffect(EffectID.IMMUNE_CRUSH_DAMAGE) == null)
                    if(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_CRUSH_DAMAGE) == null) {  // if has no shield
                        int damageValue = creature.getEffect(EffectID.WEEKNESS_CRUSH_DAMAGE) == null ?
                                (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_CRUSH_DAMAGE)))
                                :
                                (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_CRUSH_DAMAGE))) * 2  /// multiply if has weekness
                                ;

                        if(damageValue > 0) // if was not stoped by armor/buffs
                            creature.stats.health.current = creature.stats.health.current - damageValue;
                    }else { // if had shield remove shield
                        Gdx.app.log(CRUSH_DAMAGE.toString(),"Has shield");
                        creature.removeEffectByID(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_CRUSH_DAMAGE).id);
                    }
                else
                    Gdx.app.log(CRUSH_DAMAGE.toString(),"Immune");
                // creature is immune to this type of damage
                break;
            case FIRE_DAMAGE:
                if(creature.getEffect(EffectID.IMMUNE_FIRE_DAMAGE) == null)
                    if(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_FIRE_DAMAGE) == null) {  // if has no shield
                        int damageValue = creature.getEffect(EffectID.WEEKNESS_FIRE_DAMAGE) == null ?
                                (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_FIRE_DAMAGE)))
                                :
                                (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_FIRE_DAMAGE))) * 2  /// multiply if has weekness
                                ;

                        if(damageValue > 0) // if was not stoped by armor/buffs
                            creature.stats.health.current = creature.stats.health.current - damageValue;
                    }else { // if had shield remove shield
                        Gdx.app.log(FIRE_DAMAGE.toString(),"Has shield");
                        creature.removeEffectByID(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_FIRE_DAMAGE).id);
                    }
                else
                    Gdx.app.log(FIRE_DAMAGE.toString(),"Immune");
                // creature is immune to this type of damage
                break;
            case ICE_DAMAGE:
                if(creature.getEffect(EffectID.IMMUNE_ICE_DAMAGE) == null)
                    if(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_ICE_DAMAGE) == null) {  // if has no shield
                        int damageValue = creature.getEffect(EffectID.WEEKNESS_ICE_DAMAGE) == null ?
                                (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_ICE_DAMAGE)))
                                :
                                (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_ICE_DAMAGE))) * 2  /// multiply if has weekness
                                ;

                        if(damageValue > 0) // if was not stoped by armor/buffs
                            creature.stats.health.current = creature.stats.health.current - damageValue;
                    }else { // if had shield remove shield
                        Gdx.app.log(ICE_DAMAGE.toString(),"Has shield");
                        creature.removeEffectByID(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_ICE_DAMAGE).id);
                    }
                else
                    Gdx.app.log(ICE_DAMAGE.toString(),"Immune");
                // creature is immune to this type of damage
                break;


            case SLOW:
                creature.stats.speed.current--;
                break;
            case FAST:
                creature.stats.speed.current  = (int) Math.abs(magnitude); //TODO fix
                break;

            case STUNED:
                creature.setStun(true);
                break;

            case MOVE_LEFT:
                creature.getBody().applyLinearImpulse(new Vector2(-magnitude,0), creature.getBody().getWorldCenter(), true);
                break;
            case MOVE_RIGHT:
                creature.getBody().applyLinearImpulse(new Vector2(magnitude,0), creature.getBody().getWorldCenter(), true);
                break;

            default:
                Gdx.app.log("No such effect", id.toString());
        }
    }

    public static void resetEffect(Creature creature, EffectID id, float magnitude){
        switch (id){
            case SLOW:
                creature.stats.speed.current++;
                break;
            case FAST:
                creature.stats.speed.current--;
                break;

            case STUNED:
                creature.setStun(false);
                break;

            default:
                // No remove process needed
                //
        }
    }
}
