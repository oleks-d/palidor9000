package com.mygdx.game.enums;

import com.mygdx.game.PalidorGame;

/**
 * Created by odiachuk on 12/22/17.
 */
public enum ActivityAreaType {
    ARROW("aim_left",2f,32,32),
    BOX("aim_left",0.2f,96,48),
    SPRAY("aim_left",0.5f,32,32),
    SELF("castbar",0f),
    BIGBOX("aim_left",0.2f),
    BOOM("aim_left",1 );
    private String aimIcon;
    private float liveTime;
    private int width;
    private int higth;


    ActivityAreaType(String aimIcon, float liveTime) {
        this(aimIcon, liveTime, PalidorGame.TILE_SIZE, PalidorGame.TILE_SIZE);
    }


    ActivityAreaType(String aimIcon, float liveTime, int width, int higth) {
        this.aimIcon = aimIcon;
        this.liveTime = liveTime;
        this.width=width;
        this.higth=higth;
    }

    public String getAimIcon() {
        return aimIcon;
    }


    public float getLiveTime() {
        return liveTime;
    }

    public int getWidth() {
        return width;
    }

    public int getHigth() {
        return higth;
    }
}
