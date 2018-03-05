package com.mygdx.game.enums;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by odiachuk on 12/22/17.
 */

public enum AbilityID{
    NONE("Nope", "None", "icon_blank", State.CASTING, AbilityType.FLAG, ActivityAreaType.SELF, 0f, 0f),
    PUNCH("Punch",  "Melee strike", "fist", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.6f, 1f ),


    HUMMER_SWING("Smash",  "Melee strike \n (crush damage)", "hummer", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.5f, 0.3f ),
//    AXE_SWING("Axe swing",  "Melee strike", "axe", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 1f ),
    SWORD_SWING("Swing",  "Melee strike \n (cut damage)", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.3f, 0.3f ),

    DASH("Dash","Run forward and push enemy \n (interrupts and locks ability that enemy is casting)","icon_blank", State.KICKING, AbilityType.LONG_RANGE_ATACK , ActivityAreaType.BOX, 0.1f , 0.2f),

    HUMMER_SMASH("Total SMASH",  "Stunning Melee strike (use Smash twice) \n (stun 3 sec)", "hummer_red", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.6f, 5f ),
//    AXE_SMASH("Axe SMASH",  "Powerfull Melee strike", "axe_red", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 10f ),
    SWORD_SMASH("Circle of death",  "Melee strike with multiple targets (use Swing twice) \n every enemy around will be damaged", "sword_red", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.5f, 1f ),

    HUMMER_SMASH2("Total SMASH",  "Stunning Melee strike (use Smash twice) \n (stun 3 sec)", "hummer_red", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.6f, 5f ),
    //    AXE_SMASH("Axe SMASH",  "Powerfull Melee strike", "axe_red", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 10f ),
    SWORD_SMASH2("Circle of death",  "Melee strike with multiple targets (use Swing twice) \n every enemy around will be damaged", "sword_red", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.5f, 1f ),


    DITRUCTING_SHOT("Stuning shot", "Stunning shot \n (2 sec of Stun)", "sling_red", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ROCK, 0.3f, 5f),
    //POWER_SHOT("Power shot", "Three arrows one by one", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.1f, 10f),
    TRIPLE_SHOT("Triple shot", "Shot Five arrows in different front directions", "bow_red", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.3f, 5f),

    SLING_SHOT("Sling shot", "Shot with sling \n (crush damage)", "sling", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ROCK, 0.2f, 1f),
   // CROSSBOW_SHOT("Crossbow shot", "Shot with crossbow", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.1f, 1f),
    LONGBOW_SHOT("Long bow shot", "Shot with long bow \n (cut damage)", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.2f, 1f),

    DODGE("Dodge", "Makes your jump higher \n Gives ability to avoid damage \n Effect last only one second \n and allows to avoid any damage from enemy abilities", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_DEFENSE, ActivityAreaType.BOX, 0.1f, 1f),
    POWERJUMP("Powerjump", "Jump twice", "icon_blank",State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 5f),
    HASTE("Haste", "Running speed increased for 10 seconds \n (use Dodge ability twice)", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),


    COVER("Cover", "Cover with shield", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_DEFENSE, ActivityAreaType.BOX, 0.1f, 3f),
    BARSKIN("Barskin", "Add protectoin against any damage", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),
    FULLPROTECTION("Fullprotection", "Immune to any damage", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),

//    // shouts should depend on weapon in hands
//    SHOUT("Shout", "Makes you stronger" , "icon_cross", State.CASTING , AbilityType.BUFF , ActivityAreaType.SELF, 0.1f, 30f),
//    POWER_SHOUT("Power shout", "Makes you muuuch stronger", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.BIGBOX, 0.1f, 60f),
//    TRADE("Trade", "Sell item on a full price", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.BOX, 0.1f, 5f),

    FIREWALL("Firewall", "Wall of fire in specified direction \n (fire damage)", "fire_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.SPRAY, 0.3f, 0.3f),
    ICEWALL("Icewall", "Ice", "ice_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.SPRAY, 0.6f, 0.3f),
    FIRESHIELD("Fireshield", "Shield of fire \n Enemy that will atack you will get fire damage", "shield_red", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 3f),
    ICESHIELD("Iceshield", "Shield of ice \n Enemy that will atack you will became slower", "shield", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 3f),
    FIREBALL("Fireball", "Explosion", "stuff_red", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.SPRAY, 1f, 5f),
    ICESTORM("Icestorm", "Icestorm", "stuff_blue", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.SPRAY, 1f, 5f),


    MASK1("Hide", "Hide from everyone (DOUBLE CLICK USE)\n (touch to other creature or usage of any ability will remove Hide)", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 5f),
    MASK2("Greater Hide", "Hide from everyone (DOUBLE CLICK USE)\n (touch to other creature or usage of any ability will remove Hide)", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 5f),
    MASK3("Master of shadows", "Hide from everyone (DOUBLE CLICK USE)\n (touch to other creature or usage of any ability will remove Hide)", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 5f),
    PICKPOCKET("Pickpocket", "Steal item from creature (DOUBLE CLICK USE in Hidden mode)\n", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.BOX, 0.1f, 5f),
//    SILENT_STRIKE("Silent strike", "Double damage from behind", "sword", State.KICKING, AbilityType.BUFF, ActivityAreaType.BOX, 0.1f, 60f),

//    JUMP_BACK("Jump back","Run backward","icon_blank", State.STANDING, AbilityType.BUFF , ActivityAreaType.SELF, 0.1f , 60f),
    FLY("Fly","Falling slower","icon_blank", State.CASTING, AbilityType.FLAG , ActivityAreaType.SELF, 0.1f , 1f),
//    INVISIBILITY("Invisibility", "Hide from everyone", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 10f),
//    TIMESTOP("Timestop", "You are moving toooo fast", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),
    //TELEPORT("Teleport", "Move to ", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),


    READ_BASIC("Reading", "Allows to read Signs and Books" , "book", State.CASTING,AbilityType.FLAG, ActivityAreaType.SELF, 0.1f,1f),
    READ_FOREIGN("Reading", "Allows to read Signs and Books using foreign language \n Allows to talk with foreigners" , "book", State.CASTING,AbilityType.FLAG, ActivityAreaType.SELF, 0.1f,1f),
    READ_ANCIENT("Reading", "Allows to read Signs and Books using Ancient language \n Allows to talk with Ancient creatures" , "book", State.CASTING,AbilityType.FLAG, ActivityAreaType.SELF, 0.1f,1f),


    SUMMON_MARK("Call Mark", "Allows to call Mark" , "hummer_blue", State.CASTING,AbilityType.SUMMON, ActivityAreaType.SELF, 3f,45f),
    SUMMON_ROLF("Call Rolf", "Allows to call Rolf" , "axe", State.CASTING,AbilityType.SUMMON, ActivityAreaType.SELF, 3f,45f),
    SUMMON_DEMON("Call Demon", "Allows to call Demon" , "hummer_red", State.CASTING,AbilityType.SUMMON, ActivityAreaType.SELF, 3f,45f),

    SUMMON_BAD_DEMON("Call Demon", "Allows to call Demon" , "hummer_red", State.CASTING,AbilityType.SUMMON, ActivityAreaType.SELF, 3f,45f),

    //special abilities
    ROLFS_TALK("Talk with Rolfs", "Allows to become a friend with a Rolf " , "icon_blank", State.CASTING,AbilityType.FLAG, ActivityAreaType.SELF, 0.1f,1f),


    BACKSTUB("Backstub", "Damage made in a back" , "sword_red" , State.KICKING , AbilityType.FLAG ,ActivityAreaType.BOX, 1f , 1f),
    DISAPPEAR("Disappear", "Hide from everyone in combat", "icon_cross", State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),


    SEEING_DETAILS("Seeing details", "See strength and weeknesses of creature" , "icon_blank", State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),
    DIALOG_FEAR("Intimidate", "Intimidate in dialogs" , "icon_blank" , State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),
    DIALOG_USE_WISDOM("Wisdm", "Share wisdom in dialogs" , "icon_blank", State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),
    DIALOG_MAKE_COME("Soothe", "Show support in dialogs" ,"icon_blank" , State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),
    DIALOG_LYING("Lying", "Lie in dialogs" ,"icon_blank" ,  State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),
    DIALOG_TRADE("Trading", "Trade in dialogs" , "icon_blank", State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),

    LEADERSHIP("Leadersip", "Allows to have a followers" , "icon_blank", State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),
    GREAT_LEADERSHIP("Great leadersip", "Allows to have strong followers", "icon_blank", State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),
    GREATEST_LEADERSHIP("Greater leadersip", "Allows to have  strongest followers", "icon_blank", State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),

    BETTER_JUMP("Better jump", "Jump higher" , "icon_blank" , State.CASTING , AbilityType.FLAG, ActivityAreaType.SELF, 0.1f, 5f),
    POWERPUNCH ("Powerpush","Push forward","fist_red",State.KICKING,AbilityType.CLOSE_RANGE_ATACK , ActivityAreaType.BOX, 0.2f , 0.5f),
    UPPERPUNCH ("Powerpunch","Appercote","fist_red",State.KICKING,AbilityType.CLOSE_RANGE_ATACK , ActivityAreaType.BOX, 0.2f , 0.5f),


    RAGE("Rage", "Makes you stronger" , "icon_cross_red", State.CASTING , AbilityType.BUFF , ActivityAreaType.SELF, 0.7f, 5f),
    MAGIC_RAGE("Magic Rage", "Makes your spells stronger" , "icon_cross_red", State.CASTING , AbilityType.BUFF , ActivityAreaType.SELF, 0.7f, 5f),

    HEAL("Heal", "Healing" , "icon_cross_green", State.CASTING , AbilityType.BUFF , ActivityAreaType.SELF, 1f, 5f),

    APPEAR("Appearing", "Sumoned", "icon_cross_red", State.CASTING , AbilityType.FLAG , ActivityAreaType.SELF, 2f, 0.1f),

    // for mechs
    TURREL_PIU("Piu left", "Arrow" , "icon_blank", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.5f, 0.5f ),

    //creatures actions
    ANIMAL_PUNCH("Bite",  "Melee strike", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.7f, 1f ),
    ANIMAL_POKE("Poke",  "Melee strike", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.5f, 1f ),
    ANIMAL_CLAW("Claw",  "Melee strike", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.9f, 1f ),

    ANIMAL_SHIELD("Covering with shell", "Cover with shell", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_DEFENSE, ActivityAreaType.BOX, 0.1f, 3f),
    ACID_SHOT("Splash", "Acid arrow" , "icon_blank", State.CASTING,AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.4f, 0.5f ),
    SPIKE_SHOT("Showing spikes", "Spike arrow" , "icon_blank", State.CASTING,AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.5f, 0.5f ),
    ANIMAL_DASH("Dash","Run forward and push enemy \n (interrupts and locks ability that enemy is casting)","icon_blank", State.KICKING, AbilityType.LONG_RANGE_ATACK , ActivityAreaType.BOX, 0.6f , 10f);

    private final AbilityType type;
    String value;
    String description;
    String icon;
    private State state;
    float castTime;
    float cooldown;
    private ActivityAreaType activityAreaType;

    AbilityID(String value, String description, String icon, State state, AbilityType type, ActivityAreaType activityAreaType, float castTime, float cooldown){
        this.value = value;
        this.description = description;
        this.icon = icon;
        this.state = state;
        this.type = type;
        this.castTime = castTime;
        this.cooldown = cooldown;
        this.activityAreaType = activityAreaType;
    }

    @Override
    public String toString() {
        return super.toString();
        //return value;
    }

    public static AbilityID getIDByName(String name){
        for (AbilityID current : values()){
            if(current.value.equals(name))
                return current;
        }
        return NONE;
    }

    public String getName() {
        return value;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public State getState() {
        return state;
    }

    public com.mygdx.game.enums.AbilityType getType() {
        return type;
    }

    public float getCastTime() {
        return castTime;
    }

    public float getCooldownTime() {
        return cooldown;
    }

    public ActivityAreaType getActivityAreaType() {
        return activityAreaType;
    }

}
