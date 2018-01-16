package com.mygdx.game.sprites.creatures;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.stuctures.descriptions.CreatureDescription;
import com.mygdx.game.tools.Fonts;

import java.util.HashMap;

/**
 * Created by odiachuk on 12/17/17.
 */
public class Hero extends Creature {

    public Array<AbilityID> selectedAtackAbilities;
    public Array<AbilityID> selectedDefenseAbilities;

    HashMap<String,String> GLOBAL_STATES;

    public String currentLevel;
    public String previousLevel;

    public Hero (GameScreen screen, CreatureDescription heroDescription, String currentLevel, String previousLevel, HashMap<String,String> globalStates, Array<AbilityID> selectedAtackAbilities, Array<AbilityID> selectedDefenseAbilities, Array<AbilityID> selectedHelpAbilities){
        super(screen, heroDescription);

        this.canPickUpObjects = true;

        this.selectedAtackAbilities = selectedAtackAbilities;
        this.selectedDefenseAbilities =selectedDefenseAbilities;

        this.GLOBAL_STATES = globalStates;

        this.currentLevel = currentLevel;
        this.previousLevel = previousLevel;
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

    public void deselectDefenseAbility(int index) {
        selectedDefenseAbilities.removeIndex(index);
    }

    public void deselectAtackAbility(int index) {
        selectedAtackAbilities.removeIndex(index);
    }

    public void selectAbility(AbilityID currentAbility) {
                if(!selectedAtackAbilities.contains(currentAbility, true))
                    selectedAtackAbilities.add(currentAbility);
                else if(!selectedDefenseAbilities.contains(currentAbility, true))
                    selectedDefenseAbilities.add(currentAbility);
    }


    public void shout() {
        if(abilities.contains(com.mygdx.game.enums.AbilityID.SHOUT, false))
            useAbility(com.mygdx.game.enums.AbilityID.SHOUT);
        else
            statusbar.addMessage("You can not shout", existingTime + 1f, Fonts.INFO);
    }

    public void hide() {
        if(abilities.contains(AbilityID.MASK, false))
            setInvisible(true);
        else
            statusbar.addMessage("You can not hide", existingTime + 1f, Fonts.INFO);
        //useAbility(com.mygdx.game.enums.AbilityID.MASK);
    }

    public void fly() {
        if(abilities.contains(com.mygdx.game.enums.AbilityID.FLY, false))
            useAbility(com.mygdx.game.enums.AbilityID.FLY);
    }

    public void jumpBack() {
        if(abilities.contains(com.mygdx.game.enums.AbilityID.JUMP_BACK, false))
            useAbility(com.mygdx.game.enums.AbilityID.JUMP_BACK);
    }

    public void dash() {
        if(abilities.contains(AbilityID.DASH, false))
            useAbility(AbilityID.DASH);
    }


    public void attack(boolean isAbility1, boolean isPower) {
        if(isAbility1)
            if(isPower)
                useAbility(selectedAtackAbilities.get(1));
            else
                useAbility(selectedAtackAbilities.get(0));
        else
            if(isPower)
                useAbility(selectedDefenseAbilities.get(1));
            else
                useAbility(selectedDefenseAbilities.get(0));
    }
}
