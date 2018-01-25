package com.mygdx.game.dialogs;

/**
 * Created by odiachuk on 1/24/18.
 */
public class DialogAnswer {
    String text;
    String condition;
    String process;

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    int next;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCondition() {
        return condition==null?"":condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getProcess() {
        return process==null?"":process;
    }

    public void setProcess(String process) {
        this.process = process;
    }


}
