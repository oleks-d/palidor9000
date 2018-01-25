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
            case "route": // movement to other locations
                if(creature.equals(creature.screen.hero)) {
                    creature.screen.levelmanager.removeDeadBodies();
                    creature.screen.levelmanager.saveLevel(creature.screen.hero.currentLevel, creature.screen.hero.name);
                    creature.screen.levelmanager.loadNextLevel(trigger.getValue(), creature.screen.hero.name);
                    creature.screen.levelmanager.saveHero(creature.screen.hero);
                    creature.screen.dialogPanel.update();
                }
            break;
            case "story": // story points
                if(creature.equals(creature.screen.hero)) {
                    if ((ConditionProcessor.conditionSatisfied(creature.screen.hero, trigger.getCondition()))){
                        creature.screen.hero.changeGlobalState(trigger.getKey(), trigger.getValue());
                        creature.screen.dialogPanel.addMessage(trigger.getDescription(), "Story", creature.screen.animationHelper.getTextureRegionByIDAndIndex("book"));
                        creature.screen.showDialog();
                    }
                }
                break;
            case "label": // text labe
                if(creature.equals(creature.screen.hero)) {
                    if(creature.screen.hero.skills.contains(Skill.INTELLIGENCE1, true))
                        creature.addStatusMessage(trigger.getDescription(), Fonts.IMPORTANT);
                    else
                        creature.addStatusMessage("You cannot read", Fonts.IMPORTANT);
                }
                break;
            case "acient_label": //acient label
                if(creature.equals(creature.screen.hero)) {
                    if(creature.screen.hero.skills.contains(Skill.INTELLIGENCE3, true))
                        creature.addStatusMessage(trigger.getDescription(), Fonts.IMPORTANT);
                    else
                        creature.addStatusMessage("You cannot read Acient text", Fonts.IMPORTANT);
                }
                break;
            case "foreign_label": //foreign label
                if(creature.equals(creature.screen.hero)) {
                    if(creature.screen.hero.skills.contains(Skill.INTELLIGENCE2, true))
                        creature.addStatusMessage(trigger.getDescription(), Fonts.IMPORTANT);
                    else
                        creature.addStatusMessage("You cannot read Foreign text", Fonts.IMPORTANT);
                }
                break;
        }
    }
}
