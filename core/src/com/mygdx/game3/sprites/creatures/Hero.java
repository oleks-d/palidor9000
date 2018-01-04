package com.mygdx.game3.sprites.creatures;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game3.enums.AbilityType;
import com.mygdx.game3.screens.GameScreen;
import com.mygdx.game3.enums.AbilityID;

/**
 * Created by odiachuk on 12/17/17.
 */
public class Hero extends Creature {

    // OLd approach
//    public AbilityID ABILITY0 = AbilityID.PUNCH;;
//    public AbilityID ABILITY1 = AbilityID.FIREBALL;
//    public AbilityID ABILITY2 = AbilityID.ARROW;
//    public AbilityID ABILITY3 = AbilityID.ICEBALL;

    public Array<AbilityID> selectedAtackAbilities;
    public Array<AbilityID> selectedDefenseAbilities;
    public Array<AbilityID> selectedHelpAbilities;

    public Hero (GameScreen screen, float x, float y){
        super(screen, x, y, screen.levelmanager.CREATURE_DESCRIPTIONS.get("hero"));

        selectedAtackAbilities = new Array<AbilityID>();
        selectedAtackAbilities.add(AbilityID.PUNCH);

        selectedDefenseAbilities = new Array<AbilityID>();
        selectedDefenseAbilities.add(AbilityID.STOP_CAST);

        selectedHelpAbilities = new Array<AbilityID>();
        selectedHelpAbilities.add(AbilityID.HASTE);
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
}
