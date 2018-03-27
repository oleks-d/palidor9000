package com.mygdx.game.enums;

/**
 * Created by odiachuk on 12/22/17.
 */

public enum EffectID {

    //damage
    CUT_DAMAGE("bleed_icon", false, "Сhopped wound"),
    CRUSH_DAMAGE("blunt_icon",false, "Injury"),

    FIRE_DAMAGE("fire_icon", false, "Burn"),
    ICE_DAMAGE("ice_icon", false, "Burn"),

    BLEEDING("bleed_icon", false, "Bleeding"),
    POISON("poison_icon", false, "Poisoning"),

    //states
    STUNED("bolt_icon", false, "Stun"), //No actions
    BURDENED("bolt_icon"), //NO jump
    TIED("bolt_icon"), //NO move/jump
    DISORIENTED("bolt_icon"),// NO cast

    //speed
    SLOW("icon_slow", false, "Slow"),
    FAST("icon_fast", true, "Fast"),
//    CAST_SLOW("icon_blank"),
//    CAST_FAST("icon_blank", true),

    //weapon
    PLUS_CUT_DAMAGE("icon_plus_damage", true, "Plus to cutting damage"),
    PLUS_CRUSH_DAMAGE("icon_plus_damage", true,"Plus to crushing damage"),
    PLUS_ICE_DAMAGE("icon_plus_damage", true,"Plus to ice damage"),
    PLUS_FIRE_DAMAGE("icon_plus_damage", true,"Plus to fire damage"),

    //armor
    MINUS_CUT_DAMAGE("icon_plus_armor", true,"Armor against cutting damage"),
    MINUS_CRUSH_DAMAGE("icon_plus_armor", true,"Armor against crushing damage"),
    MINUS_ICE_DAMAGE("icon_plus_armor", true,"Armor against fire damage"),
    MINUS_FIRE_DAMAGE("icon_plus_armor", true,"Armor against ice damage"),

    //immune
    IMMUNE_CUT_DAMAGE("icon_plus_armor", true, "Immune to cutting damage"),
    IMMUNE_CRUSH_DAMAGE("icon_plus_armor", true, "Immune to srushing damage"),
    IMMUNE_FIRE_DAMAGE("icon_plus_armor", true, "Immune to fire damage"),
    IMMUNE_ICE_DAMAGE("icon_plus_armor", true, "Immune to ice damage"),
    IMMUNE_POISON_DAMAGE("icon_plus_armor", true, "Immune to poison damage"),
    IMMUNE_DAMAGE("icon_plus_armor", true, "Immune to any damage"),

    //shields
    HAS_SHIELD_AGAINST_CUT_DAMAGE("icon_plus_armor", true),
    HAS_SHIELD_AGAINST_CRUSH_DAMAGE("icon_plus_armor", true),
    HAS_SHIELD_AGAINST_ICE_DAMAGE("icon_plus_armor", true),
    HAS_SHIELD_AGAINST_FIRE_DAMAGE("icon_plus_armor", true),

    //weak
    WEEKNESS_CUT_DAMAGE("icon_minus_armor", false, "Weekness to cutting damage"),
    WEEKNESS_CRUSH_DAMAGE("icon_minus_armor", false, "Weekness to crushing damage"),
    WEEKNESS_FIRE_DAMAGE("icon_minus_armor", false, "Weekness to fire damage"),
    WEEKNESS_ICE_DAMAGE("icon_minus_armor", false, "Weekness to ice damage"),

    MOVE_LEFT("icon_blank", false),
    MOVE_RIGHT("icon_blank", false),
    MOVE_UP("icon_blank", false),
    MOVE_DOWN("icon_blank", false),

    COVERED_BY_SHIELD("shield",true, "Covered by shield"),
    DODGE("shield_yellow", true),
    COVERED_BY_FIRE_SHIELD("shield_red",true),
    INVISIBLE("icon_cross_blue",true, "Invisible"),

    THROW_FROM_INVENTORY("icon_cross_blue",true, "Loosing something"),


    ANGRY("icon_cross_red", true, "In RAGE"),
    CHARMED("icon_cross_green", true, "Charmed"),

    NO_MASS("icon_cross_blue", false, "Has no weight"),

    HEAL("icon_cross_green", true, "Under healing"),

    AURA_DAMAGE("icon_cross_red", false, "Damaging aura"),
    AURA_HEAL("icon_cross_green", true, "Healing aura"),
    AURA_JUMP("icon_cross_yellow", true, "Jumpy");


    private String icon;
    private boolean positive;  // is it possitive effect
    private boolean type;
    private String descritption;

    EffectID(String icon) {
        this.icon = icon;
        this.positive = false;
        this.descritption = "";
    }

    EffectID(String icon, boolean positive) {
        this.icon = icon;
        this.positive = positive;
        this.descritption = "";
    }

    EffectID(String icon, boolean positive, String descritption) {
        this.icon = icon;
        this.positive = positive;
        this.descritption = descritption;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isPositive() {
        return positive;
    }

    public String getDescritption() {
        return descritption;
    }
}
