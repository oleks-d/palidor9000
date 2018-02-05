package com.mygdx.game.enums;

/**
 * Created by odiachuk on 12/22/17.
 */

public enum EffectID {

    //damage
    CUT_DAMAGE("icon_blank"),
    CRUSH_DAMAGE("icon_blank"),

    FIRE_DAMAGE("fire_icon"),
    ICE_DAMAGE("ice_icon"),

    BLEEDING("icon_blank"),
    POISON("icon_blank"),

    //states
    STUNED("bolt_icon"), //No actions
    BURDENED("bolt_icon"), //NO jump
    TIED("bolt_icon"), //NO move/jump
    DISORIENTED("bolt_icon"),// NO cast

    //speed
    SLOW("icon_blank"),
    FAST("icon_blank", true),
    CAST_SLOW("icon_blank"),
    CAST_FAST("icon_blank", true),

    //weapon
    PLUS_CUT_DAMAGE("icon_blank", true),
    PLUS_CRUSH_DAMAGE("icon_blank", true),
    PLUS_ICE_DAMAGE("icon_blank", true),
    PLUS_FIRE_DAMAGE("icon_blank", true),

    //armor
    MINUS_CUT_DAMAGE("icon_blank", true),
    MINUS_CRUSH_DAMAGE("icon_blank", true),
    MINUS_ICE_DAMAGE("icon_blank", true),
    MINUS_FIRE_DAMAGE("icon_blank", true),

    //immune
    IMMUNE_CUT_DAMAGE("icon_blank", true),
    IMMUNE_CRUSH_DAMAGE("icon_blank", true),
    IMMUNE_FIRE_DAMAGE("icon_blank", true),
    IMMUNE_ICE_DAMAGE("icon_blank", true),

    //shields
    HAS_SHIELD_AGAINST_CUT_DAMAGE("icon_blank", true),
    HAS_SHIELD_AGAINST_CRUSH_DAMAGE("icon_blank", true),
    HAS_SHIELD_AGAINST_ICE_DAMAGE("icon_blank", true),
    HAS_SHIELD_AGAINST_FIRE_DAMAGE("icon_blank", true),

    //immune
    WEEKNESS_CUT_DAMAGE("icon_blank", false),
    WEEKNESS_CRUSH_DAMAGE("icon_blank", false),
    WEEKNESS_FIRE_DAMAGE("icon_blank", false),
    WEEKNESS_ICE_DAMAGE("icon_blank", false),
    MOVE_LEFT("icon_blank", false),
    MOVE_RIGHT("icon_blank", false),

    COVERED_BY_SHIELD("shield",false),
    DODGE("shield_yellow", false),
    COVERED_BY_FIRE_SHIELD("shield_red",false),
    INVISIBLE("icon_cross_blue",false),

    THROW_FROM_INVENTORY("icon_cross_blue",false),
    ANGRY("icon_cross_red", false);

    private String icon;
    private boolean positive;  // is it possitive effect
    private boolean type;

    EffectID(String icon) {
        this.icon = icon;
        this.positive = false;
    }

    EffectID(String icon, boolean positive) {
        this.icon = icon;
        this.positive = positive;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isPositive() {
        return positive;
    }
}
