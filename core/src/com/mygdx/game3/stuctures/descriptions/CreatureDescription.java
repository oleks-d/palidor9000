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
    public Array<String> equiped;
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

    public CreatureDescription(String name, String description, String region, Characteristics characteristics, int organization, String abilities, String effects, String inventory, String equiped) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.stats = characteristics;
        this.abilities = new Array<AbilityID>();
        this.effects = new Array<Effect>();
        this.inventory = new Array<String>();
        this.equiped = new Array<String>();

        if(!abilities.equals("")) {
            for (String curAbiility : abilities.split(",")) {
                //this.abilities.add(AbilityID.getIDByName(curAbiility));
                this.abilities.add(AbilityID.valueOf(curAbiility.trim()));
            }
        }
        if(!effects.equals("")) {
            for (String curEffect : effects.split(",")) {
                if(!curEffect.equals(""))
                    this.effects.add(new Effect(curEffect));
            }
        }
        if(!inventory.equals("")) {
            for (String curItem : inventory.split(",")) {
                if(!curItem.equals(""))
                    this.inventory.add(curItem.trim());
            }
        }

        if(!equiped.equals("")) {
            for (String curItem : equiped.split(",")) {
                if(!curItem.equals(""))
                    this.equiped.add(curItem.trim());
            }
        }

        this.organization = organization;
    }
}
