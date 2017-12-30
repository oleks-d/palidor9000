package com.mygdx.game3.sprites.creatures;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game3.HuntersGame;
import com.mygdx.game3.enums.EffectID;
import com.mygdx.game3.enums.State;
import com.mygdx.game3.sprites.creatures.Creature;
import com.mygdx.game3.sprites.creatures.Hero;
import com.mygdx.game3.stuctures.Effect;
import com.mygdx.game3.tools.EffectsHandler;

import static com.mygdx.game3.HuntersGame.PPM;
import static com.mygdx.game3.HuntersGame.WIDTH;
import static java.awt.image.ImageObserver.HEIGHT;

/**
 * Created by odiachuk on 12/23/17.
 */
public class CreatureStatus extends Sprite {

    public Creature owner;
    public Array<TextureRegion> icons;

    //Array<Integer> iconIDs;

    public TextureRegion castbar;

    public TextureRegion healthbar;


    public CreatureStatus(Creature owner) {
        super();
        // setRegion(owner.screen.animationHelper.getStandingPositionByID("enemy1"));
        //setBounds(0,0,16 / HuntersGame.PPM,16/ HuntersGame.PPM);
        this.owner = owner;
        //this.setPosition(owner.getBody().getPosition().x - getWidth()/2 , owner.getBody().getPosition().y + getHeight() /2);

        //buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Math.round(owner.getWidth()), Math.round(owner.getHeight() / 4), false);

        icons = new Array<TextureRegion>();

        update();
        updatePosition();

        //healthbar = owner.screen.animationHelper.getTextureRegionByIDAndIndex("abilities", EffectsHandler.getIconForEffect(EffectID.IN_FIRE), 16);

        castbar = owner.screen.animationHelper.getTextureRegionByIDAndIndex("castbar");

    }



    public void update() {

        //setRegion(owner.screen.animationHelper.getTextureRegionByIDAndIndex("abilities", 0 ,16));
        //setPosition(owner.getBody().getPosition().x - owner.getWidth()/2 , owner.getBody().getPosition().y + owner.getHeight() /2);

        //setPosition(owner.getBody().getPosition().x - owner.getWidth()/2 , owner.getBody().getPosition().y + owner.getHeight() /2);
        icons.clear();
        for (Effect effect : owner.activeEffects) {
            icons.add(owner.screen.animationHelper.getTextureRegionByIDAndIndex(effect.id.getIcon()));
        }

    }

    public void updatePosition(){
        setPosition(owner.getBody().getPosition().x - owner.getWidth()/2 , owner.getBody().getPosition().y + owner.getHeight() /2);
    }

    @Override
    public void draw(Batch batch) {
        if(!owner.destroyed) {
            //super.draw(batch);
            for (int i = 0; i < icons.size; i++)
                batch.draw(icons.get(i), getX() + i * HuntersGame.TILE_SIZE / 2 / PPM, getY(), HuntersGame.TILE_SIZE / 2 / PPM, HuntersGame.TILE_SIZE / 2 / PPM);

            owner.screen.font.draw(batch, String.valueOf(owner.stats.health.current), getX(), getY()  + HuntersGame.TILE_SIZE/PPM);
            //batch.draw(healthbar, getX() + 8 / PPM, getY() + 8 / PPM, (60*(owner.stats.health.current/owner.stats.health.base )) / PPM, 8 / PPM);
//TODO healthbar

            if(owner.getState() == State.CASTING && owner.abilityToCastExecutionTime > 0.2) {
                owner.screen.font.draw(batch, owner.abilityToCast.toString(), getX() + 8/PPM, getY()  + HuntersGame.TILE_SIZE/PPM);
                batch.draw(castbar, getX() + 8 / PPM, getY() + 24 / PPM, (HuntersGame.TILE_SIZE / PPM) * (owner.timeSpentOnCast/ owner.abilityToCastExecutionTime), 8 / PPM);
            }
        }

    }
}
