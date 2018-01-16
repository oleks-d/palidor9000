package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * Created by odiachuk on 12/27/17.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(String title, Skin skin) {
        super(title, skin);

        button("Yes", "1");
        button("No", "0");
    }

    @Override
    protected void result(Object object) {
        super.result(object);
        System.out.println(object);
    }
}
