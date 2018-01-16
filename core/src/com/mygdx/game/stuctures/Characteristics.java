package com.mygdx.game.stuctures;

/**
 * Created by odiachuk on 12/22/17.
 */
public class Characteristics {
    public Stat STR;
    public Stat AGI;
    public Stat CHAR;
    public Stat INT;

    public Characteristics(Integer health, Integer speed, Integer jumphight) {
        this.health = new Stat(health,health);
        this.speed = new Stat(speed,speed);
        this.jumphight = new Stat(jumphight,jumphight);
    }

    public Stat getSTR() {
        return STR;
    }

    public void setSTR(Stat STR) {
        this.STR = STR;
    }

    public Stat getAGI() {
        return AGI;
    }

    public void setAGI(Stat AGI) {
        this.AGI = AGI;
    }

    public Stat getCHAR() {
        return CHAR;
    }

    public void setCHAR(Stat CHAR) {
        this.CHAR = CHAR;
    }

    public Stat getINT() {
        return INT;
    }

    public void setINT(Stat INT) {
        this.INT = INT;
    }

    public void setHealth(Stat health) {
        this.health = health;
    }

    public Stat getSpeed() {
        return speed;
    }

    public void setSpeed(Stat speed) {
        this.speed = speed;
    }

    public Stat getJumphight() {
        return jumphight;
    }

    public void setJumphight(Stat jumphight) {
        this.jumphight = jumphight;
    }

    public Stat health;
    public Stat speed;
    public Stat jumphight;


    public Characteristics(Stat STR, Stat AGI, Stat CHAR, Stat INT, Stat health, Stat speed, Stat jumphight) {
        this.STR = STR;
        this.AGI = AGI;
        this.CHAR = CHAR;
        this.INT = INT;
        this.health = health;
        this.speed = speed;
        this.jumphight = jumphight;
    }

    public Characteristics(Stat health, Stat speed, Stat jumphight) {
        this.health = health;
        this.speed = speed;
        this.jumphight = jumphight;
    }

    public Characteristics(Characteristics stats) {
        this.health = new Stat(stats.getHealth());
        this.jumphight = new Stat(stats.getJumphight());
        this.speed = new Stat(stats.getSpeed());
    }

    public Stat getHealth() {
        return health;
    }
}
