package com.mygdx.game3.sprites.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game3.HuntersGame;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game3.enums.EquipmentType;
import com.mygdx.game3.screens.GameScreen;
import com.mygdx.game3.stuctures.Effect;
import com.mygdx.game3.stuctures.descriptions.ItemDescription;

import java.io.File;

public class GameItem extends Sprite {
    protected GameScreen screen;
    protected World world;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    Texture image;

    public String itemname;
    public String itemdescription;
    int itemvalue;

    Array<Effect> effects;
    EquipmentType type;
    private boolean usable;
    private String icon;

    public GameItem(GameScreen screen, ItemDescription description){
        super();
        this.screen = screen;
        this.world = screen.world;

        this.icon = description.image;
        setRegion(screen.animationHelper.getTextureRegionByIDAndIndex(description.image));

        itemname = description.name;
        itemdescription = description.description;
        itemvalue = description.value;

        this.type = description.type;

        this.usable = description.usable;

        this.effects = new Array<Effect>();
        for(Effect effect : description.effects)
            this.effects.add(effect);

    }

    public GameItem(GameScreen screen, float x, float y, ItemDescription description){
        super();
        this.screen = screen;
        this.world = screen.world;

        this.icon = description.image;
        setRegion(screen.animationHelper.getTextureRegionByIDAndIndex(description.image));

        itemname = description.name;
        itemdescription = description.description;
        itemvalue = description.value;

        this.type = description.type;

        this.usable = description.usable;

        this.effects = new Array<Effect>();
        for(Effect effect : description.effects)
        this.effects.add(effect);

        createBody(x, y);

    }

    public void createBody(float x, float y){

        setPosition(x, y);
        setBounds(x, y, HuntersGame.TILE_SIZE / HuntersGame.PPM, HuntersGame.TILE_SIZE / HuntersGame.PPM);


        toDestroy = false;
        destroyed = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / HuntersGame.PPM, getY()/ HuntersGame.PPM );
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(HuntersGame.TILE_SIZE / 2 / HuntersGame.PPM );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = HuntersGame.ITEM_BIT;
        fixtureDef.filter.maskBits = HuntersGame.CREATURE_BIT | HuntersGame.HERO_BIT | HuntersGame.ACTIVITY_BIT | HuntersGame.OBJECT_BIT;


        body.createFixture(fixtureDef).setUserData(this);

        setPosition(body.getPosition().x - getWidth()/2 , body.getPosition().y - getHeight() /2);
    }

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if(!destroyed) {
            this.setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            //this.setRegion(getFrame(dt));
            super.draw(batch);
        }
    }

    public void destroyBody(){
        toDestroy = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Array<Effect> getEffects() {
        return effects;
    }

    public EquipmentType getType() {
        return type;
    }

    public boolean isUsable() {
        return usable;
    }

    public String getIcon() {
        return icon;
    }
}
