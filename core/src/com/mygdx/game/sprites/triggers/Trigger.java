package com.mygdx.game.sprites.triggers;

/**
 * Created by odiachuk on 12/26/17.
 */
public class Trigger {

    private String type;
    private String value;
    private String key;

    public Trigger(String type,  String key, String value, String description) {
        this.type = type;
        this.value = value;
        this.key = key;
        this.description = description;
    }

    private String description;



    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
