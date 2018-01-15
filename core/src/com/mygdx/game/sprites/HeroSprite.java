package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by odiachuk on 12/15/17.
 */
public class HeroSprite {

    public static final int gravity = -5;
    public static final int airpressure = 0;

    public static final int WIDTH = 40;
    public static final int HIGTH = 40;

    Vector3 position;
    Vector3 velocity;

    Texture image;

    public boolean isOnAGround = false;



    public HeroSprite(int x, int y){
        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);
        image = new Texture("hero.png");

    }

    public void update(float dt){
        velocity.add(0,gravity,0);
        velocity.scl(dt);
        position.add(velocity.x,velocity.y,0);
        velocity.scl(1/dt); // TODO ?

        if (position.y<0) {
            position.y = 0;
            velocity.set(0,0,0);
        }

        if (position.y > 1){
            velocity.add(0,gravity,0);
        }
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

    public void jump(){
        if (velocity.y == 0) {
            velocity.y = 500;
        }
    }

    public void move(int speed){
            velocity.x = speed;
    }

    public boolean collides(Rectangle rect){
        return rect.overlaps(getBounds());
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x,position.y,position.x+WIDTH,position.y+HIGTH);

    }

}
