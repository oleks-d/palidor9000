package com.mygdx.game.enums;

import com.mygdx.game.PalidorGame;

/**
 * Created by odiachuk on 12/22/17.
 */
public enum ActivityAreaType {
    ARROW(2f,32,32),
    BOX(0.2f,96,48),
    SPRAY(0.5f,192,48),
    SELF(0f),
    BIGBOX(0.2f),
    BOOM(1 );
    private String aimIcon;
    private float liveTime;
    private int width;
    private int higth;


    ActivityAreaType(float liveTime) {
        this(liveTime, PalidorGame.TILE_SIZE, PalidorGame.TILE_SIZE);
    }


    ActivityAreaType(float liveTime, int width, int higth) {
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
