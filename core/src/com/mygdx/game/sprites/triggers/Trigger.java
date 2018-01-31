package com.mygdx.game.sprites.triggers;

/**
 * Created by odiachuk on 12/26/17.
 */
public class Trigger {

    public String getCondition() {
        return condition;
    }

    private final String condition;
    private String type;
    private String value;
    private String key;
    private String process;

    public Trigger(String type,  String key, String value, String description, String condition, String process) {
        this.type = type;
        this.value = value;
        this.key = key;
        this.description = description;
        this.condition = condition;
        this.process = process;
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

    public String getProcess() {
        return process;
    }
}
