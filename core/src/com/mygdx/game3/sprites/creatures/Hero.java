package com.mygdx.game3.sprites.creatures;

import com.mygdx.game3.screens.GameScreen;
import com.mygdx.game3.enums.AbilityID;

/**
 * Created by odiachuk on 12/17/17.
 */
public class Hero extends Creature {

    // TODO make abilities ajustable
    public AbilityID ABILITY0 = AbilityID.PUNCH;;
    public AbilityID ABILITY1 = AbilityID.FIREBALL;
    public AbilityID ABILITY2 = AbilityID.ARROW;
    public AbilityID ABILITY3 = AbilityID.ICEBALL;

    public Hero (GameScreen screen, float x, float y){
        super(screen, x, y, screen.levelmanager.CREATURE_DESCRIPTIONS.get("hero"));
    }
}
