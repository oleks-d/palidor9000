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

    public TextureRegion hiddenMark;
    public TextureRegion battleMark;

    private Array<String> messages;
    public double removeMessageTime;

    public CreatureStatus(Creature owner) {
        super();
        // setRegion(owner.screen.animationHelper.getStandingPositionByID("enemy1"));
        //setBounds(0,0,16 / HuntersGame.PPM,16/ HuntersGame.PPM);
        this.owner = owner;
        //this.setPosition(owner.getBody().getPosition().x - getWidth()/2 , owner.getBody().getPosition().y + getHeight() /2);

        //buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Math.round(owner.getWidth()), Math.round(owner.getHeight() / 4), false);

        messages = new Array<String>();
        removeMessageTime =0;

        icons = new Array<TextureRegion>();

        update();
        updatePosition();

        //healthbar = owner.screen.animationHelper.getTextureRegionByIDAndIndex("abilities", EffectsHandler.getIconForEffect(EffectID.IN_FIRE), 16);

        castbar = owner.screen.animationHelper.getTextureRegionByIDAndIndex("castbar");

        healthbar = owner.screen.animationHelper.getTextureRegionByIDAndIndex("castbar2");

        hiddenMark = owner.screen.animationHelper.getTextureRegionByIDAndIndex("dialog2");

        battleMark = owner.screen.animationHelper.getTextureRegionByIDAndIndex("dialog2");

    }


    public void update() {

        //setRegion(owner.screen.animationHelper.getTextureRegionByIDAndIndex("abilities", 0 ,16));
        //setPosition(owner.getBody().getPosition().x - owner.getWidth()/2 , owner.getBody().getPosition().y + owner.getHeight() /2);

        //setPosition(owner.getBody().getPosition().x - owner.getWidth()/2 , owner.getBody().getPosition().y + owner.getHeight() /2);
        icons.clear();
        for (Effect effect : owner.activeEffects) {
            if(effect.duration != 0) // do not show constant effects
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
            int i;
            for (i = 0; i < icons.size; i++)
                batch.draw(icons.get(i), getX() + i * HuntersGame.TILE_SIZE / 2 / PPM, getY(), HuntersGame.TILE_SIZE / 2 / PPM, HuntersGame.TILE_SIZE / 2 / PPM);

            if(owner.isHidden()){
                batch.draw(hiddenMark, getX() + i * HuntersGame.TILE_SIZE / 2 / PPM, getY(), HuntersGame.TILE_SIZE / 2 / PPM, HuntersGame.TILE_SIZE / 2 / PPM);
            }

            if(owner.IN_BATTLE){
                batch.draw(battleMark, getX() + i * HuntersGame.TILE_SIZE / 2 / PPM, getY(), HuntersGame.TILE_SIZE / 2 / PPM, HuntersGame.TILE_SIZE / 2 / PPM);
            }

            for(int j = 0; j<messages.size+0; j++){
                owner.screen.shadyfont.draw(batch, messages.get(j), getX(), getY()  + HuntersGame.TILE_SIZE/PPM + (j + 2)*20/PPM);
            }

            owner.screen.font.draw(batch, String.valueOf(owner.stats.health.current), getX(), getY() + HuntersGame.TILE_SIZE  / PPM);
            owner.screen.font.draw(batch, owner.name, getX() + HuntersGame.TILE_SIZE / 2 /PPM, getY() + HuntersGame.TILE_SIZE  / PPM);
            //batch.draw(healthbar, getX() + 8 / PPM, getY() + 8 / PPM, (60*(owner.stats.health.current/owner.stats.health.base )) / PPM, 8 / PPM);
//TODO healthbar

            if(owner.abilityToCastExecutionTime > 0.2) {
                batch.draw(castbar,                                             getX()-HuntersGame.TILE_SIZE/PPM,                             getY() + 2*HuntersGame.TILE_SIZE/PPM, (2*HuntersGame.TILE_SIZE / PPM) * (owner.timeSpentOnCast/ owner.abilityToCastExecutionTime), 16 / PPM);
                batch.draw(healthbar,                                             getX()- HuntersGame.TILE_SIZE/PPM,                             getY() + 2*HuntersGame.TILE_SIZE/PPM, (2*HuntersGame.TILE_SIZE / PPM), 16 / PPM);
                owner.screen.font.draw(batch, owner.abilityToCast.toString(),   getX() - HuntersGame.TILE_SIZE/PPM, getY()  + 2*HuntersGame.TILE_SIZE/PPM);
            }

        }

    }

    public void addMessage(String s, double removeMessageTime) {
        messages.add(s);
        this.removeMessageTime = removeMessageTime;
        if(messages.size > 1)
            removeMessage();
    }

    public void removeMessage() {
        //removeMessageTime = 0;
        if(messages.size > 0)
            messages.removeIndex(0);
    }
}
