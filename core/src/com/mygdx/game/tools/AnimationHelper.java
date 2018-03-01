package com.mygdx.game.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;

import java.io.File;

/**
 * Animation creation
 */
public class AnimationHelper {

    public Animation greenAuraAnimation;
    public Animation yellowAuraAnimation;
    public Animation redAuraAnimation;

    public TextureAtlas getAtlas() {
        return atlas;
    }

    TextureAtlas atlas;

    public AnimationHelper(){
        this.atlas = new TextureAtlas(PalidorGame.SPRITES_DIR + File.separator +  "main_sheet.txt") ; //"simple_sheet.txt") ;//("hero_tiles.txt");
    }

//        new TextureRegion(getTexture(),0, 0, 32, 32);
//        frames.add(new TextureRegion(getTexture(),0, 0, 32, 32));
//        frames.add(new TextureRegion(getTexture(),81, 0, 39, 38));
//        frames.add(new TextureRegion(getTexture(),121, 0, 39, 38));
//
//        runAnimation = new Animation(0.1f, frames);
//        frames.clear();
//
//        //frames.add(new TextureRegion(getTexture(),0, 0, 39, 38));
//        frames.add(new TextureRegion(getTexture(),39, 0, 43, 37));
//
//        kickAnimation = new Animation(0.1f, frames);
//
//        frames.clear();



    public TextureRegion getTextureRegionByIDAndIndex(String id) {
        return  new TextureRegion(getAtlas().findRegion(id));
    }

    public TextureRegion getTextureRegionByIDAndIndex(String id, int index) {
        return  new TextureRegion(getAtlas().findRegion(id), index * PalidorGame.TILE_SIZE, 0 , PalidorGame.TILE_SIZE, PalidorGame.TILE_SIZE);
    }

    public Animation getAnimationByID(String region, float duration, int... indexes) {
        Animation finalAnimation = null;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int index : indexes) {
            frames.add(new TextureRegion(getAtlas().findRegion(region), index * PalidorGame.TILE_SIZE, 0, PalidorGame.TILE_SIZE, PalidorGame.TILE_SIZE));
            finalAnimation = new Animation(duration, frames);
        }
        frames.clear();
        return finalAnimation;
    }

    public Animation getAnimationByID(String region, int tileWidth, int tileHigth,  float duration, int... indexes) {
        Animation finalAnimation = null;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int index : indexes) {
            frames.add(new TextureRegion(getAtlas().findRegion(region), index * tileWidth, 0, tileWidth, tileHigth));
            finalAnimation = new Animation(duration, frames);
        }
        frames.clear();
        return finalAnimation;
    }
}
