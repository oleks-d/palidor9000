package com.mygdx.game3.stuctures;

import com.mygdx.game3.enums.EffectID;

/**
 * Created by odiachuk on 12/20/17.
 */
public class Effect {

    public EffectID id; // type of effect
    public double duration; // if 0 - constant - Forever
    public float magnitude; // power of effect

    public float dotDuration ; // time to refresh effect for DoT 0,5 for example

    //calculated during Effect aplication
    public double refreshTime ; // time to refresh effect for DoT next time
    public double removeTime;

    public Effect(EffectID id, double duration, float magnitude, float dotDuration) {
        this.id = id;
        this.duration = duration;
        this.magnitude = magnitude;
        this.dotDuration = dotDuration;

        refreshTime = 0;
        removeTime = 0;
    }

    public Effect(EffectID id, double duration, float magnitude, float dotDuration, double refreshTime, double removeTime ) {
        this.id = id;
        this.duration = duration;
        this.magnitude = magnitude;
        this.dotDuration = dotDuration;

        this.refreshTime = refreshTime;
        this.removeTime = removeTime;
    }

    public Effect(String curEffect) {
        String[] fields = curEffect.split("\\s");
        this.id = EffectID.valueOf(fields[0]);
        this.duration = Double.valueOf(fields[1]);
        this.magnitude = Float.valueOf(fields[2]);
        this.dotDuration = Float.valueOf(fields[3]);
    }
}
