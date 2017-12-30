package com.mygdx.game3.enums;

/**
 * Created by odiachuk on 12/22/17.
 */

public enum EffectID {
    BLEEDING("icon_blank"),
    SLOW("icon_blank"),
    FAST("icon_blank"),
    IN_FIRE("fire_icon"),
    IN_ICE("ice_icon"),
    STUNED("bolt_icon"),
    DAMAGE("icon_blank");

    private String icon;

    EffectID(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }


}
