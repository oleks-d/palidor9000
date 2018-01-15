package com.mygdx.game3.enums;

/**
 * Created by odiachuk on 12/22/17.
 */
public enum ActivityAreaType {
    ARROW("aim_left",0.5f),
    BOX("aim_left",0.1f),
    SPRAY("aim_left",0.5f),
    SELF("castbar",0f);
    private String aimIcon;
    private float liveTime;


    ActivityAreaType(String aimIcon, float liveTime) {
        this.aimIcon = aimIcon;
        this.liveTime = liveTime;
    }

    public String getAimIcon() {
        return aimIcon;
    }


    public float getLiveTime() {
        return liveTime;
    }
}
