package com.mygdx.game.sprites.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.AbilityType;
import com.mygdx.game.enums.ActivityAreaType;
import com.mygdx.game.enums.State;

/**
 * Created by odiachuk on 1/9/18.
 */
public class WeaponSprite extends Sprite {

    private boolean isMain;
    public Creature owner;
    TextureRegion picture;
    String pictureName;
    String defaultPicture;

    float angle;
    float defaultAngle;
    float partOfTrajectory;
    float trajectoryX;
    float trajectoryY;
    float prevtrajectoryX;
    float prevtrajectoryY;

    int scale;

    float distortionX = 0;
    float distortionY;
    boolean moveDown = false;

    public boolean isMoving = false;
    boolean holding = false;

    double holdingTime = 0;
    float holdingAngle = defaultAngle;


    String lastAction = "";

    double timeToMoveWeapon;

    public WeaponSprite(Creature owner, String defaultPicture, boolean isMain) {
        super();
        this.defaultPicture = defaultPicture;
        this.owner = owner;
        this.isMain = isMain;
        resetState();

        updatePicture();
    }

    public void update(float delta){

        if(owner.getState() == State.STANDING){
            if(timeToMoveWeapon <= owner.existingTime) {
                moveDown = !moveDown;
                    ///moveDown = owner.screen.randomizer.nextBoolean();
                    timeToMoveWeapon = owner.existingTime + 0.5;
                } else {
                //distortionX = (owner.screen.randomizer.nextBoolean() ? 0.05f:-0.05f);  //TODO distortion

                if (isMain)
                    distortionY = distortionY + (moveDown ? 0.005f:-0.005f);
                else
                    distortionY = distortionY + (moveDown ? -0.005f:0.005f);

                distortionX = 0;

            }
        }

        if(owner.getState() == State.RUNNING) {
            if (timeToMoveWeapon <= owner.existingTime) {
                moveDown = !moveDown;
                ///moveDown = owner.screen.randomizer.nextBoolean();
                timeToMoveWeapon = owner.existingTime + 0.2;
            } else {
                //distortionX = (owner.screen.randomizer.nextBoolean() ? 0.05f:-0.05f);  //TODO distortion

                if (isMain)
                    distortionX = distortionX + (moveDown ? 0.01f : -0.01f);
                else
                    distortionX = distortionX + (moveDown ? -0.01f : 0.01f);

            }
            distortionY = 0;
        }

                if (isMoving && owner.timeSpentOnCast != 0 && owner.abilityToCast != AbilityID.NONE && !owner.abilityToCast.getType().equals(AbilityType.BUFF)) {

                    partOfTrajectory = (((owner.timeSpentOnCast > 0 ? owner.timeSpentOnCast : owner.timeSpentOnCast) / (owner.abilityToCastExecutionTime > 0 ? owner.abilityToCastExecutionTime : owner.abilityToCastExecutionTime)));

                    switch (owner.abilityToCast) {

                        case ANIMAL_POKE:
                        case PUNCH:
                            angle = 0;
                            trajectoryX = (owner.directionRight ? -partOfTrajectory : partOfTrajectory) * owner.getWidth();
                            //trajectoryY = partOfTrajectory * owner.getHeight() / 2;
                            scale = 2;
                            holdingAngle = 1;
                            break;
                        case SWORD_SWING:
                        case SWORD_SMASH:
                        case ANIMAL_CLAW:
                        case UPPERPUNCH:
                            angle = -partOfTrajectory * 160;
                            trajectoryX = (owner.directionRight ? -partOfTrajectory : partOfTrajectory) * owner.getWidth();
                            if(!picture.isFlipY()){
                                picture.flip(false,true);
                            }
                            trajectoryY = - partOfTrajectory * owner.getHeight() / 2;
                            scale = 2;
                            holdingAngle = 60;
                            break;
                        case ANIMAL_PUNCH:
                        case HUMMER_SWING:
                        case HUMMER_SMASH:
                            angle = partOfTrajectory * 160;
                            trajectoryX = (owner.directionRight ? -partOfTrajectory : partOfTrajectory) * owner.getWidth();
                            trajectoryY = partOfTrajectory * owner.getHeight() / 2;
//                            if (partOfTrajectory < 0.8) { //first part
//                                angle = partOfTrajectory * 180;
//                                trajectoryX = (owner.directionRight ? -partOfTrajectory : partOfTrajectory) * owner.getWidth();
//                                trajectoryY = partOfTrajectory * owner.getHeight() / 2;
//                            } else { //second part
//                                angle = -60;
//                                trajectoryX = 0.1f;
//                                trajectoryY = -owner.getHeight()/ 2;
////                                trajectoryX = (owner.directionRight ? partOfTrajectory - 1 : 1 - partOfTrajectory) * owner.getWidth();
////                                trajectoryY = (1 - partOfTrajectory) * owner.getHeight() / 2;
////
//                            }
                            scale = 2;

//                            holding = true;
//
//                            if("".equals(lastAction)){
//                                lastAction = "swing";
//                            }
                            holdingAngle = -60;
                            break;

                        case SLING_SHOT:
                        case LONGBOW_SHOT:
                        case TRIPLE_SHOT:
                        case DITRUCTING_SHOT:
                            if (partOfTrajectory < 0.7) { //first part
                                //angle = partOfTrajectory * 180;
                                trajectoryX = (owner.directionRight ? -partOfTrajectory : partOfTrajectory) * owner.getWidth();
                                trajectoryY = partOfTrajectory * owner.getHeight() / 2;
                            } else { //second part
                                //angle = 180 - partOfTrajectory * 180;
                                trajectoryX = (owner.directionRight ? partOfTrajectory - 1 : 1 - partOfTrajectory) * owner.getWidth();
                                trajectoryY = (1 - partOfTrajectory) * owner.getHeight() / 2;
                            }
                            break;
                        case COVER:
                            if (!isMain){
                                //trajectoryX = (owner.directionRight ? partOfTrajectory : -partOfTrajectory) * owner.getWidth() / 2;
                                trajectoryY = partOfTrajectory * owner.getHeight() / 2;
                                scale = 2;
                                holding = true;
                            }
                            break;
                        default:
                            //partOfTrajectory = (((owner.timeSpentOnCast > 0 ? owner.timeSpentOnCast : owner.timeSpentOnCast) / (owner.abilityToCastExecutionTime > 0 ? owner.abilityToCastExecutionTime : owner.abilityToCastExecutionTime)));
                            angle = partOfTrajectory * 160;
                            trajectoryX = (owner.directionRight ? -partOfTrajectory : partOfTrajectory) * owner.getWidth();
                            trajectoryY = partOfTrajectory * owner.getHeight() / 2;
                    }
                } else {

                    if(holdingAngle != 0) {
                        if (holdingTime == 0) {
                            holding = true;
                            angle = holdingAngle;
                            holdingTime = owner.existingTime + 0.5;
                            if(picture.isFlipY()) { // swing
                                picture.flip(false, true);
                                trajectoryX = prevtrajectoryX;
                                trajectoryY = prevtrajectoryY;
                            }else{ //smash
                                if(holdingAngle != 1){
                                    trajectoryX = -owner.getWidth() / 2;
                                    trajectoryY = -owner.getHeight() / 2;
                                }
                            }
                        } else {
                            if(holdingTime <= owner.existingTime){
                                holding = false;
                                holdingAngle = 0;
                                holdingTime = 0;
                            }
                        }
                    }

                        if (!holding) {
                            if(picture.isFlipY()){
                                picture.flip(false,true);
                            }
                            scale = 1;
                            angle = defaultAngle;
                            trajectoryX = 0;
                            trajectoryY = 0;
//                            prevtrajectoryX = trajectoryX;
//                            prevtrajectoryY = trajectoryY;
                        }
                }


        updatePosition();
        //setRegion(icon);

    }

    void updatePosition(){
            //setPosition(owner.getBody().getPosition().x - (owner.directionRight ? owner.getWidth() : owner.getWidth()), owner.getBody().getPosition().y - owner.getHeight() / 3);
        setPosition(owner.getBody().getPosition().x - (owner.directionRight ?  0 : owner.getWidth()) + (owner.directionRight ?  distortionX : -distortionX) + trajectoryX, owner.getBody().getPosition().y - (isMain?owner.getHeight() / 2/3:owner.getHeight() / 3) + distortionY + trajectoryY);


        if(!owner.directionRight && !picture.isFlipX()){
            picture.flip(true,false);
        } else
            if (owner.directionRight && picture.isFlipX()){
                picture.flip(true,false);
            }

    }

    @Override
    public void draw(Batch batch) {
        //super.draw(batch);
        //Gdx.app.log(this + " " + getX()+"", getY()+"");
  //      batch.draw(picture, getX(), getY(), picture.getRegionWidth()/ PalidorGame.PPM/2,picture.getRegionHeight()/ PalidorGame.PPM/2, picture.getRegionWidth()/ PalidorGame.PPM, picture.getRegionHeight()/ PalidorGame.PPM, scale, scale, (owner.directionRight?1:-1)*angle);
        batch.draw(picture, getX(), getY(), picture.getRegionWidth()/ PalidorGame.PPM/2,picture.getRegionHeight()/ PalidorGame.PPM/2, picture.getRegionWidth()/ PalidorGame.PPM, picture.getRegionHeight()/ PalidorGame.PPM, scale, scale, (owner.directionRight?1:-1)*angle);
        //batch.draw(icon, getX(), getY(),  icon.getRegionWidth()/PPM, icon.getRegionHeight()/PPM);
    }

    public void setPicture(String picture) {
        this.pictureName = picture;
        updatePicture();
    }

    private void updatePicture() {
        if(pictureName == null)
            pictureName = defaultPicture;
        picture = owner.screen.animationHelper.getTextureRegionByIDAndIndex(pictureName);
    }

    public void resetState() {
        if(!holding) {
            angle = 15;
            trajectoryX = 0;
            trajectoryY = 0;
            distortionY = 0;
            if(!isMain) {
                distortionX = -0.1f;
                defaultAngle = 15;
            }else {
                defaultAngle = 25;
            }

            prevtrajectoryX = trajectoryX;
            prevtrajectoryY = trajectoryY;

            isMoving = false;
        }
    }

    public void setDistortionX(float val){
        distortionX = val;
    }
}
