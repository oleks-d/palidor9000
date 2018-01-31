package com.mygdx.game.stuctures.descriptions;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.enums.EquipmentType;
import com.mygdx.game.stuctures.Effect;

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
    public String process;
    public String condition;


    public ItemDescription(String id, String name, String description, String image, int value, EquipmentType type, boolean usable, String effects, String process, String condition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.value = value;
        this.type = type;
        this.usable = usable;
        this.process = process;
        this.condition = condition;
        this.effects = new Array<Effect>();
        if(!effects.equals("")) {
            for(String curEffect : effects.split(",")){
                this.effects.add(new Effect(curEffect));
            }
        }
    }
}
