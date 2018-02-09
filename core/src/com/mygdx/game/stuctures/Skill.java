package com.mygdx.game.stuctures;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.enums.AbilityID;

/**
 * Created by odiachuk on 1/17/18.
 */
public enum Skill {
    STRENGTH1(1, "Close combat", "Using close combat weapon",
            new AbilityID[]{
//                    AbilityID.AXE_SWING,
                    AbilityID.SWORD_SWING,
                    AbilityID.HUMMER_SWING
            } , 100,0),
    STRENGTH2(11, "Advanced Close combat", "Using Dash ability. \n +1 Crush, +1 Cut damage",
            new AbilityID[]{
                    AbilityID.DASH,
                    AbilityID.DIALOG_FEAR
            } , 100,1),
    STRENGTH3(21, "Master Close combat", "Mastering in close combat weapon",
            new AbilityID[]{
                    //AbilityID.AXE_SMASH,
                    AbilityID.SWORD_SMASH,
                    AbilityID.HUMMER_SMASH,
            } , 100,11),

    ACCURACY1(2, "Range combat", "Using range combat weapon",
            new AbilityID[]{
                    //AbilityID.CROSSBOW_SHOT,
                    AbilityID.SLING_SHOT,
                    AbilityID.LONGBOW_SHOT,
            } , 100,0),
    ACCURACY2(12, "Advanced Range combat", "Seeing details",
            new AbilityID[]{
                    AbilityID.SEEING_DETAILS,
            }, 100 ,2),
    ACCURACY3(22, "Master Range combat", "Mastering in range combat weapon",
            new AbilityID[]{
                    //AbilityID.POWER_SHOT,
                    AbilityID.DITRUCTING_SHOT,
                    AbilityID.TRIPLE_SHOT
            } , 100,12),

    WILL1(3, "Magical fire", "Using Fire in combat",
            new AbilityID[]{
                    AbilityID.FIREWALL,
                    AbilityID.ICEWALL,
            } , 100,0),
    WILL2(13, "Advanced Magical fire", "Using Fire shield in combat",
            new AbilityID[]{
                    AbilityID.DIALOG_USE_WISDOM,
                    AbilityID.FIRESHIELD,
                    AbilityID.ICESHIELD
            }, 100 ,3),
    WILL3(23, "Master Magical fire", "Using Fireball in combat",
            new AbilityID[]{
                    AbilityID.FIREBALL,
                    AbilityID.ICESTORM
            } , 100,13),


    ENDURANCE1(5, "Shield", "Using shield in combat. Cover: " + AbilityID.COVER.getDescription(),
            new AbilityID[]{
                    AbilityID.COVER
            } , 100,0),
    ENDURANCE2(15, "Advanced Shield", "Barskin : " + AbilityID.BARSKIN.getDescription(),
            new AbilityID[]{
                    AbilityID.BARSKIN,
                    AbilityID.DIALOG_MAKE_COME
            }, 100 ,5),
    ENDURANCE3(25, "Master Shield", "Fullprotection ability: " + AbilityID.FULLPROTECTION.getDescription(),
            new AbilityID[]{
                    AbilityID.FULLPROTECTION
            }, 100 ,15),




    MASK1(7, "Stealth", "Hiding and silent movements",
            new AbilityID[]{
                    AbilityID.MASK1
            }, 100 ,0),
    MASK2(17, "Advanced Stealth", "Pickpocketing",
            new AbilityID[]{
                    AbilityID.MASK2,
                    AbilityID.BACKSTUB,
                    AbilityID.PICKPOCKET,
                    AbilityID.DIALOG_LYING
            }, 100 ,7),

    MASK3(27, "Master Stealth", "Atack from hide makes more damage",
            new AbilityID[]{
                    AbilityID.MASK3,
                    AbilityID.DISAPPEAR
            }, 100 ,17),


    CHARISMA1(9, "Leadership", "Using charisma to make you stronger",
            new AbilityID[]{
                    AbilityID.LEADERSHIP
            } , 100,0),
    CHARISMA2(19, "Advanced Leadership", "Using charisma to trade",
            new AbilityID[]{
                    AbilityID.GREAT_LEADERSHIP,
                    AbilityID.DIALOG_TRADE,
            } , 100,9),
    CHARISMA3(29, "Master Leadership", "Using charisma to command stronger creatures",
            new AbilityID[]{
                    AbilityID.GREATEST_LEADERSHIP
            } , 100,19),



    AGILITY1(6, "Advanced Jump", "Jump height",
            new AbilityID[]{
                    AbilityID.BETTER_JUMP
            }, 100 ,0),
    AGILITY2(16, "Advanced Punch", "Powerpunch (Push enemy)",
            new AbilityID[]{
                    AbilityID.POWERPUNCH,
                    AbilityID.APPERPUNCH,
                    //AbilityID.POWERJUMP
            } , 100,6),
    AGILITY3(26, "Master Haste", "Using reflexes and speed in combat",
            new AbilityID[]{
                    AbilityID.HASTE
            } , 100,16),


    INTELLIGENCE1(31, "Intelligance" , "Reading of text", new AbilityID[]{AbilityID.READ_BASIC}, 100, 0),
    INTELLIGENCE2(32, "Advanced Intelligance", "Reading of foreign text" , new AbilityID[]{AbilityID.READ_FOREIGN}, 100,31 ),
    INTELLIGENCE3(33, "Expert Intelligance", "Reading of Ancient text" , new AbilityID[]{AbilityID.READ_ANCIENT}, 100,32 );

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

    public int getID() {
        return id;
    }
}
