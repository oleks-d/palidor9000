package com.mygdx.game.sprites.triggers;

import com.mygdx.game.sprites.creatures.Creature;


/**
 * Created by odiachuk on 12/26/17.
 */
public class TriggerHandler {

    public static void runProcess(Creature creature, Trigger trigger) {
        //TODO describe triggers
        switch(trigger.getType()){
            case "route":
                //TODO dialog
                if(creature.equals(creature.screen.hero)) {
                    creature.screen.levelmanager.loadNextLevel(trigger.getValue());
                    creature.screen.levelmanager.saveHero(creature.screen.hero);
                    creature.screen.infoPanel.update();
                }
            break;

        }
    }
}
