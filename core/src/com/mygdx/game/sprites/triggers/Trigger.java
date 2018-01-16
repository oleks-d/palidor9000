package com.mygdx.game.sprites.triggers;

/**
 * Created by odiachuk on 12/26/17.
 */
public class Trigger {

    private String type;
    private String value;

    public Trigger(String name) {
        this.type = name.split("_")[0];
        this.value = name.split("_")[1];
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
