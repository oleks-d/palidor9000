package com.mygdx.game.sprites.creatures;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.AbilityType;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.stuctures.Effect;
import com.mygdx.game.stuctures.Skill;
import com.mygdx.game.stuctures.descriptions.CreatureDescription;
import com.mygdx.game.tools.Fonts;

import java.util.HashMap;

/**
 * Created by odiachuk on 12/17/17.
 */
public class Hero extends Creature {

    public Array<AbilityID> selectedAtackAbilities = new Array<AbilityID> ();
    public Array<AbilityID> selectedDefenseAbilities = new Array<AbilityID> ();

    HashMap<String,String> GLOBAL_STATES;

    public String currentLevel;
    public String previousLevel;

    public Array<Skill> skills;
    public int experience = 0;

    public Hero (GameScreen screen,
                 CreatureDescription heroDescription,
                 String currentLevel,
                 String previousLevel,
                 HashMap<String,String> globalStates,
                 Array<AbilityID> selectedAtackAbilities,
                 Array<AbilityID> selectedDefenseAbilities,
                 Array<Skill> skills,
                 int experience,
                 String items){
        super(screen, heroDescription, items);

        this.canPickUpObjects = true;

        this.selectedAtackAbilities = selectedAtackAbilities;
        this.selectedDefenseAbilities =selectedDefenseAbilities;

        this.GLOBAL_STATES = globalStates;

        this.currentLevel = currentLevel;
        this.previousLevel = previousLevel;

        this.skills = skills;

        this.experience= experience;

        //equip
        for (String itemd : heroDescription.equiped){
            equipItem(new GameItem(screen, this.screen.levelmanager.ITEMS_DESCRIPTIONS.get(itemd.trim())));
        }
    }

    public void changeGlobalState(String key, String value){
        GLOBAL_STATES.put(key,value);
    }

    public String getGlobalState(String key){
        return GLOBAL_STATES.get(key);
    }

    public HashMap<String, String> getGlobalStates(){
        return GLOBAL_STATES;
    }

    public void deselectDefenseAbility(int index) {
        selectedDefenseAbilities.removeIndex(index);
    }

    public void deselectAtackAbility(int index) {
        selectedAtackAbilities.removeIndex(index);
    }

    public void selectAbility(AbilityID currentAbility) {
        if(currentAbility.getType()!= AbilityType.BUFF){
                if(!selectedAtackAbilities.contains(currentAbility, true) && selectedAtackAbilities.size<2)
                    selectedAtackAbilities.add(currentAbility);
                else if(!selectedDefenseAbilities.contains(currentAbility, true) && selectedDefenseAbilities.size<2)
                    selectedDefenseAbilities.add(currentAbility);
        }
    }


    public void shout() {
        if(abilities.contains(AbilityID.SHOUT, false))
            useAbility(AbilityID.SHOUT);
        else
            statusbar.addMessage("You can not shout", existingTime + 1f, Fonts.INFO);
    }

    public void hide() {
        if(abilities.contains(AbilityID.MASK, false))
            useAbility(AbilityID.MASK);
        else
            statusbar.addMessage("You can not hide", existingTime + 1f, Fonts.INFO);
        //useAbility(AbilityID.MASK);
    }

    public void fly() {
        if(abilities.contains(AbilityID.FLY, false))
            useAbility(AbilityID.FLY);
    }

    public void jumpBack() {
        if(abilities.contains(AbilityID.JUMP_BACK, false))
            useAbility(AbilityID.JUMP_BACK);
    }

    public void dash() {
        if(abilities.contains(AbilityID.DASH, false))
            useAbility(AbilityID.DASH);
    }


    public void attack(boolean isAbility1, boolean isPower) {
        if(isAbility1)
            if(isPower) {
                if (selectedAtackAbilities.size > 1)
                    useAbility(selectedAtackAbilities.get(1));
            }else {
                if (selectedAtackAbilities.size > 0)
                    useAbility(selectedAtackAbilities.get(0));
            }
        else
            if(isPower) {
                if (selectedDefenseAbilities.size > 1)
                    useAbility(selectedDefenseAbilities.get(1));
            }else {
                if (selectedDefenseAbilities.size > 0)
                    useAbility(selectedDefenseAbilities.get(0));
            }
    }


    // put item on and apply effect
    @Override
    public void equipItem(GameItem item){

        for(Effect curEffect : item.getEffects()){
            applyEffect(curEffect);
        }
        switch (item.getType()){
            case HEAD:
                head = item;
                break;
            case ARMOR:
                armor = item;
                break;
            // TODO add all
            case WEAPON_MAGIC_ICE:
            case WEAPON_MAGIC_FIRE:
                if(abilities.contains(AbilityID.FIREWALL, true)) {

                if(weapon1 == null) {
                    weapon1 = item;
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.FIREWALL);
                        if (abilities.contains(AbilityID.FIREBALL, true))
                            selectedAtackAbilities.add(AbilityID.FIREBALL);

                } else if (weapon2 == null) {
                        weapon2 = item;
                        selectedDefenseAbilities.clear();
                        if (abilities.contains(AbilityID.FIRESHIELD, true))
                            selectedDefenseAbilities.add(AbilityID.FIRESHIELD);
                        else {
                            selectedDefenseAbilities.add(AbilityID.FIREWALL);
                            if (abilities.contains(AbilityID.FIREBALL, true))
                                selectedAtackAbilities.add(AbilityID.FIREBALL);
                        }

                    }
                } else return;
                break;
//            case WEAPON_MAGIC_NATURE:
//            case WEAPON_MAGIC_DEATH:
            case    WEAPON_AXE:
                if(abilities.contains(AbilityID.AXE_SWING, true)) {

                    if(weapon1 == null) {
                        weapon1 = item;
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.AXE_SWING);
                        if (abilities.contains(AbilityID.AXE_SMASH, true))
                            selectedAtackAbilities.add(AbilityID.AXE_SMASH);

                    } else if (weapon2 == null) {
                        weapon2 = item;
                        selectedDefenseAbilities.clear();
                        selectedDefenseAbilities.add(AbilityID.AXE_SWING);
                        if (abilities.contains(AbilityID.AXE_SMASH, true))
                            selectedDefenseAbilities.add(AbilityID.AXE_SMASH);
                    }
                } else return;
                break;
            case    WEAPON_SWORD:
                if(abilities.contains(AbilityID.SWORD_SWING, true)) {

                    if(weapon1 == null) {
                        weapon1 = item;
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.SWORD_SWING);
                        if (abilities.contains(AbilityID.SWORD_SMASH, true))
                            selectedAtackAbilities.add(AbilityID.SWORD_SMASH);

                    } else if (weapon2 == null) {
                        weapon2 = item;
                        selectedDefenseAbilities.clear();
                        selectedDefenseAbilities.add(AbilityID.SWORD_SWING);
                        if (abilities.contains(AbilityID.SWORD_SMASH, true))
                            selectedDefenseAbilities.add(AbilityID.SWORD_SMASH);
                    }
                } else return;
                break;
//            case    WEAPON_HUMMER:
            case    WEAPON_BOW:
                if(abilities.contains(AbilityID.LONGBOW_SHOT, true)) {

                    if(weapon1 == null) {
                        weapon1 = item;
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.LONGBOW_SHOT);
                        if (abilities.contains(AbilityID.TRIPLE_SHOT, true))
                            selectedAtackAbilities.add(AbilityID.TRIPLE_SHOT);

                    } else if (weapon2 == null) {
                        weapon2 = item;
                        selectedDefenseAbilities.clear();
                        selectedDefenseAbilities.add(AbilityID.LONGBOW_SHOT);
                        if (abilities.contains(AbilityID.TRIPLE_SHOT, true))
                            selectedDefenseAbilities.add(AbilityID.TRIPLE_SHOT);
                    }
                } else return;
                break;
//            case    WEAPON_SLING:
//            case    WEAPON_XBOW:
            case    WEAPON_SHIELD:
                if(abilities.contains(AbilityID.COVER, true)) {
                    if (weapon1 == null) {
                        weapon1 = item;
                        selectedAtackAbilities.clear();
                        if (abilities.contains(AbilityID.COVER, true))
                            selectedAtackAbilities.add(AbilityID.COVER);
                        if (abilities.contains(AbilityID.BARSKIN, true))
                            selectedAtackAbilities.add(AbilityID.BARSKIN);
                    } else if (weapon2 == null) {
                        weapon2 = item;
                        selectedDefenseAbilities.clear();
                        if (abilities.contains(AbilityID.COVER, true))
                            selectedDefenseAbilities.add(AbilityID.COVER);
                        if (abilities.contains(AbilityID.BARSKIN, true))
                            selectedDefenseAbilities.add(AbilityID.BARSKIN);
                    }
                } else return;
                break;
        }
        inventory.removeValue(item,true);
        if(statusbar != null)
            statusbar.update();
    }

    //take off item - undo effect
    @Override
    public void unEquipItem(GameItem item){

        for(Effect curEffect : item.getEffects()){
            removeEffect(curEffect);
        }
        switch (item.getType()){
            case HEAD:
                head = null;
                inventory.add(item);
                break;
            case ARMOR:
                armor = null;
                inventory.add(item);
                break;
            case WEAPON_MAGIC_ICE:
            case WEAPON_MAGIC_FIRE:
            case WEAPON_MAGIC_NATURE:
            case WEAPON_MAGIC_DEATH:
            case    WEAPON_AXE:
            case    WEAPON_SWORD:
            case    WEAPON_HUMMER:
            case    WEAPON_BOW:
            case    WEAPON_SLING:
            case    WEAPON_XBOW:
            case    WEAPON_SHIELD:
                if(weapon1.equals(item)) {
                    weapon1 = null;
                    selectedAtackAbilities.clear();
                    if(abilities.contains(AbilityID.DODGE, true))
                        selectedAtackAbilities.add(AbilityID.DODGE);
                    inventory.add(item);
                } else if (weapon2.equals(item)) {
                    weapon2 = null;
                    if(abilities.contains(AbilityID.DODGE, true))
                        selectedDefenseAbilities.add(AbilityID.DODGE);
                    inventory.add(item);
                }

                break;
            // TODO add all
        }

        statusbar.update();
    }

    public void addAbilities(AbilityID[] abilities) {
        for(AbilityID ability : abilities){
            this.abilities.add(ability);
            this.cooldowns.put(ability,0d);;
        }
    }

    public Creature getNeighbor() {
        return closeNeighbor;
    }
}
