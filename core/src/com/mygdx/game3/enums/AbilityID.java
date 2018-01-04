package com.mygdx.game3.enums;

/**
 * Created by odiachuk on 12/22/17.
 */

public enum AbilityID{
    NONE("Nope", "None", "icon_blank", State.CASTING, AbilityType.BUFF, 0f, 0f),

    FIREBALL("Firewall" , "Wall of fire", "fire_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, 2f, 10f),
    ICEBALL("Icewall", "Wall of ice", "ice_icon", State.CASTING, AbilityType.LONG_RANGE_ATACK, 2f , 10f),

    PUNCH("Punch",  "Melee strike", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, 1f, 5f ),
    QUICK_PUNCH("Quick punch",  "Quick melee strike", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, 0.1f, 5f ),
    POWER_PUNCH("Power punch",  "Powerfull melee strike", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, 2f, 10f ),
    LEG_PUNCH("Leg punch",  "Melee strike to leg", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, 2f, 10f ),
    ARM_PUNCH("Arm punch",  "Melee strike to arm", "sword", State.KICKING, AbilityType.CLOSE_RANGE_ATACK, 2f, 10f ),

    TARGET_SHOT("Shot in target", "Shot in target", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, 1f, 5f),
    QUICK_SHOT("Quick shot", "Quick shot", "bow", State.SHOTING, AbilityType.LONG_RANGE_ATACK, 0.1f, 5f),
    DISTRACTING_SHOT("Distracting shot", "Distracting shot", "bow", State.SHOTING, AbilityType.LONG_RANGE_DEFENSE, 1f, 15f),
    SHOT_IN_LEG("Shot in leg", "Shot in leg", "bow", State.SHOTING, AbilityType.LONG_RANGE_DEFENSE, 1f, 15f),
    SHOT_IN_ARM("Shot in arm", "Shot in arm", "bow", State.SHOTING, AbilityType.LONG_RANGE_DEFENSE, 1f, 15f),

    HASTE("Haste", "Haste", "icon_blank", State.CASTING, AbilityType.BUFF, 3f, 30f),
    STOP_CAST("Stopcast", "Stopcast", "icon_blank", State.KICKING, AbilityType.CLOSE_RANGE_DEFENSE, 0.1f, 10f),
    STUN("Stun","Stun","icon_blank",State.KICKING,AbilityType.CLOSE_RANGE_DEFENSE , 0.3f , 30f),
    LONG_STUN("Long Stun","Stun","icon_blank",State.KICKING,AbilityType.CLOSE_RANGE_DEFENSE , 1f , 60f),

    DASH("Dash","Run forward","icon_blank",State.KICKING,AbilityType.LONG_RANGE_DEFENSE , 0.1f , 60f),
    JUMP_BACK("Jump back","Run backward","icon_blank",State.KICKING,AbilityType.LONG_RANGE_DEFENSE , 0.1f , 60f),

    PUSH("Push","Push forward","icon_blank",State.KICKING,AbilityType.LONG_RANGE_DEFENSE , 0.1f , 60f),
    PULL("Pull","Pull to me","icon_blank",State.KICKING,AbilityType.LONG_RANGE_DEFENSE , 0.1f , 60f);
    ;

    private final AbilityType type;
    String value;
    String description;
    String icon;
    private State state;
    float castTime;
    float cooldown;

    AbilityID(String value, String description, String icon, State state, AbilityType type, float castTime, float cooldown){
        this.value = value;
        this.description = description;
        this.icon = icon;
        this.state = state;
        this.type = type;
        this.castTime = castTime;
        this.cooldown = cooldown;
    }

    @Override
    public String toString() {
        return value;
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

    public AbilityType getType() {
        return type;
    }

    public float getCastTime() {
        return castTime;
    }

    public float getCooldownTime() {
        return cooldown;
    }
}
