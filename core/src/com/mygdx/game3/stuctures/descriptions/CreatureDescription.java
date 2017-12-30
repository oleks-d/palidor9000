package com.mygdx.game3.stuctures.descriptions;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game3.enums.AbilityID;
import com.mygdx.game3.stuctures.Characteristics;
import com.mygdx.game3.stuctures.Effect;
import com.mygdx.game3.stuctures.Stat;

/**
 * Created by odiachuk on 12/21/17.
 */
public class CreatureDescription {
    public String name;
    public String description;
    public String region;
    public Characteristics stats;
    public Array<AbilityID> abilities;
    public Array<Effect> effects;
    public Array<String> inventory;
    public int organization;


    // short form
    public CreatureDescription(String name, String description, String region) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.stats = new Characteristics( new Stat(1,1), new Stat(1,1), new Stat(1,1));
        this.abilities = new Array<AbilityID>();
        this.effects = new Array<Effect>();
    }

    public CreatureDescription(String name, String description, String region, Characteristics characteristics, int organization, String abilities, String effects, String inventory) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.stats = characteristics;
        this.abilities = new Array<AbilityID>();
        this.effects = new Array<Effect>();
        this.inventory = new Array<String>();

        if(!abilities.equals("")) {
            for (String curAbiility : abilities.split(",")) {
                this.abilities.add(AbilityID.getIDByName(curAbiility));
            }
        }
        if(!effects.equals("")) {
            for (String curEffect : effects.split(",")) {
                this.effects.add(new Effect(curEffect));
            }
        }
        if(!inventory.equals("")) {
            for (String curItem : inventory.split(",")) {
                this.inventory.add(curItem);
            }
        }

        this.organization = organization;
    }
}
