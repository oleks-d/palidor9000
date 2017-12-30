package com.mygdx.game3.enums;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game3.tools.AnimationHelper;

/**
 * Created by odiachuk on 12/22/17.
 */

public enum AbilityID{
    FIREBALL("Firewall" , "Wall of fire", "fire_icon"),
    ICEBALL("Icewall", "Wall of ice", "ice_icon"),
    PUNCH("Punch",  "Melee strike", "sword"),
    ARROW("Arrow", "Shot", "bow"),
    NONE("Nope", "None", "icon_blank");

    String value;
    String description;
    String icon;

    AbilityID(String value, String description, String icon){
        this.value = value;
        this.description = description;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return value;
    }

    public static AbilityID getIDByName(String name){
        for (AbilityID current : values()){
            if(current.value.equals(name))
                return current;
        }
        return NONE;
    }

    public String getName() {
        return value;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }
}
