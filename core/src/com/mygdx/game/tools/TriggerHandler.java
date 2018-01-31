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
                    if((ConditionProcessor.conditionSatisfied(creature.screen.hero, trigger.getCondition()))) {
                        creature.screen.levelmanager.removeDeadBodies();
                        creature.screen.levelmanager.saveLevel(creature.screen.hero.currentLevel, creature.screen.hero.name);
                        creature.screen.levelmanager.loadNextLevel(trigger.getValue(), creature.screen.hero.name);
                        creature.screen.levelmanager.saveHero(creature.screen.hero);
                        creature.screen.hero.addStatusMessage(creature.screen.hero.currentLevel, Fonts.GOOD);
                    } else
                        creature.screen.hero.addStatusMessage("You cannot leave this area so far", Fonts.INFO);
                }
            break;
            case "story": // story points
                if(creature.equals(creature.screen.hero)) {
                    if ((ConditionProcessor.conditionSatisfied(creature.screen.hero, trigger.getCondition()))){
                        creature.screen.hero.changeGlobalState(trigger.getKey(), trigger.getValue());
                        ConditionProcessor.conditionProcess(creature.screen.hero, trigger.getProcess());
                        creature.screen.dialogPanel.addMessage(trigger.getDescription(), "Story", creature.screen.animationHelper.getTextureRegionByIDAndIndex("book"));
                        creature.screen.showDialog();
                    }
                }
                break;
            case "step": // story points
                if(creature.equals(creature.screen.hero)) {
                    if ((ConditionProcessor.conditionSatisfied(creature.screen.hero, trigger.getCondition()))){
                        creature.screen.hero.changeGlobalState(trigger.getKey(), trigger.getValue());
                        ConditionProcessor.conditionProcess(creature.screen.hero, trigger.getProcess());
                        creature.addStatusMessage(trigger.getDescription(),Fonts.IMPORTANT);
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
            case "ancient_label": //Ancient label
                if(creature.equals(creature.screen.hero)) {
                    if(creature.screen.hero.skills.contains(Skill.INTELLIGENCE3, true))
                        creature.addStatusMessage(trigger.getDescription(), Fonts.IMPORTANT);
                    else
                        creature.addStatusMessage("You cannot read Ancient text", Fonts.IMPORTANT);
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
