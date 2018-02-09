package com.mygdx.game.ai;

import com.mygdx.game.ai.BehaviourPatternStep;

import static com.mygdx.game.ai.BehaviourPatternStep.*;

/**
 * Created by odiachuk on 2/9/18.
 */
public enum BehaviourPattern {

    ANIMAL_ROLF("animal_rolf", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE),
    ANIMAL_TORDUR("animal_tordur", ANIMAL_DASH, ANIMAL_PUNCH, RAGE),
    ANIMAL_BOLFIR("animal_bolfir", ANIMAL_ACID, ANIMAL_PUNCH, RAGE),
    ANIMAL_RAGER("animal_rager", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE),

    ENEMY_1("enemy_1", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE),
    ENEMY_2("enamy_2", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE),
    ENEMY_3("enemy_3", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE),

    LOCAL_1("animal_rolf", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE),
    LOCAL_2("animal_rolf", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE),
    LOCAL_3("animal_rolf", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE),

    DEMON_1("demon_1", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE),
    DEMON_2("demon_2", ANIMAL_PUNCH, ANIMAL_PUNCH, RAGE);

    String id;
    BehaviourPatternStep steps[];

    BehaviourPattern(String id, BehaviourPatternStep... steps) {
        this.id = id;
        this.steps = steps;
    }


    public BehaviourPatternStep[] getSteps() {
        return steps;
    }
}
