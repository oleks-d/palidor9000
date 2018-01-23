package com.mygdx.game.tools;

import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.sprites.triggers.Trigger;
import com.mygdx.game.stuctures.Skill;


/**
 * Created by odiachuk on 12/26/17.
 */
public class TriggerHandler {

    public static void runProcess(Creature creature, Trigger trigger) {
        //TODO describe triggers
        switch(trigger.getType()){
            case "route":
                if(creature.equals(creature.screen.hero)) {
                    creature.screen.levelmanager.removeDeadBodies();
                    creature.screen.levelmanager.saveLevel(creature.screen.hero.currentLevel, creature.screen.hero.name);
                    creature.screen.levelmanager.loadNextLevel(trigger.getValue(), creature.screen.hero.name);
                    creature.screen.levelmanager.saveHero(creature.screen.hero);
                    creature.screen.infoPanel.update();
                }
            break;
            case "story":
                if(creature.equals(creature.screen.hero)) {
                    creature.screen.hero.changeGlobalState(trigger.getKey(),trigger.getValue());
                    creature.screen.hero.addStatusMessage(trigger.getDescription(), Fonts.IMPORTANT);
                }
                break;
            case "label":
                if(creature.equals(creature.screen.hero)) {
                    if(creature.screen.hero.skills.contains(Skill.INTELLIGENCE1, true))
                        creature.addStatusMessage(trigger.getDescription(), Fonts.IMPORTANT);
                    else
                        creature.addStatusMessage("You cannot read", Fonts.IMPORTANT);
                }
                break;
            case "acient_label":
                if(creature.equals(creature.screen.hero)) {
                    if(creature.screen.hero.skills.contains(Skill.INTELLIGENCE2, true))
                        creature.addStatusMessage(trigger.getDescription(), Fonts.IMPORTANT);
                    else
                        creature.addStatusMessage("You cannot read Acient text", Fonts.IMPORTANT);
                }
                break;
        }
    }
}
