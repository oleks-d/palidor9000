package com.mygdx.game.tools;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by odiachuk on 1/16/18.
 */
public class StatusMessage {
    String message;
    BitmapFont font;


    public StatusMessage(String message, BitmapFont font) {
        this.message = message;
        this.font = font;
    }

    public BitmapFont getFont() {
        return font;
    }

    public String getMessage() {
        return message;
    }
}
