package com.mygdx.game3.sprites.creatures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game3.enums.ActivityAreaType;

import static com.mygdx.game3.HuntersGame.PPM;

/**
 * Created by odiachuk on 1/9/18.
 */
public class CreatureAim extends Sprite {

    public Creature owner;
    TextureRegion icon;

    public CreatureAim(Creature owner) {
        super();

        this.owner = owner;

        update(ActivityAreaType.BOX);
        updatePosition();
    }

    public void update(ActivityAreaType type){
        icon = owner.screen.animationHelper.getTextureRegionByIDAndIndex(type.getAimIcon());
        //setRegion(icon);

    }

    void updatePosition(){
        setPosition( (owner.getBody().getPosition().x  - owner.getWidth()/4 - 2 * owner.getWidth() * owner.direction.x)  , (owner.getBody().getPosition().y  - owner.getHeight()/4 +  2 * owner.getHeight()  * owner.direction.y) );
        //setRotation(owner.direction.angle());

    }

    @Override
    public void draw(Batch batch) {
        //super.draw(batch);
        batch.draw(icon, getX(), getY(), icon.getRegionWidth()/PPM/2,icon.getRegionHeight()/PPM/2, icon.getRegionWidth()/PPM, icon.getRegionHeight()/PPM, 1, 1, -owner.direction.angle());
        //batch.draw(icon, getX(), getY(),  icon.getRegionWidth()/PPM, icon.getRegionHeight()/PPM);
    }


    //    public void rotatePoint(float pointX, float pointY, float centerX, float centerY, double angle){
//
//        angle = (angle ) * (Math.PI/180); // Convert to radians
//
//        double rotatedX = Math.cos(angle) * (pointX - centerX) - Math.sin(angle) * (pointY-centerY) + centerY;
//
//        double rotatedY = Math.sin(angle) * (pointX - centerX) + Math.cos(angle) * (pointY - centerY) + centerY;
//
//        return ;
//
//    }
}
