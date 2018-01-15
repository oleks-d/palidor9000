package com.mygdx.game3.sprites.creatures;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game3.screens.GameScreen;
import com.mygdx.game3.enums.AbilityID;
import com.mygdx.game3.stuctures.descriptions.CreatureDescription;

import java.util.HashMap;

/**
 * Created by odiachuk on 12/17/17.
 */
public class Hero extends Creature {

    public Array<AbilityID> selectedAtackAbilities;
    public Array<AbilityID> selectedDefenseAbilities;
    public Array<AbilityID> selectedHelpAbilities;

    HashMap<String,String> GLOBAL_STATES;

    public String currentLevel;
    public String previousLevel;

    public Hero (GameScreen screen, CreatureDescription heroDescription, String currentLevel, String previousLevel, HashMap<String,String> globalStates, Array<AbilityID> selectedAtackAbilities,Array<AbilityID> selectedDefenseAbilities, Array<AbilityID> selectedHelpAbilities){
        super(screen, heroDescription);

        this.canPickUpObjects = true;

        this.selectedAtackAbilities = selectedAtackAbilities;
        this.selectedDefenseAbilities =selectedDefenseAbilities;
        this.selectedHelpAbilities = selectedHelpAbilities;

        this.GLOBAL_STATES = globalStates;

        this.currentLevel = currentLevel;
        this.previousLevel = previousLevel;
    }

    public Hero (GameScreen screen, float x, float y){
        super(screen, x, y, screen.levelmanager.CREATURE_DESCRIPTIONS.get("hero"));

        selectedAtackAbilities = new Array<AbilityID>();
        selectedAtackAbilities.add(AbilityID.PUNCH);

        selectedDefenseAbilities = new Array<AbilityID>();
        selectedDefenseAbilities.add(AbilityID.STOP_CAST);

        selectedHelpAbilities = new Array<AbilityID>();
        selectedHelpAbilities.add(AbilityID.HASTE);

        GLOBAL_STATES = new HashMap<String,String>();
    }

    public void deselectHelpAbility(int index) {
        selectedHelpAbilities.removeIndex(index);
    }

    public void deselectDefenseAbility(int index) {
        selectedDefenseAbilities.removeIndex(index);
    }

    public void deselectAtackAbility(int index) {
        selectedAtackAbilities.removeIndex(index);
    }

    public void selectAbility(AbilityID currentAbility) {
        switch (currentAbility.getType()){
            case BUFF:
                if(!selectedHelpAbilities.contains(currentAbility, true))
                    selectedHelpAbilities.add(currentAbility);
                break;
            case LONG_RANGE_ATACK:
            case CLOSE_RANGE_ATACK:
                if(!selectedAtackAbilities.contains(currentAbility, true))
                selectedAtackAbilities.add(currentAbility);
                break;
            case LONG_RANGE_DEFENSE:
            case CLOSE_RANGE_DEFENSE:
                if(!selectedDefenseAbilities.contains(currentAbility, true))
                selectedDefenseAbilities.add(currentAbility);
                break;
        }
    }

    public void changeGlobalState(String key, String value){
        GLOBAL_STATES.put(key,value);
    }

    public String getGlobalState(String key){
        return GLOBAL_STATES.get(key);
    }

    public HashMap<String, String> getGlobalStates(){
        return GLOBAL_STATES;
    }
}
