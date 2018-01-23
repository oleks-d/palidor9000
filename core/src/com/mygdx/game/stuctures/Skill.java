package com.mygdx.game.stuctures;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.enums.AbilityID;

/**
 * Created by odiachuk on 1/17/18.
 */
public enum Skill {
    STRENGTH1(1, "Close combat", "Using close combat weapon",
            new AbilityID[]{
                    AbilityID.AXE_SWING,
                    AbilityID.SWORD_SWING,
                    AbilityID.HUMMER_SWING
            } , 100,0),
    STRENGTH2(11, "Advanced Close combat", "Using special weapon strikes",
            new AbilityID[]{
                    AbilityID.DASH
            } , 100,1),
    STRENGTH3(21, "Master Close combat", "Mastering in close combat weapon",
            new AbilityID[]{
                    AbilityID.AXE_SMASH,
                    AbilityID.SWORD_SMASH,
                    AbilityID.HUMMER_SMASH,
            } , 100,11),

    ACCURACY1(2, "Range combat", "Using range combat weapon",
            new AbilityID[]{
                    AbilityID.CROSSBOW_SHOT,
                    AbilityID.SLING_SHOT,
                    AbilityID.LONGBOW_SHOT,
            } , 100,0),
    ACCURACY2(12, "Advanced Range combat", "Using range combat weapon",
            new AbilityID[]{
                    AbilityID.JUMP_BACK,
            }, 100 ,2),
    ACCURACY3(22, "Master Range combat", "Mastering in range combat weapon",
            new AbilityID[]{
                    AbilityID.POWER_SHOT,
                    AbilityID.DITRUCTING_SHOT,
                    AbilityID.TRIPLE_SHOT
            } , 100,12),

    WILL1(3, "Magical fire", "Using Fire in combat",
            new AbilityID[]{
                    AbilityID.FIREWALL,
            } , 100,0),
    WILL2(13, "Advanced Magical fire", "Using Fire shield in combat",
            new AbilityID[]{
                    AbilityID.FIRESHIELD,
            }, 100 ,3),
    WILL3(23, "Master Magical fire", "Using Fireball in combat",
            new AbilityID[]{
                    AbilityID.FIREBALL
            } , 100,13),


    ENDURANCE1(5, "Shield", "Using shield in combat",
            new AbilityID[]{
                    AbilityID.COVER
            } , 100,0),
    ENDURANCE2(15, "Advanced Shield", "Using shield cover in combat",
            new AbilityID[]{
                    AbilityID.BARSKIN
            }, 100 ,5),
    ENDURANCE3(25, "Master Shield", "Using Fullprotection in combat",
            new AbilityID[]{
                    AbilityID.FULLPROTECTION
            }, 100 ,15),




    MASK1(7, "Stealth", "Hiding and silent movements",
            new AbilityID[]{
                    AbilityID.MASK
            }, 100 ,0),
    MASK2(17, "Advanced Stealth", "Pickpocketing",
            new AbilityID[]{
                    AbilityID.PICKPOCKET
            }, 100 ,7),

    MASK3(27, "Master Stealth", "Atack from hide makes more damage",
            new AbilityID[]{
                    AbilityID.SILENT_STRIKE
            }, 100 ,17),

    MAGIC1(8, "Magic", "Using magic powers",
            new AbilityID[]{
                    AbilityID.FLY
            } , 100,0),
    MAGIC2(18, "Advanced Magic", "Using magic powers",
            new AbilityID[]{
                    AbilityID.INVISIBILITY,
            } , 100,8),
    MAGIC3(28, "Master Magic", "Using magic powers to teleport",
            new AbilityID[]{
                    AbilityID.TELEPORT
            } , 100,18),


    CHARISMA1(9, "Shouts", "Using charisma to make you stronger",
            new AbilityID[]{
                    AbilityID.SHOUT
            } , 100,0),
    CHARISMA2(19, "Advanced Shouts", "Using charisma to trade",
            new AbilityID[]{
                    AbilityID.TRADE
            } , 100,9),
    CHARISMA3(29, "Master Shouts", "Using charisma to make you stronger",
            new AbilityID[]{
                    AbilityID.POWER_SHOUT
            } , 100,19),



    AGILITY1(6, "Dodge", "Using reflexes and speed in combat",
            new AbilityID[]{
                    AbilityID.DODGE
            }, 100 ,0),
    AGILITY2(16, "Advanced Dodge", "Using reflexes and speed in combat",
            new AbilityID[]{
                    AbilityID.POWERJUMP
            } , 100,6),
    AGILITY3(26, "Master Dodge", "Using reflexes and speed in combat",
            new AbilityID[]{
                    AbilityID.HASTE
            } , 100,16),




    INTELLIGENCE1(31, "Intelligance" , "Reading of text", new AbilityID[]{}, 100, 0),
    INTELLIGENCE2(32, "Advanced Intelligance", "Reading of foreign text" , new AbilityID[]{}, 100,31 ),
    INTELLIGENCE3(33, "Expert Intelligance", "Reading of Acient text" , new AbilityID[]{}, 100,32 );

    int id;
    String name;

    public String getName() {
        return name;
    }

    public AbilityID[] getAbilities() {
        return abilities;
    }

    public int getIdOfRequiredSkill() {
        return idOfRequiredSkill;
    }

    public int getPrice() {
        return price;
    }

    String description;
    AbilityID[] abilities;
    int idOfRequiredSkill;
    int price;

    Skill(int id, String name, String description, AbilityID[] abilities, int price,  int idOfRequiredSkill) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.abilities = abilities;
        this.idOfRequiredSkill = idOfRequiredSkill;
    }


    public String getDescription() {
        return description;
    }
}
