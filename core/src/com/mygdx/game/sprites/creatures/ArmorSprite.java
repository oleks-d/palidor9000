package com.mygdx.game.sprites.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.State;

/**
 * Created by odiachuk on 1/9/18.
 */
public class ArmorSprite extends Sprite {

    public Creature owner;
    TextureRegion picture;
    public String pictureName;

    TextureRegion region;

    protected TextureRegion stand;
    protected Animation runAnimation;
    protected Animation abilityToCastAnimation;
    protected Animation jumpAnimation;

    public ArmorSprite(Creature owner) {
        super();

        this.owner = owner;
        //updatePicture();

    }

    public void update(float delta){
        updatePosition(delta);
        //setRegion(icon);

    }

    void updatePosition(float dt){
        if(pictureName != null) {
            //setPosition( owner.getBody().getPosition().x  , owner.getBody().getPosition().y);
            setPosition((owner.getBody().getPosition().x)-owner.getWidth()/2, owner.getBody().getPosition().y-owner.getHeight()/2);
            this.setRegion(getFrame(dt));
        }
        //setRotation(owner.direction.angle());

    }

    public TextureRegion getFrame(float dt) {

        State currentState = owner.getState();
        switch (currentState) {

            case RUNNING:
                region = (TextureRegion) runAnimation.getKeyFrame(owner.stateTimer, true);
                break;
            case JUMPING:
                region = (TextureRegion) jumpAnimation.getKeyFrame(owner.stateTimer);
                break;
            case CASTING:
                region = (TextureRegion)abilityToCastAnimation.getKeyFrame(owner.stateTimer, true);
                //region = (TextureRegion) castAnimation.getKeyFrame(stateTimer, true);
                break;
            case KICKING:
                region = (TextureRegion)abilityToCastAnimation.getKeyFrame(owner.stateTimer, false);
                //region = (TextureRegion) kickAnimation.getKeyFrame(stateTimer, true);
                break;
            case SHOTING:
                region = (TextureRegion)abilityToCastAnimation.getKeyFrame(owner.stateTimer, false);
                //region = (TextureRegion) shotAnimation.getKeyFrame(stateTimer, true);
                break;

            default:
                region = stand;
        }

        //if ((body.getLinearVelocity().x < 0 || !directionRight) && !region.isFlipX()){
        if (!owner.directionRight && !region.isFlipX()){
            region.flip(true, false);
            //directionRight = false;
        } else
            //if ((body.getLinearVelocity().x > 0 || directionRight) && region.isFlipX()){
            if (owner.directionRight && region.isFlipX()){
                region.flip(true,false);
                //directionRight = true;
            }

        return region;
    }

    @Override
    public void draw(Batch batch) {
        if(pictureName != null && region !=null) {
            batch.draw(region, getX(), getY(), region.getRegionWidth() / PalidorGame.PPM, region.getRegionHeight() / PalidorGame.PPM);
            //batch.draw(getTexture(), getX(),getY(),region.getRegionWidth(), region.getRegionHeight());
            //super.draw(batch);
        }
    }

    public void setPicture(String picture) {

        this.pictureName = picture;

        if(picture != null) {
            stand = owner.screen.animationHelper.getTextureRegionByIDAndIndex(pictureName, 0);
            runAnimation = owner.screen.animationHelper.getAnimationByID(pictureName, 0.2f, 1, 2);
            jumpAnimation = owner.screen.animationHelper.getAnimationByID(pictureName, 0.3f, 3);

            updatePicture();
        }

    }

    private void updatePicture() {
        picture = owner.screen.animationHelper.getTextureRegionByIDAndIndex(pictureName);
    }

    public void updateAbilityToCastAnimation() {
        if(pictureName != null && picture != null) {
            abilityToCastAnimation = owner.screen.animationHelper.getAnimationByID(pictureName, owner.abilityToCast.getCastTime(), 4, 5);
        }
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
