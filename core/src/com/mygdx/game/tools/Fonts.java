package com.mygdx.game.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.PalidorGame;

/**
 * Created by odiachuk on 1/16/18.
 */
public enum Fonts {
    INFO(Color.LIME, 0.015f),
    IMPORTANT(Color.GOLD, 0.015f),
    BAD(Color.RED, 0.015f),
    GOOD(Color.GREEN, 0.015f),
    NAMES(Color.GOLD, 0.015f),
    HEADER(Color.GREEN, 0.015f ),

    DIALOG_REPLIC(Color.BLACK, 1.5f),

    GAMEMENUITEM(Color.GREEN, 1f ),
    GAMEHEADER(Color.GREEN, 2f ),
    GAMEMENUHEADER(Color.BLUE, 1.5f ),

    TESTFF(Color.DARK_GRAY, 0.015f);



    private BitmapFont font;

    Fonts(Color color, float scale) {
        if (Color.DARK_GRAY == color) {
            font = PalidorGame.asm.get("skin/font.fnt");
            //font.setColor(1,1,1,0.5f);
            font.setColor(color);
    } else {
            //load font
            font = new BitmapFont(); //buildFont("arial.ttf", 8, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*");
            font.setColor(color);
        }
        font.getData().setScale(scale);
        font.setUseIntegerPositions(false);
    }

    public BitmapFont getFont() {
        return font;
    }


}
