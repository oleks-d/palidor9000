package com.mygdx.game.sprites.creatures;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.stuctures.Effect;
import com.mygdx.game.tools.Fonts;
import com.mygdx.game.tools.StatusMessage;

import static com.mygdx.game.PalidorGame.PPM;

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

    private Array<StatusMessage> messages;
    public double removeMessageTime;

    private float timeSpentOnCast;
    private String abilityToCastName;
    private float abilityToCastExecutionTime;
    private float partOfCastbar;

    public CreatureStatus(Creature owner) {
        super();
        // setRegion(owner.screen.animationHelper.getStandingPositionByID("enemy1"));
        //setBounds(0,0,16 / PalidorGame.PPM,16/ PalidorGame.PPM);
        this.owner = owner;
        //this.setPosition(owner.getBody().getPosition().x - getWidth()/2 , owner.getBody().getPosition().y + getHeight() /2);

        //buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Math.round(owner.getWidth()), Math.round(owner.getHeight() / 4), false);

        messages = new Array<StatusMessage>();
        removeMessageTime =0;

        icons = new Array<TextureRegion>();

        update();
        updatePosition();

        //healthbar = owner.screen.animationHelper.getTextureRegionByIDAndIndex("abilities", EffectsHandler.getIconForEffect(EffectID.IN_FIRE), 16);

        castbar = owner.screen.animationHelper.getTextureRegionByIDAndIndex("castbar");

        healthbar = owner.screen.animationHelper.getTextureRegionByIDAndIndex("castbar2");

        hiddenMark = owner.screen.animationHelper.getTextureRegionByIDAndIndex("dialog2");

        battleMark = owner.screen.animationHelper.getTextureRegionByIDAndIndex("dialog2");

        resetCastbar();

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
                batch.draw(icons.get(i), getX() + i * PalidorGame.TILE_SIZE / 2 / PPM, getY(), PalidorGame.TILE_SIZE / 2 / PPM, PalidorGame.TILE_SIZE / 2 / PPM);

            if(owner.isHidden()){
                batch.draw(hiddenMark, getX() + i * PalidorGame.TILE_SIZE / 2 / PPM, getY(), PalidorGame.TILE_SIZE / 2 / PPM, PalidorGame.TILE_SIZE / 2 / PPM);
            }

            if(owner.IN_BATTLE){
                batch.draw(battleMark, getX() + i * PalidorGame.TILE_SIZE / 2 / PPM, getY(), PalidorGame.TILE_SIZE / 2 / PPM, PalidorGame.TILE_SIZE / 2 / PPM);
            }

            for(int j = 0; j<messages.size+0; j++){
                messages.get(j).getFont().draw(batch, messages.get(j).getMessage(), getX(), getY()  + PalidorGame.TILE_SIZE/PPM + (j + 2)*20/PPM);
            }

            Fonts.NAMES.getFont().draw(batch, String.valueOf(owner.stats.health.current), getX(), getY() + PalidorGame.TILE_SIZE  / PPM);
            Fonts.NAMES.getFont().draw(batch, owner.name, getX() + PalidorGame.TILE_SIZE / 2 /PPM, getY() + PalidorGame.TILE_SIZE  / PPM);
            //batch.draw(healthbar, getX() + 8 / PPM, getY() + 8 / PPM, (60*(owner.stats.health.current/owner.stats.health.base )) / PPM, 8 / PPM);
//TODO healthbar

            if(owner.abilityToCastExecutionTime > 0.2 || timeSpentOnCast > 0) {
                partOfCastbar = (((timeSpentOnCast>0 ? timeSpentOnCast : owner.timeSpentOnCast) / (abilityToCastExecutionTime > 0 ? abilityToCastExecutionTime : owner.abilityToCastExecutionTime)));
                if (partOfCastbar > 1) partOfCastbar = 1;
                batch.draw(castbar, getX()- PalidorGame.TILE_SIZE/PPM, getY() + 2* PalidorGame.TILE_SIZE/PPM, (2* PalidorGame.TILE_SIZE / PPM) * partOfCastbar, 16 / PPM);
                batch.draw(healthbar, getX()- PalidorGame.TILE_SIZE/PPM, getY() + 2* PalidorGame.TILE_SIZE/PPM, (2* PalidorGame.TILE_SIZE / PPM), 16 / PPM);
                Fonts.NAMES.getFont().draw(batch, "".equals(abilityToCastName) ? owner.abilityToCast.toString() : abilityToCastName,   getX() - PalidorGame.TILE_SIZE/PPM, getY()  + 2* PalidorGame.TILE_SIZE/PPM);
            }

        }

    }

    public void addMessage(String message, double removeMessageTime, Fonts font) {
        messages.add(new StatusMessage(message, font.getFont()));
        this.removeMessageTime = removeMessageTime;
        if(messages.size > 3)
            removeMessage();
    }

    public void removeMessage() {
        //removeMessageTime = 0;
        if(messages.size > 0)
            messages.removeIndex(0);
    }


    public void updateCastBar(String name, float timeSpentOnCast, float abilityToCastExecutionTime) {
        this.timeSpentOnCast = timeSpentOnCast;
        this.abilityToCastExecutionTime = abilityToCastExecutionTime;
        this.abilityToCastName = name;
    }

    public void resetCastbar(){
        this.timeSpentOnCast = 0;
        this.abilityToCastExecutionTime = 0;
        this.abilityToCastName = "";
    }

}
