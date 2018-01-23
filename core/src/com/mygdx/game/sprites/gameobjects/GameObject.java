package com.mygdx.game.sprites.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.GameObjectType;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.stuctures.descriptions.ObjectDescription;

/**
 * Created by odiachuk on 1/23/18.
 */
public class GameObject extends Sprite {

    protected GameScreen screen;
    protected World world;
    public boolean toDestroy;
    protected boolean destroyed;
    protected Body body;


    GameObjectType type;
    private String program;
    private String icon;
    public String id;

    public Array<GameItem> items;

    public GameObject(GameScreen screen, float x, float y, ObjectDescription objectDescription, String items, String program) {

        super();

        this.screen = screen;
        this.id = objectDescription.id;
        this.icon = objectDescription.image;
        this.type = objectDescription.type;
        this.program = program;

        this.items = new Array<GameItem>();

        // add all items from inventory  -  get items from item descriptions in levelmanager
        if(items != null && !"".equals(items))
            for (String itemd : items.split(",")){
                this.items.add(new GameItem(screen, this.screen.levelmanager.ITEMS_DESCRIPTIONS.get(itemd.trim())));
            }

        setRegion(screen.animationHelper.getTextureRegionByIDAndIndex(objectDescription.image));

        createBody(x, y);
    }

    public void createBody(float x, float y){
        this.world = screen.world;
        setPosition(x, y);
        setBounds(x, y, PalidorGame.TILE_SIZE/2 / PalidorGame.PPM, PalidorGame.TILE_SIZE/2 / PalidorGame.PPM);

        toDestroy = false;
        destroyed = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(getX() / PalidorGame.PPM, getY()/ PalidorGame.PPM );
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(PalidorGame.TILE_SIZE / 4 / PalidorGame.PPM );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = PalidorGame.OBJECT_BIT;
        fixtureDef.filter.maskBits = PalidorGame.CREATURE_BIT | PalidorGame.ACTIVITY_BIT;


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

    public GameObjectType getType() {
        return type;
    }


    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return id;
    }

    public Body getBody() {
        return body;
    }

    public String getProgram() {
        return program;
    }

    public String getRequiredKey() {
        return program;
    }

    public Array<GameItem> getItems() {
        return items;
    }
}
