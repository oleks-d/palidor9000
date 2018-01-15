package com.mygdx.game3.stuctures.descriptions;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game3.enums.EquipmentType;
import com.mygdx.game3.stuctures.Effect;

/**
 * Created by odiachuk on 12/21/17.
 */
public class ItemDescription {
    public String name;
    public String description;
    public String image;
    public int value;
    public EquipmentType type;
    public boolean usable;
    public Array<Effect> effects;
    public String id;

    public ItemDescription(String id, String name, String description, String image, int value) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.value = value;
        this.type = EquipmentType.NONE;
        this.usable = false;
        this.effects = new Array<Effect>();
    }


    public ItemDescription(String id, String name, String description, String image, int value, EquipmentType type, boolean usable, String effects) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.value = value;
        this.type = type;
        this.usable = usable;
        this.effects = new Array<Effect>();
        if(!effects.equals("")) {
            for(String curEffect : effects.split(",")){
                this.effects.add(new Effect(curEffect));
            }
        }
    }
}
