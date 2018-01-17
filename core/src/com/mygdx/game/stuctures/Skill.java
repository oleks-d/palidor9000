package com.mygdx.game.stuctures;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.enums.AbilityID;

/**
 * Created by odiachuk on 1/17/18.
 */
public enum Skill {
    CLOSE_WEAPON1(1, "Close combat", "Using close combat weapon",
            new AbilityID[]{
                    AbilityID.AXE_SWING,
                    AbilityID.SWORD_SWING,
                    AbilityID.HUMMER_SWING
            } ,0),
    RANGE_WEAPON1(2, "Range combat", "Using range combat weapon",
            new AbilityID[]{
                    AbilityID.CROSSBOW_SHOT,
                    AbilityID.SLING_SHOT,
                    AbilityID.LONGBOW_SHOT,
            } ,0),
    FIRE1(3, "Magical fire", "Using Fire in combat",
            new AbilityID[]{
                    AbilityID.FIREWALL,
            } ,0),
    ICE1(4, "Magical ice", "Using Ice in combat",
            new AbilityID[]{
                    AbilityID.ICEWALL,
            } ,0),
    SHILED1(5, "Shield", "Using shield in combat",
            new AbilityID[]{
                    AbilityID.COVER
            } ,0),
    DODGE1(6, "Dodge", "Using reflexes and speed in combat",
            new AbilityID[]{
                    AbilityID.DODGE
            } ,0),
    MASK1(7, "Mask", "Hiding and silent movements",
            new AbilityID[]{
                    AbilityID.MASK
            } ,0),
    MAGIC1(8, "Magic", "Using magic powers",
            new AbilityID[]{
                    AbilityID.FLY
            } ,0),
    CHARISMA1(9, "Shouts", "Using charisma to make you stronger",
            new AbilityID[]{
                    AbilityID.SHOUT
            } ,0),
    CLOSE_WEAPON2(11, "Advanced Close combat", "Using special weapon strikes",
            new AbilityID[]{
                    AbilityID.DASH
            } ,1),
    RANGE_WEAPON2(12, "Advanced Range combat", "Using range combat weapon",
            new AbilityID[]{
                    AbilityID.JUMP_BACK,
            } ,2),
    FIRE2(13, "Advanced Magical fire", "Using Fire shield in combat",
            new AbilityID[]{
                    AbilityID.FIRESHIELD,
            } ,3),
    ICE2(14, "Advanced Magical ice", "Using Ice shield in combat",
            new AbilityID[]{
                    AbilityID.ICESHIELD,
            } ,4),
    SHILED2(15, "Advanced Shield", "Using shield cover in combat",
            new AbilityID[]{
                    AbilityID.BARSKIN
            } ,5),
    DODGE2(16, "Advanced Dodge", "Using reflexes and speed in combat",
            new AbilityID[]{
                    AbilityID.HASTE
            } ,6),
    MASK2(17, "Advanced Mask", "Pickpocketing",
            new AbilityID[]{
                    AbilityID.PICKPOCKET
            } ,7),
    MAGIC2(18, "Advanced Magic", "Using magic powers",
            new AbilityID[]{
                    AbilityID.INVISIBILITY,
            } ,8),
    CHARISMA2(19, "Advanced Shouts", "Using charisma to trade",
            new AbilityID[]{
                    AbilityID.TRADE
            } ,9),
    CLOSE_WEAPON3(21, "Master Close combat", "Mastering in close combat weapon",
            new AbilityID[]{
                    AbilityID.AXE_SMASH,
                    AbilityID.SWORD_SMASH,
                    AbilityID.HUMMER_SMASH,
            } ,11),
    RANGE_WEAPON3(22, "Master Range combat", "Mastering in range combat weapon",
            new AbilityID[]{
                    AbilityID.POWER_SHOT,
                    AbilityID.DITRUCTING_SHOT,
                    AbilityID.TRIPLE_SHOT
            } ,12),
    FIRE3(23, "Master Magical fire", "Using Fireball in combat",
            new AbilityID[]{
                    AbilityID.FIREBALL
            } ,13),
    ICE3(24, "Master Magical ice", "Using Icestorm in combat",
            new AbilityID[]{
                    AbilityID.ICESTORM
            } ,14),
    SHILED3(25, "Master Shield", "Using Fullprotection in combat",
            new AbilityID[]{
                    AbilityID.FULLPROTECTION
            } ,15),
    DODGE3(26, "Master Dodge", "Using reflexes and speed in combat",
            new AbilityID[]{
                    AbilityID.TIMESTOP
            } ,16),
    MASK3(27, "Master Mask", "Atack from hide makes more damage",
            new AbilityID[]{
                    AbilityID.SILENT_STRIKE
            } ,17),
    MAGIC3(28, "Master Magic", "Using magic powers to teleport",
            new AbilityID[]{
                    AbilityID.TELEPORT
            } ,18),
    CHARISMA3(29, "Master Shouts", "Using charisma to make you stronger",
            new AbilityID[]{
                    AbilityID.POWER_SHOUT
            } ,19);

    int id;

    Skill(int id, String name, String description, AbilityID[] abilities, int idOfRequiredSkill) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.abilities = abilities;
        this.idOfRequiredSkill = idOfRequiredSkill;
    }

    String name;
    String description;
    AbilityID[] abilities;
    int idOfRequiredSkill;



}
