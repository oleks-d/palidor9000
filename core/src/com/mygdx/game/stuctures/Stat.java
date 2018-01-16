package com.mygdx.game.stuctures;

/**
 * Created by odiachuk on 12/20/17.
 */
public class Stat {
    public int current;
    public int base;

    public Stat( int current, int base) {
        this.current = current;
        this.base = base;
    }

    public Stat(Stat stat) {
        this.current = stat.current;
        this.base = stat.base;
    }
}
