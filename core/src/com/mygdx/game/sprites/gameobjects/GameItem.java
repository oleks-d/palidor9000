package com.mygdx.game.sprites.gameobjects;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.enums.EquipmentType;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.stuctures.Effect;
import com.mygdx.game.stuctures.descriptions.ItemDescription;

public class GameItem extends Sprite {
    protected GameScreen screen;
    protected World world;
    public boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public String itemname;
    public String itemdescription;
    int itemvalue;

    Array<Effect> effects;
    EquipmentType type;
    private boolean usable;
    private String icon;
    public String id;

    public GameItem(GameScreen screen, ItemDescription description){
        super();

        this.screen = screen;
        this.id = description.id;
        this.icon = description.image;
        this.itemname = description.name;
        this.itemdescription = description.description;
        this.itemvalue = description.value;
        this.type = description.type;
        this.usable = description.usable;

        //get all effects
        this.effects = new Array<Effect>();
        for(Effect effect : description.effects)
            this.effects.add(effect);

        setRegion(screen.animationHelper.getTextureRegionByIDAndIndex(description.image));

    }

    public GameItem(GameScreen screen, float x, float y, ItemDescription description){
        this(screen,description);

        createBody(x, y);
    }

    public void createBody(float x, float y){
        this.world = screen.world;
        setPosition(x, y);
        setBounds(x, y, PalidorGame.TILE_SIZE / PalidorGame.PPM, PalidorGame.TILE_SIZE / PalidorGame.PPM);

        toDestroy = false;
        destroyed = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(getX() / PalidorGame.PPM, getY()/ PalidorGame.PPM );
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(PalidorGame.TILE_SIZE / 2 / PalidorGame.PPM );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = PalidorGame.ITEM_BIT;
        fixtureDef.filter.maskBits = PalidorGame.CREATURE_BIT | PalidorGame.OBJECT_BIT;


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

    @Override
    public String toString() {
        return id;
    }
}
