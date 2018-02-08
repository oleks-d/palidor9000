package com.mygdx.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.enums.EffectID;
import com.mygdx.game.sprites.creatures.Creature;


public class EffectsHandler {
    public static void applyEffect(Creature creature, EffectID id, float magnitude){
        switch (id){
            case POISON:
                //int damageValue = Math.round(magnitude);
                creature.doDamage( Math.round(magnitude),EffectID.POISON);
                break;
            case CUT_DAMAGE:
                if(creature.getEffect(EffectID.IMMUNE_CUT_DAMAGE) == null)
                    if(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_CUT_DAMAGE) == null) {  // if has no shield
                        int damageValue = creature.getEffect(EffectID.WEEKNESS_CUT_DAMAGE) == null ?
                            (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_CUT_DAMAGE)))
                            :
                            (Math.round(magnitude - creature.getEffectsSum(EffectID.MINUS_CUT_DAMAGE))) * 2  /// multiply if has weekness
                            ;

                            if(damageValue > 0) {// if was not stoped by armor/buffs
                                creature.doDamage(damageValue,EffectID.CUT_DAMAGE);
                                //creature.addStatusMessage(String.valueOf(damageValue), Fonts.BAD);
                            }
                    }else { // if had shield remove shield
                        creature.addStatusMessage("Has shield to " + EffectID.CUT_DAMAGE.toString(), Fonts.IMPORTANT);
                        creature.removeEffectByID(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_CUT_DAMAGE).id);
                    }
                else
                    creature.addStatusMessage("Immune to " + EffectID.CUT_DAMAGE.toString(), Fonts.IMPORTANT);
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

                        if(damageValue > 0) {// if was not stoped by armor/buffs
                            creature.doDamage(damageValue,EffectID.CRUSH_DAMAGE);
                            //creature.addStatusMessage(String.valueOf(damageValue), Fonts.BAD);
                        }
                    }else { // if had shield remove shield
                        creature.addStatusMessage("Has shiled to " + EffectID.CRUSH_DAMAGE.toString(), Fonts.IMPORTANT);
                        creature.removeEffectByID(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_CRUSH_DAMAGE).id);
                    }
                else
                    creature.addStatusMessage("Immune to " + EffectID.CRUSH_DAMAGE.toString(), Fonts.IMPORTANT);
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

                        if(damageValue > 0) { // if was not stoped by armor/buffs
                            creature.doDamage(damageValue,EffectID.FIRE_DAMAGE);
                            //creature.addStatusMessage(String.valueOf(damageValue), Fonts.BAD);
                        }
                    }else { // if had shield remove shield
                        creature.addStatusMessage("Has shield to " + EffectID.FIRE_DAMAGE.toString(), Fonts.IMPORTANT);
                        creature.removeEffectByID(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_FIRE_DAMAGE).id);
                    }
                else
                    creature.addStatusMessage("Immune to " + EffectID.FIRE_DAMAGE.toString(), Fonts.IMPORTANT);
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

                        if(damageValue > 0) {// if was not stoped by armor/buffs
                            creature.doDamage(damageValue,EffectID.ICE_DAMAGE);
                            //creature.addStatusMessage(String.valueOf(damageValue), Fonts.BAD);
                        }
                    }else { // if had shield remove shield
                        creature.addStatusMessage("Has shield to " + EffectID.ICE_DAMAGE.toString(), Fonts.IMPORTANT);
                        creature.removeEffectByID(creature.getEffect(EffectID.HAS_SHIELD_AGAINST_ICE_DAMAGE).id);
                    }
                else
                    creature.addStatusMessage("Immune to " + EffectID.ICE_DAMAGE.toString(), Fonts.IMPORTANT);
                // creature is immune to this type of damage
                break;


            case SLOW:
                creature.addStatusMessage("Slow", Fonts.BAD);
                creature.stats.speed.current= creature.stats.speed.current - (int) Math.abs(magnitude);
                break;
            case FAST:
                creature.addStatusMessage("Fast", Fonts.GOOD);
                creature.stats.speed.current  = creature.stats.speed.current + (int) Math.abs(magnitude); //TODO fix
                break;

            case STUNED:
                creature.addStatusMessage("Stuned", Fonts.BAD);
                creature.setStun(true);
                break;

            case MOVE_LEFT:
                creature.getBody().applyLinearImpulse(new Vector2(-magnitude,0), creature.getBody().getWorldCenter(), true);
                break;
            case MOVE_RIGHT:
                creature.getBody().applyLinearImpulse(new Vector2(magnitude,0), creature.getBody().getWorldCenter(), true);
                break;
            case INVISIBLE:
                creature.setInvisible(true);

//            default:
//                Gdx.app.log("No such effect or no processing needed", id.toString());
        }
    }

    public static void resetEffect(Creature creature, EffectID id, float magnitude){
        switch (id){
            case SLOW:
                creature.addStatusMessage("Fast againg", Fonts.IMPORTANT);
                creature.stats.speed.current = creature.stats.speed.base;
                break;
            case FAST:
                creature.addStatusMessage("Slow againg", Fonts.IMPORTANT);
                creature.stats.speed.current = creature.stats.speed.base;
                break;

            case STUNED:
                creature.addStatusMessage("Active (not in stun)", Fonts.IMPORTANT);
                creature.setStun(false);
                break;
            case COVERED_BY_FIRE_SHIELD:
            case COVERED_BY_SHIELD:
                creature.removeShield();
                break;
            case INVISIBLE:
                creature.setInvisible(false);
                break;
            case THROW_FROM_INVENTORY:
                creature.throwFromInventory();
                break;
            default:
                // No remove process needed
                //
        }
    }
}
