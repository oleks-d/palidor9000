package com.mygdx.game3.tools;

import com.mygdx.game3.enums.EffectID;
import com.mygdx.game3.sprites.creatures.Creature;

public class EffectsHandler {

    public static void applyEffectUseIt(Creature creature, EffectID id, float magnitude){
        switch (id){
            case DAMAGE:
                creature.stats.health.current = creature.stats.health.current - Math.round(magnitude);
                break;
            case BLEEDING:
                creature.stats.health.current = creature.stats.health.current - Math.round(magnitude);
                break;
            case SLOW:
                creature.stats.speed.current--;
                break;
            case FAST:
                creature.stats.speed.current++;
                break;
            case IN_FIRE:
                creature.stats.speed.current = creature.stats.speed.current + Math.round(magnitude);
                break;
            case IN_ICE:
                creature.stats.speed.current = creature.stats.speed.current - Math.round(magnitude);
                break;
            case STUNED:
                creature.stuned = true;
                break;
        }
    }

    public static void resetEffect(Creature creature, EffectID id, float magnitude){
        switch (id){
            case DAMAGE:
                //no reset
                break;
            case BLEEDING:
                // no reset
                break;
            case SLOW:
                creature.stats.speed.current++;
                break;
            case FAST:
                creature.stats.speed.current--;
                break;
            case IN_FIRE:
                creature.stats.speed.current = creature.stats.speed.current - Math.round(magnitude);
                break;
            case IN_ICE:
                creature.stats.speed.current = creature.stats.speed.current + Math.round(magnitude);
                break;
            case STUNED:
                creature.stuned = false;
                break;
        }
    }
}
