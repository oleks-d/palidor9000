package com.mygdx.game3.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game3.HuntersGame;

import java.io.File;

/**
 * Animation creation
 */
public class AnimationHelper {

    public TextureAtlas getAtlas() {
        return atlas;
    }

    TextureAtlas atlas;

    public AnimationHelper(){
        this.atlas = new TextureAtlas(HuntersGame.SPRITES_DIR + File.separator +  "main_spreadsheet.txt") ; //"simple_sheet.txt") ;//("hero_tiles.txt");
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
        return  getAtlas().findRegion(id);
    }

    public TextureRegion getTextureRegionByIDAndIndex(String id, int index) {
        return  new TextureRegion(getAtlas().findRegion(id), index * HuntersGame.TILE_SIZE, 0 , HuntersGame.TILE_SIZE, HuntersGame.TILE_SIZE);
    }

    public Animation getAnimationByID(String region, float duration, int... indexes) {
        Animation finalAnimation = null;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int index : indexes) {
            frames.add(new TextureRegion(getAtlas().findRegion(region), index * HuntersGame.TILE_SIZE, 0, HuntersGame.TILE_SIZE, HuntersGame.TILE_SIZE));
            finalAnimation = new Animation(duration, frames);
            frames.clear();
        }
        return finalAnimation;
    }
}
