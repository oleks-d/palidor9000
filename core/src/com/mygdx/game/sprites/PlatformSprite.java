package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by odiachuk on 12/15/17.
 */
public class PlatformSprite {

    public static final int WIDTH = 128;
    public static final int HIGTH = 30;

    Vector3 position;

    Texture image;

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    Rectangle bounds;

    public PlatformSprite(int x, int y){
        position = new Vector3(x,y,0);
        image = new Texture("platform_long.png");
        bounds = new Rectangle(x,y,x+WIDTH,y+HIGTH);
    }

    public void update(float dt){
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }


}
