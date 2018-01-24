package com.mygdx.game.tools;

import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.EffectID;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.stuctures.Effect;

/**
 * Created by odiachuk on 1/24/18.
 */
public class ConditionProcessor {
    public static boolean conditionSatisfied(Hero hero, String condition) {

        boolean result = true;
        if (condition != null && !"".equals(condition)) {
            String conditionType = condition.split(":")[0];
            String conditionKey = condition.split(":")[1];
            String conditionValue = condition.split(":")[2];

            switch (conditionType) {
                case "T" :  //check trigger
                    if (hero.getGlobalState(conditionKey)!=null && hero.getGlobalState(conditionKey).equals(conditionValue))
                        return true;
                    else
                        return false;
                case "I" : //check item
                    if (hero.checkInInventory(conditionKey))
                        return true;
                    else
                        return false;
                case "A" : //check ability
                    if (hero.getAbilities().contains(AbilityID.valueOf(conditionKey),true))
                        return true;
                    else
                        return false;
                case "E" : //check ability
                    if (hero.checkEffect(EffectID.valueOf(conditionKey)))
                        return true;
                    else
                        return false;
            }
        }
        return result;
    }


    public static void conditionProcess(Hero hero, String condition) {

        if (condition != null && !"".equals(condition)) {
            String conditionType = condition.split(":")[0];
            String conditionKey = condition.split(":")[1];
            String conditionValue = condition.split(":")[2];

            switch (conditionType) {
                case "T":  //put trigger
                    hero.getGlobalStates().put(conditionKey, conditionValue);
                    break;
                case "IR": // item REMOVE
                    hero.removeItemByID(conditionKey);
                    break;
                case "IA": // item ADD
                    hero.addItemByID(conditionKey);
                    break;
                case "A": //add ability
                    hero.addAbilities(new AbilityID[]{AbilityID.valueOf(conditionKey)});
                    break;
                case "AE": //add effect
                    hero.applyEffect(new Effect(conditionKey));
                    break;

            }

        }
    }

    public static void conditionProcess(Creature creature, String condition) {

        if (condition != null && !"".equals(condition)) {
            String conditionType = condition.split(":")[0];
            String conditionKey = condition.split(":")[1];
            String conditionValue = condition.split(":")[2];

            switch (conditionType) {
                case "T" :  //put trigger
                    creature.screen.hero.getGlobalStates().put(conditionKey,conditionValue);
                    break;
                case "IR" : // item REMOVE
                    creature.throwFromInventory(conditionKey);
                    break;
                case "AE" : //add effect
                    creature.screen.hero.applyEffect(new Effect(conditionKey));
                    break;

            }
        }
    }
}
