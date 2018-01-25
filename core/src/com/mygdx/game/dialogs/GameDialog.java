package com.mygdx.game.dialogs;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by odiachuk on 1/22/18.
 */
public class GameDialog {

    int id;
    String title;
    String condition;
    HashMap<Integer, DialogReplic> replics;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCondition() {
        return condition==null?"":condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public HashMap<Integer, DialogReplic> getReplic() {
        return replics;
    }

    public void addReplic(Integer id, DialogReplic replic) {
        this.replics.put(id, replic);
    }

    public GameDialog(int id) {
        this.id = id;
        this.replics = new HashMap<Integer, DialogReplic>();
        this.condition = null;
    }
}
