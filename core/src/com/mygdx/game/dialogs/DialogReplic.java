package com.mygdx.game.dialogs;

import com.badlogic.gdx.utils.Array;

/**
 * Created by odiachuk on 1/24/18.
 */
public class DialogReplic {
    String text;
    private int ID;
    Array<DialogAnswer> answers;
    String condition;

    public String getCondition() {
        return condition==null?"":condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public DialogReplic() {
        answers = new Array<DialogAnswer>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Array<DialogAnswer> getAnswers() {
        return answers;
    }

    public void addAnswer(DialogAnswer answer) {
        this.answers.add(answer);
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
