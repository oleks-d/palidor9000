package com.mygdx.game.sprites.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.AbilityType;
import com.mygdx.game.enums.State;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.stuctures.Effect;
import com.mygdx.game.stuctures.Skill;
import com.mygdx.game.stuctures.descriptions.CreatureDescription;
import com.mygdx.game.tools.ConditionProcessor;
import com.mygdx.game.tools.Fonts;

import java.util.HashMap;

import static com.mygdx.game.PalidorGame.PPM;

/**
 * Created by odiachuk on 12/17/17.
 */
public class Hero extends Creature {

    public Array<AbilityID> selectedAtackAbilities = new Array<AbilityID> ();
    public Array<AbilityID> selectedDefenseAbilities = new Array<AbilityID> ();

    public Array<AbilityID> summonAbilities = new Array<AbilityID> ();

    HashMap<String,String> GLOBAL_STATES;
    //HashMap<Integer, String> rivalOrganizations;

    public String currentLevel;
    public String previousLevel;

    public Array<Skill> skills;
    public int experience = 0;
    public int money = 0;


    public Hero (GameScreen screen,
                 CreatureDescription heroDescription,
                 String currentLevel,
                 String previousLevel,
                 HashMap<String,String> globalStates,
                 Array<AbilityID> selectedAtackAbilities,
                 Array<AbilityID> selectedDefenseAbilities,
                 Array<AbilityID> summonAbilities,
                 Array<Skill> skills,
                 int experience,
                 int money,
                 String items){
        super(screen, heroDescription, items, null, 0, null, null);

        setUniqueID(999999);

        this.canPickUpObjects = true;

        this.selectedAtackAbilities = selectedAtackAbilities;
        this.selectedDefenseAbilities =selectedDefenseAbilities;
        if(selectedAtackAbilities.size == 0)
            selectedAtackAbilities.add(AbilityID.PUNCH);
        if(selectedDefenseAbilities.size == 0)
            selectedDefenseAbilities.add(AbilityID.PUNCH);

        this.summonAbilities = summonAbilities;

        this.GLOBAL_STATES = globalStates;

        this.currentLevel = currentLevel;
        this.previousLevel = previousLevel;

        this.skills = skills;

        this.experience= experience;
        this.money = money;

//        rivalOrganizations = new HashMap<Integer,String>();  // TODO detect rivals
//        rivalOrganizations.put(0,"1,2");
//        rivalOrganizations.put(1,"0,2");
//        rivalOrganizations.put(2,"0,1");

//        weaponSprite = new WeaponSprite(this);
//        armorSprite = new ArmorSprite(this);

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
        if(currentAbility.getType()!= AbilityType.BUFF ){
                if(!selectedAtackAbilities.contains(currentAbility, true) && selectedAtackAbilities.size<2)
                    selectedAtackAbilities.add(currentAbility);
                else if(!selectedDefenseAbilities.contains(currentAbility, true) && selectedDefenseAbilities.size<2)
                    selectedDefenseAbilities.add(currentAbility);
        }
    }


//    public void shout() {
//        if(abilities.contains(AbilityID.SHOUT, false))
//            useAbility(AbilityID.SHOUT);
//        else
//            statusbar.addMessage("You can not shout", existingTime + 1f, Fonts.INFO);
//    }
//
//    public void poweshout() {
//        if(abilities.contains(AbilityID.POWER_SHOUT, false))
//            useAbility(AbilityID.POWER_SHOUT);
//        else
//            statusbar.addMessage("You can not Power shout", existingTime + 1f, Fonts.INFO);
//    }

    public void hide() {
        if(abilities.contains(AbilityID.MASK3, false)) {
            if (!IN_BATTLE || abilities.contains(AbilityID.DISAPPEAR, false))
                useAbility(AbilityID.MASK3);
            else
                statusbar.addMessage("You can not hide in battle", existingTime + 1f, Fonts.INFO);
        } else if(abilities.contains(AbilityID.MASK2, false)) {
            if (!IN_BATTLE || abilities.contains(AbilityID.DISAPPEAR, false))
                useAbility(AbilityID.MASK2);
            else
                statusbar.addMessage("You can not hide in battle", existingTime + 1f, Fonts.INFO);
        } else  if(abilities.contains(AbilityID.MASK1, false)) {
            if (!IN_BATTLE || abilities.contains(AbilityID.DISAPPEAR, false))
                useAbility(AbilityID.MASK1);
            else
                statusbar.addMessage("You can not hide in battle", existingTime + 1f, Fonts.INFO);
        }
    }

    public void fly() {
        if(abilities.contains(AbilityID.FLY, false))
            useAbility(AbilityID.FLY);
    }
//
//    public void jumpBack() {
//        if(abilities.contains(AbilityID.JUMP_BACK, false))
//            useAbility(AbilityID.JUMP_BACK);
//    }

    public void powerjump() {
        if(getState() == State.JUMPING) {
            if (abilities.contains(AbilityID.POWERJUMP, false))
                useAbility(AbilityID.POWERJUMP);
        } else
            jump();
    }

        public void haste() {
        if(abilities.contains(AbilityID.HASTE, false))
            useAbility(AbilityID.HASTE);
    }

    public void dash() {
        if(abilities.contains(AbilityID.DASH, false))
            useAbility(AbilityID.DASH);
    }

    public void pickpocket() {
        if(abilities.contains(AbilityID.PICKPOCKET, false))
            useAbility(AbilityID.PICKPOCKET);
    }

    public void attack(boolean isAbility1, boolean isPower) {
        //Gdx.app.log("" + isAbility1,""+isPower);
        if(isAbility1) {
            weaponSprite.isMoving = true;
            if (isPower) {
                if (selectedAtackAbilities.size > 1)
                    useAbility(selectedAtackAbilities.get(1));
                else
                    useAbility(selectedAtackAbilities.get(0));
            } else {
                if (selectedAtackAbilities.size > 0)
                    useAbility(selectedAtackAbilities.get(0));
            }
        }else {
            weaponSprite2.isMoving = true;
            if (isPower) {
                if (selectedDefenseAbilities.size > 1)
                    useAbility(selectedDefenseAbilities.get(1));
                else
                    useAbility(selectedDefenseAbilities.get(0));
            } else {
                if (selectedDefenseAbilities.size > 0)
                    useAbility(selectedDefenseAbilities.get(0));
            }
        }
    }


    // put item on and apply effect
    @Override
    public String equipItem(GameItem item){
        String result = item.itemdescription;

        switch (item.getType()){
            case HEAD:
                head = item;
                break;
            case ARMOR:
                armor = item;
                armorSprite.setPicture(item.getPicture());
                this.stats.jumphight.current = this.stats.jumphight.current - 1;
                break;
            case WEAPON_MAGIC_ICE:
                if(abilities.contains(AbilityID.ICEWALL, true)) {

                    if(weapon1 == null) {
                        weapon1 = item;
                        weaponSprite.setPicture(item.getPicture());
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.ICEWALL);
                        if (abilities.contains(AbilityID.ICESTORM, true))
                            selectedAtackAbilities.add(AbilityID.ICESTORM);

                    } else if (weapon2 == null) {
                        weapon2 = item;
                        weaponSprite2.setPicture(item.getPicture());
                        selectedDefenseAbilities.clear();
//                        if (abilities.contains(AbilityID.FIRESHIELD, true))
//                            selectedDefenseAbilities.add(AbilityID.FIRESHIELD);
//                        else {
                        selectedDefenseAbilities.add(AbilityID.ICEWALL);
                        if (abilities.contains(AbilityID.ICESTORM, true))
                            selectedDefenseAbilities.add(AbilityID.ICESTORM);
                        //}

                    } else  return "Item does not fit any slot (remove equiped item)";
                } else return "NO REQUIRED SKILL (WILL)";;
                break;
            case WEAPON_MAGIC_FIRE:
                if(abilities.contains(AbilityID.FIREWALL, true)) {

                    if(weapon1 == null) {
                        weapon1 = item;
                        weaponSprite.setPicture(item.getPicture());
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.FIREWALL);
                        if (abilities.contains(AbilityID.FIREBALL, true))
                            selectedAtackAbilities.add(AbilityID.FIREBALL);

                    } else if (weapon2 == null) {
                        weapon2 = item;
                        weaponSprite2.setPicture(item.getPicture());
                        selectedDefenseAbilities.clear();
//                        if (abilities.contains(AbilityID.FIRESHIELD, true))
//                            selectedDefenseAbilities.add(AbilityID.FIRESHIELD);
//                        else {
                            selectedDefenseAbilities.add(AbilityID.FIREWALL);
                            if (abilities.contains(AbilityID.FIREBALL, true))
                                selectedDefenseAbilities.add(AbilityID.FIREBALL);
                        //}

                    } else  return "Item does not fit any slot (remove equiped item)";
                } else return "NO REQUIRED SKILL (WILL)";;
                break;
//            case WEAPON_MAGIC_NATURE:
//            case WEAPON_MAGIC_DEATH:
//            case    WEAPON_AXE:
//                if(abilities.contains(AbilityID.AXE_SWING, true)) {
//
//                    if(weapon1 == null) {
//                        weapon1 = item;
//                        selectedAtackAbilities.clear();
//                        selectedAtackAbilities.add(AbilityID.AXE_SWING);
//                        if (abilities.contains(AbilityID.AXE_SMASH, true))
//                            selectedAtackAbilities.add(AbilityID.AXE_SMASH);
//
//                    } else if (weapon2 == null) {
//                        weapon2 = item;
//                        selectedDefenseAbilities.clear();
//                        selectedDefenseAbilities.add(AbilityID.AXE_SWING);
//                        if (abilities.contains(AbilityID.AXE_SMASH, true))
//                            selectedDefenseAbilities.add(AbilityID.AXE_SMASH);
//                    }
//                } else return "NO REQUIRED SKILL (STRENTH)";;
//                break;

            case    WEAPON_AXE:  // AXE and SWORD are same so far
            case    WEAPON_SWORD:
                if(abilities.contains(AbilityID.SWORD_SWING, true)) {

                    if(weapon1 == null) {
                        weapon1 = item;
                        weaponSprite.setPicture(item.getPicture());
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.SWORD_SWING);
                        if (abilities.contains(AbilityID.SWORD_SMASH, true))
                            selectedAtackAbilities.add(AbilityID.SWORD_SMASH);

                    } else if (weapon2 == null) {
                        weapon2 = item;
                        weaponSprite2.setPicture(item.getPicture());
                        selectedDefenseAbilities.clear();
                        selectedDefenseAbilities.add(AbilityID.SWORD_SWING);
                        if (abilities.contains(AbilityID.SWORD_SMASH, true))
                            selectedDefenseAbilities.add(AbilityID.SWORD_SMASH);
                    }  else  return "Item does not fit any slot (remove equiped item)";
                } else return "NO REQUIRED SKILL (STRENTH)";
                break;
            case    WEAPON_HUMMER:
                if(abilities.contains(AbilityID.HUMMER_SWING, true)) {

                    if(weapon1 == null) {
                        weapon1 = item;
                        weaponSprite.setPicture(item.getPicture());
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.HUMMER_SWING);
                        if (abilities.contains(AbilityID.HUMMER_SMASH, true))
                            selectedAtackAbilities.add(AbilityID.HUMMER_SMASH);

                    } else if (weapon2 == null) {
                        weapon2 = item;
                        weaponSprite2.setPicture(item.getPicture());
                        selectedDefenseAbilities.clear();
                        selectedDefenseAbilities.add(AbilityID.HUMMER_SWING);
                        if (abilities.contains(AbilityID.HUMMER_SMASH, true))
                            selectedDefenseAbilities.add(AbilityID.HUMMER_SMASH);
                    }  else  return "Item does not fit any slot (remove equiped item)";
                } else return "NO REQUIRED SKILL (STRENTH)";
                break;
            case    WEAPON_BOW:
                if(abilities.contains(AbilityID.LONGBOW_SHOT, true)) {

                    if(weapon1 == null) {
                        weapon1 = item;
                        weaponSprite.setPicture(item.getPicture());
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.LONGBOW_SHOT);
                        if (abilities.contains(AbilityID.TRIPLE_SHOT, true))
                            selectedAtackAbilities.add(AbilityID.TRIPLE_SHOT);

                    } else if (weapon2 == null) {
                        weapon2 = item;
                        weaponSprite2.setPicture(item.getPicture());
                        selectedDefenseAbilities.clear();
                        selectedDefenseAbilities.add(AbilityID.LONGBOW_SHOT);
                        if (abilities.contains(AbilityID.TRIPLE_SHOT, true))
                            selectedDefenseAbilities.add(AbilityID.TRIPLE_SHOT);
                    }  else  return "Item does not fit any slot (remove equiped item)";
                } else return "NO REQUIRED SKILL (ACCURACY)";;
                break;
            case    WEAPON_SLING:
                if(abilities.contains(AbilityID.SLING_SHOT, true)) {

                    if(weapon1 == null) {
                        weapon1 = item;
                        weaponSprite.setPicture(item.getPicture());
                        selectedAtackAbilities.clear();
                        selectedAtackAbilities.add(AbilityID.SLING_SHOT);
                        if (abilities.contains(AbilityID.DITRUCTING_SHOT, true))
                            selectedAtackAbilities.add(AbilityID.DITRUCTING_SHOT);

                    } else if (weapon2 == null) {
                        weapon2 = item;
                        weaponSprite2.setPicture(item.getPicture());
                        selectedDefenseAbilities.clear();
                        selectedDefenseAbilities.add(AbilityID.SLING_SHOT);
                        if (abilities.contains(AbilityID.DITRUCTING_SHOT, true))
                            selectedDefenseAbilities.add(AbilityID.DITRUCTING_SHOT);
                    }  else  return "Item does not fit any slot (remove equiped item)";
                } else return "NO REQUIRED SKILL (ACCURACY)";;
                break;
//            case    WEAPON_XBOW:
            case    WEAPON_SHIELD:
                if(abilities.contains(AbilityID.COVER, true)) {
                     if (weapon2 == null) {
                        weapon2 = item;
                        weaponSprite2.setPicture(item.getPicture());
                        selectedDefenseAbilities.clear();
                        if (abilities.contains(AbilityID.COVER, true))
                            selectedDefenseAbilities.add(AbilityID.COVER);
                        if (abilities.contains(AbilityID.FULLPROTECTION, true))
                            selectedDefenseAbilities.add(AbilityID.FULLPROTECTION);
                    } else return "Item does not fit into any slot (remove equiped Secondary item)";
                } else return "NO REQUIRED SKILL (ENDURANCE)";
                break;
            default:
                return "Item can not be equiped";
        }

        //after adding item
        inventory.removeValue(item,true);
        for(Effect curEffect : item.getEffects()){
            applyEffect(curEffect);
        }

        if(statusbar != null)
            statusbar.update();

        return result;
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
                armorSprite.setPicture(null);
                this.stats.jumphight.current = this.stats.jumphight.current + 1;
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
                if(weapon1 != null && weapon1.equals(item)) {
                    weapon1 = null;
                    weaponSprite.setPicture("weapon_hand");
                    selectedAtackAbilities.clear();
                    if(abilities.contains(AbilityID.POWERPUNCH, true)) {
                        selectedAtackAbilities.add(AbilityID.POWERPUNCH);
                        if (abilities.contains(AbilityID.UPPERPUNCH, true))
                            selectedAtackAbilities.add(AbilityID.UPPERPUNCH);
                    } else {
                        selectedAtackAbilities.add(AbilityID.PUNCH);
                    }
//                    if(abilities.contains(AbilityID.DODGE, true))
//                        selectedAtackAbilities.add(AbilityID.DODGE);
                    inventory.add(item);
                } else if (weapon2 != null && weapon2.equals(item)) {
                    weapon2 = null;
                    weaponSprite2.setPicture("weapon_hand");
                    selectedDefenseAbilities.clear();
                    if(abilities.contains(AbilityID.POWERPUNCH, true)) {
                        selectedDefenseAbilities.add(AbilityID.POWERPUNCH);
                        if (abilities.contains(AbilityID.UPPERPUNCH, true))
                            selectedDefenseAbilities.add(AbilityID.UPPERPUNCH);
                    } else {
                        selectedDefenseAbilities.add(AbilityID.PUNCH);
                    }
//                    if(abilities.contains(AbilityID.DODGE, true))
//                        selectedDefenseAbilities.add(AbilityID.DODGE);
                    inventory.add(item);
                }

                break;
            // TODO add all
        }

        statusbar.update();
    }

    @Override
    public String useItem(GameItem item) {
        if(item.getCondition()!=null && !"".equals(item.getCondition())){
            if(ConditionProcessor.conditionSatisfied(this, item.getCondition())){
                if(item.getEffects() != null && item.getEffects().size>0) {
                    for (Effect curEffect : item.getEffects()) {
                        applyEffect(curEffect);
                    }
                }
                if(item.getProcess() != null && !"".equals(item.getProcess()))
                    ConditionProcessor.conditionProcess(this,item.getProcess());
                inventory.removeValue(item,true);
                addStatusMessage(item.itemname + " was used", Fonts.INFO);

                return "";

            } else {
                return "You can not use this so far";
            }

        }

        if(item.getEffects() != null && item.getEffects().size>0) {
            for (Effect curEffect : item.getEffects()) {
                applyEffect(curEffect);
            }
        }
        if(item.getProcess() != null && !"".equals(item.getProcess()))
            ConditionProcessor.conditionProcess(this,item.getProcess());
        inventory.removeValue(item,true);
        addStatusMessage(item.itemname + " was used", Fonts.INFO);

        return "";
    }

    public void addAbilities(Array<AbilityID> abilities) {
        for(AbilityID ability : abilities){
            addStatusMessage("Ability: " + ability.getName(), Fonts.GOOD);
            this.abilities.add(ability);
            this.cooldowns.put(ability,0d);

            // special processing of skills
            if(abilities.contains(AbilityID.BETTER_JUMP, true)) {
                stats.jumphight.base = stats.jumphight.base + 1;
                stats.jumphight.current = stats.jumphight.current + 1;
            }
            if(abilities.contains(AbilityID.BARSKIN, true)) {
                stats.health.base = stats.health.base + 1;
                stats.health.current = stats.health.current + 1;
            }

            if(ability.getType() == AbilityType.SUMMON){
                summonAbilities.add(ability);
            }

        }
    }

    public Creature getNeighbor() {
        return closeNeighbor;
    }

    public void removeMoney(String amount) {
        money = money - Integer.valueOf(amount);
    }
    public void addMoney(String amount) {
        money = money + Integer.valueOf(amount);
    }

    public void addExperience(String amount) {
        experience = experience + Integer.valueOf(amount);
    }

    public boolean hasSkillByID(int id) {
        if(id == 0) return true;

        for(Skill skill : skills){
            if (skill.getID() == id){
                return true;
            }
        }
        return false;
    }


    public void summonCreature(int i) {

        if(summonedCreature.equals("")) {
            if (i < summonAbilities.size)
                useAbility(summonAbilities.get(i));
        } else {
            addStatusMessage("You already have one", Fonts.INFO);
        }
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
        addAbilities(skill.getAbilities());
        addStatusMessage("Skill: " + skill.getName(), Fonts.GOOD);
    }

    public Array<Skill> getSkills() {
        return skills;
    }


    public boolean isAbleToSee(Creature creature) {
        if(creature.isHidden()){
            if(abilities.contains(AbilityID.SEEING_DETAILS, false) &&
                    ( getBody().getPosition().x < creature.getBody().getPosition().x + creature.sight/PPM / 2 && getBody().getPosition().x > creature.getBody().getPosition().x  - creature.sight/PPM/2)){
                return true;
            } else
                return false;
        }
        return true;
    }
}
