package com.mygdx.game.tools;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.AbilityType;
import com.mygdx.game.enums.EffectID;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.stuctures.Effect;
import com.mygdx.game.stuctures.Skill;

/**
 * Created by odiachuk on 1/24/18.
 */
public class ConditionProcessor {
    public static boolean conditionSatisfied(Hero hero, String condition) {

        boolean result = true;
        if (condition != null && !"".equals(condition)) {

        for(String subcondition : condition.split(";")) {
            String[] parts = subcondition.split(":");

                String conditionType = parts[0];
                String conditionKey = parts[1];
                String conditionValue = "";
                if (parts.length > 2)
                    conditionValue = parts[2];

                switch (conditionType) {
                    case "T":  //check trigger
                        if (hero.getGlobalState(conditionKey) != null && hero.getGlobalState(conditionKey).equals(conditionValue))
                            result = result && true;
                        else
                            result = result && false;
                        break;
                    case "I": //check item
                        if (hero.checkInInventory(conditionKey))
                            result = result && true;
                        else
                            result = result && false;
                        break;
                    case "A": //check ability
                        if (hero.getAbilities().contains(AbilityID.valueOf(conditionKey), true))
                            result = result && true;
                        else
                            result = result && false;
                        break;
                    case "S": //check ability
                        if (hero.getSkills().contains(Skill.valueOf(conditionKey), true))
                            result = result && true;
                        else
                            result = result && false;
                        break;
                    case "E": //check ability
                        if (hero.checkEffect(EffectID.valueOf(conditionKey)))
                            result = result && true;
                        else
                            result = result && false;
                        break;
                    //negative triggers
                    case "NT":  //check trigger
                        if (!(hero.getGlobalState(conditionKey) != null && hero.getGlobalState(conditionKey).equals(conditionValue)))
                            result = result && true;
                        else
                            result = result && false;
                        break;
                    case "NI": //check item
                        if (!hero.checkInInventory(conditionKey))
                            result = result && true;
                        else
                            result = result && false;
                        break;
                    case "NA": //check ability
                        if (!hero.getAbilities().contains(AbilityID.valueOf(conditionKey), true))
                            result = result && true;
                        else
                            result = result && false;
                        break;
                    case "NE": //check ability
                        if (!hero.checkEffect(EffectID.valueOf(conditionKey)))
                            result = result && true;
                        else
                            result = result && false;
                        break;
                    case "NO":
                        return false;
                }
            }
        }
        return result;
    }


    public static void conditionProcess(Hero hero, String condition) {

        if (condition != null && !"".equals(condition)) {

            for(String subcondition : condition.split(";")) {
                String[] parts = subcondition.split(":");

                String conditionType = parts[0];
                String conditionKey = parts[1];
                String conditionValue = "";
                if (parts.length > 2)
                    conditionValue = parts[2];

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
                        Array<AbilityID> ability = new Array<AbilityID>();
                        ability.add(AbilityID.valueOf(conditionKey));
                        hero.addAbilities(ability);
                        break;
                    case "S": //add ability
                        hero.addSkill(Skill.valueOf(conditionKey));
                        break;
                    case "AE": //add effect
                        hero.applyEffect(new Effect(conditionKey));
                        break;
                    case "MR": // money REMOVE
                        hero.removeMoney(conditionKey);
                        break;
                    case "MA": // money ADD
                        hero.addMoney(conditionKey);
                        break;
                    case "EX": // item ADD
                        hero.addExperience(conditionKey);
                        break;

                    case "FR": // make a friend
                        hero.getNeighbor().setOrganization(0);
                        break;
                    case "UNFR": // make an enemy
                        hero.getNeighbor().setOrganization(2);
                        break;

                    case "SS": // SHAKE Earth
                        hero.screen.shake(Double.parseDouble(conditionKey));
                        break;

                    case "SC": // creature Creature
                        hero.screen.creaturesToCreate.add(subcondition);
                        break;

                    case "RC": // creature Creature (from list of unavailable creatures)
                        hero.screen.creaturesToCreate.add(conditionKey);
                        break;

                    case "RO": // creature Object (from list of unavailable objects)
                        hero.screen.objectsToCreate.add(conditionKey);
                        break;

                    case "KO": // kill Object
                        hero.screen.objectsToDie.add(conditionKey);
                        break;

                    case "KC": // kill Creature
                        hero.screen.creaturesToDie.add(conditionKey);
                        break;

                    case "KN": // kill Creature you are interacting with
                        hero.screen.creaturesToDie.add(String.valueOf(hero.getNeighbor().getID()));
                        break;

                    case "SI": // create  Item
                        hero.screen.itemsToCreate.add(subcondition);
                        break;

                    case "SP": //set creature program
                        hero.screen.levelmanager.setProgramTo(conditionKey, conditionValue);
                        break;

                    case "WIN": // creature activate Creature
                        hero.screen.gameWin(conditionKey);
                        break;
                }
            }
        }
    }

    public static void conditionProcess(Creature creature, String condition) {
        if (condition != null && !"".equals(condition)) {
        for(String subcondition : condition.split(";")) {
            String[] parts = subcondition.split(":");

                String conditionType = parts[0];
                String conditionKey = parts[1];
                String conditionValue = "";
                if (parts.length > 2)
                    conditionValue = parts[2];

                switch (conditionType) {
                    case "CIR": // item REMOVE
                        creature.addItemByID(conditionKey);
                        creature.throwFromInventory(conditionKey);
                        break;
                    case "CAE": //add effect
                        creature.applyEffect(new Effect(conditionKey));
                        break;
                    default:
                        conditionProcess(creature.screen.hero,subcondition);

                }
            }
        }
    }
}
