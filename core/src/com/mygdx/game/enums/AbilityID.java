package com.mygdx.game.enums;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by odiachuk on 12/22/17.
 */

public enum AbilityID{
    NONE("Nope", "None", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0f, 0f),

//    FIREWALL("Firewall" , "Wall of fire", "fire_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.SPRAY, 2f, 10f),
//    ICEWALL("Icewall", "Wall of ice", "ice_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.SPRAY, 2f , 10f),
//
    PUNCH("Punch",  "Melee strike", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 2f, 1f ),
//    QUICK_PUNCH("Quick punch",  "Quick melee strike", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 5f ),
//    POWER_PUNCH("Power punch",  "Powerfull melee strike", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 2f, 10f ),
//    LEG_PUNCH("Leg punch",  "Melee strike to leg", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 2f, 10f ),
//    ARM_PUNCH("Arm punch",  "Melee strike to arm", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 2f, 10f ),
//
//    TARGET_SHOT("Shot in target", "Shot in target", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 1f, 5f),
//    QUICK_SHOT("Quick shot", "Quick shot", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.1f, 5f),
//    DISTRACTING_SHOT("Distracting shot", "Distracting shot", "bow", State.SHOTING, AbilityType.LONG_RANGE_DEFENSE, ActivityAreaType.ARROW, 1f, 15f),
//    SHOT_IN_LEG("Shot in leg", "Shot in leg", "bow", State.SHOTING, AbilityType.LONG_RANGE_DEFENSE, ActivityAreaType.ARROW, 1f, 15f),
//
//    HASTE("Haste", "Haste", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 3f, 30f),
//    STOP_CAST("Stopcast", "Stopcast", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_DEFENSE, ActivityAreaType.BOX, 0.1f, 10f),
//
//    STUN("Stun","Stun","icon_blank",State.KICKING,AbilityType.CLOSE_RANGE_DEFENSE , ActivityAreaType.BOX, 0.3f , 30f),
//    LONG_STUN("Long Stun","Stun","icon_blank",State.KICKING,AbilityType.CLOSE_RANGE_DEFENSE , ActivityAreaType.BOX, 1f , 60f),

    DASH("Dash","Run forward","icon_blank", State.KICKING, AbilityType.BUFF , ActivityAreaType.ARROW, 0.1f , 60f),
    JUMP_BACK("Jump back","Run backward","icon_blank", State.STANDING, AbilityType.BUFF , ActivityAreaType.SELF, 0.1f , 60f),
    FLY("Jump back","Run backward","icon_blank", State.CASTING, AbilityType.BUFF , ActivityAreaType.SELF, 0.1f , 60f),

//    PUSH("Push","Push forward","icon_blank",State.KICKING,AbilityType.LONG_RANGE_DEFENSE , ActivityAreaType.BOX, 0.1f , 60f),
//    PULL("Pull","Pull to me","icon_blank",State.KICKING,AbilityType.LONG_RANGE_DEFENSE , ActivityAreaType.ARROW, 0.1f , 60f),

    HUMMER_SWING("Hummer swing",  "Melee strike", "hummer", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 1f ),
    AXE_SWING("Axe swing",  "Melee strike", "axe", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 1f ),
    SWORD_SWING("Sword swing",  "Melee strike", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 1f ),

    HUMMER_SMASH("Hummer SMASH",  "Stunning Melee strike", "hummer", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 10f ),
    AXE_SMASH("Axe SMASH",  "Powerfull Melee strike", "axe", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 10f ),
    SWORD_SMASH("Sword SMASH",  "Melee strike with multiple targets ", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, ActivityAreaType.BOX, 0.1f, 10f ),


    DITRUCTING_SHOT("Stuning shot", "Stunning shot", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.1f, 10f),
    POWER_SHOT("Power shot", "Three arrows one by one", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.1f, 10f),
    TRIPLE_SHOT("Triple shot", "Three arrows in different directions", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.1f, 10f),

    SLING_SHOT("Sling shot", "Shot with sling", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.1f, 1f),
    CROSSBOW_SHOT("Crossbow shot", "Shot with crossbow", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.1f, 1f),
    LONGBOW_SHOT("Long bow shot", "Shot with long bow", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.ARROW, 0.1f, 1f),

    DODGE("Dodge", "Avoid damage", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_DEFENSE, ActivityAreaType.BOX, 0.1f, 3f),
    COVER("Cover", "Cover with shield", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_DEFENSE, ActivityAreaType.BOX, 0.1f, 3f),

    HASTE("Haste", "Haste", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),
    BARSKIN("Barskin", "Add protectoin against any damage", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),

    SHOUT("Shout", "Makes you stronger" , "icon_cross", State.CASTING , AbilityType.BUFF , ActivityAreaType.SELF, 0.1f, 30f),

    MASK("Hide", "Hide from everyone", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 5f),

    FIREWALL("Firewall", "Fire", "fire_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.SPRAY, 0.1f, 1f),
    ICEWALL("Icewall", "Ice", "ice_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.SPRAY, 0.1f, 0.1f),
    FIRESHIELD("Fireshield", "Fireshield", "shield_red", State.CASTING, AbilityType.CLOSE_RANGE_DEFENSE, ActivityAreaType.SELF, 0.1f, 3f),
    ICESHIELD("Iceshield", "Iceshield", "shield", State.CASTING, AbilityType.CLOSE_RANGE_DEFENSE, ActivityAreaType.SELF, 0.1f, 3f),
    FIREBALL("Fireball", "Fireball", "fire_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.BOOM, 0.1f, 0.1f),
    ICESTORM("Icestorm", "Icestorm", "fire_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, ActivityAreaType.BOOM, 0.1f, 0.1f),


    PICKPOCKET("Pickpocket", "Steal", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.BOX, 0.1f, 5f),
    INVISIBILITY("Invisibility", "Hide from everyone", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 10f),
    TRADE("Trade", "Make influence", "icon_cross", State.CASTING , AbilityType.BUFF, ActivityAreaType.BOX, 0.1f, 5f),
    FULLPROTECTION("Fullprotection", "Immune to any damage", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),
    TIMESTOP("Timestop", "You are moving toooo fast", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),
    SILENT_STRIKE("Silent strike", "Damage from hide", "sword", State.KICKING, AbilityType.BUFF, ActivityAreaType.BOX, 0.1f, 60f),
    TELEPORT("Fullprotection", "Immune to any damage", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 60f),

    POWER_SHOUT("Fullprotection", "Immune to any damage", "icon_blank", State.CASTING, AbilityType.BUFF, ActivityAreaType.BIGBOX, 0.1f, 60f),
    POWERJUMP("Powerjump", "Jump twice", "icon_blank",State.CASTING, AbilityType.BUFF, ActivityAreaType.SELF, 0.1f, 1f);

    private final com.mygdx.game.enums.AbilityType type;
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
