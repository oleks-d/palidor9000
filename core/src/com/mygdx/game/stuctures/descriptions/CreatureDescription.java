package com.mygdx.game.stuctures.descriptions;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.dialogs.GameDialog;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.stuctures.Characteristics;
import com.mygdx.game.stuctures.Effect;
import com.mygdx.game.stuctures.Stat;

/**
 * Created by odiachuk on 12/21/17.
 */
public class CreatureDescription {

    public String id;
    public String name;
    public String description;
    public String region;
    public Characteristics stats;
    public Array<AbilityID> abilities;
    public Array<Effect> effects;
    //public Array<String> inventory;
    public Array<String> equiped;
    public int organization;
    public Array<Integer> dialogs;


    public CreatureDescription(
            String id,
            String name,
            String description,
            String region,
            Characteristics characteristics,
            int organization,
            String abilities,
            String effects,
            //String inventory,
            String equiped,
            String dialogs) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.region = region;
        this.stats = characteristics;
        this.abilities = new Array<AbilityID>();
        this.effects = new Array<Effect>();
        //this.inventory = new Array<String>();
        this.equiped = new Array<String>();
        this.dialogs = new Array<Integer>();

        if(!abilities.equals("")) {
            for (String curAbiility : abilities.split(",")) {
                //this.abilities.add(AbilityID.getIDByName(curAbiility));
                this.abilities.add(com.mygdx.game.enums.AbilityID.valueOf(curAbiility.trim()));
            }
        }
        if(!effects.equals("")) {
            for (String curEffect : effects.split(",")) {
                if(!curEffect.equals(""))
                    this.effects.add(new Effect(curEffect));
            }
        }
//        if(!inventory.equals("")) {
//            for (String curItem : inventory.split(",")) {
//                if(!curItem.equals(""))
//                    this.inventory.add(curItem.trim());
//            }
//        }

        if(!equiped.equals("")) {
            for (String curItem : equiped.split(",")) {
                if(!curItem.equals(""))
                    this.equiped.add(curItem.trim());
            }
        }

        if(!dialogs.equals("")) {
            for (String curItem : dialogs.split(",")) {
                if(!curItem.equals(""))
                    this.dialogs.add(Integer.valueOf(curItem.trim()));
            }
        }

        this.organization = organization;
    }
}
