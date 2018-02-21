package com.mygdx.game.enums;

import com.mygdx.game.PalidorGame;

/**
 * Created by odiachuk on 12/22/17.
 */
public enum ActivityAreaType {
    ARROW(1f,32,32),
    BOX(0.2f,64,92),
    SPRAY(1f,64,48),
    SELF(0f),
    BOOM(1 ,64,48),
    ROCK(1, 32,32),
    ROCK_BOOM(1 ,64,48);
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
