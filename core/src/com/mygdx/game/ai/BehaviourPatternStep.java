package com.mygdx.game.ai;

import com.mygdx.game.enums.AbilityID;

/**
 * Created by odiachuk on 2/9/18.
 */
public enum BehaviourPatternStep {
    ANIMAL_PUNCH(1, "" , AbilityID.ANIMAL_PUNCH),
    ANIMAL_BOOM(2, "" , AbilityID.APPERPUNCH),
    ANIMAL_DASH(3, "D" , AbilityID.ANIMAL_DASH),
    ANIMAL_SPIKE(4, "D" , AbilityID.SPIKE_SHOT),
    ANIMAL_ACID(5, "D" , AbilityID.ACID_SHOT),
    RAGE(6, "HP" , AbilityID.RAGE),
    HEAL(7, "HP" , AbilityID.HEAL),
    BARSKIN(8, "HP" , AbilityID.BARSKIN),
    SPIKE_EXPLOSION(9, "HP" , AbilityID.FIREBALL),

    SWING(10, "" , AbilityID.SWORD_SWING),
    SMASH(11, "" , AbilityID.SWORD_SMASH),

    SWING_HUMMER(12, "" , AbilityID.HUMMER_SWING),
    SMASH_HUMMER(13, "" , AbilityID.HUMMER_SMASH),

    FIREWALL(14, "D" , AbilityID.FIREWALL),
    FIREBALL(15, "D" , AbilityID.FIREBALL),

    BLOCK(16, "" , AbilityID.FIREWALL),
    USE_POTION(17, "" , AbilityID.FIREWALL),

    LONGBOW_SHOT(18, "D" , AbilityID.LONGBOW_SHOT),
    SLING_SHOT(19, "D" , AbilityID.SLING_SHOT);

    int ID;

    BehaviourPatternStep(int ID, String condition, AbilityID ability) {
        this.ID = ID;
        this.condition = condition;
        this.ability = ability;
    }

    String condition;
    AbilityID ability;


    public String getCondition() {
        return condition;
    }
}
