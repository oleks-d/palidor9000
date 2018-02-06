package com.mygdx.game.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by odiachuk on 1/16/18.
 */
public enum Fonts {
    INFO(Color.LIME, 0.01f),
    IMPORTANT(Color.GOLD, 0.01f),
    BAD(Color.RED, 0.01f),
    GOOD(Color.GREEN, 0.01f),
    NAMES(Color.GOLD, 0.01f),
    HEADER(Color.GREEN, 0.02f ),
    GAMEHEADER(Color.GREEN, 4f );
    private BitmapFont font;

    Fonts(Color color, float scale) {
        //load font
        font = new BitmapFont(); //buildFont("arial.ttf", 8, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*");
        font.setColor(color);
        font.getData().setScale(scale);
        font.setUseIntegerPositions(false);
    }

    public BitmapFont getFont() {
        return font;
    }


}
