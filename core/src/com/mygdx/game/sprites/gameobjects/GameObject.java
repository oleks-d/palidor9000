package com.mygdx.game.sprites.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

    public char[] steps = null;
    char currentStep;
    double existingTime;
    double timeForNextStep;
    double speedInCurrentStep;
    int currentStepNumber;
Vector2 direction;

    public GameObject(GameScreen screen, Rectangle rectangle, ObjectDescription objectDescription, String items, String program) {

        super();

        this.screen = screen;
        this.id = objectDescription.id;
        this.icon = objectDescription.image;
        this.type = objectDescription.type;
        this.program = program;

        if(type != GameObjectType.DOOR && type != GameObjectType.CHEST && program != null && !program.equals(""))
            steps = program.toCharArray();

        currentStepNumber = 0;

        direction = new Vector2(0,0);

        this.items = new Array<GameItem>();

        // add all items from inventory  -  get items from item descriptions in levelmanager
        if(items != null && !"".equals(items))
            for (String itemd : items.split(",")){
                this.items.add(new GameItem(screen, this.screen.levelmanager.ITEMS_DESCRIPTIONS.get(itemd.trim())));
            }

        setRegion(screen.animationHelper.getTextureRegionByIDAndIndex(objectDescription.image));

        createBody(rectangle);

        if(steps != null)
            nextStep(0);
    }

    public void createBody(Rectangle rect){
        this.world = screen.world;
        setPosition(rect.x, rect.y);
        setBounds(rect.x, rect.y, rect.getWidth() / PalidorGame.PPM, rect.getHeight() / PalidorGame.PPM);

        toDestroy = false;
        destroyed = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / PalidorGame.PPM, (rect.getY() + rect.getHeight() / 2) / PalidorGame.PPM);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth() / 2 / PalidorGame.PPM, rect.getHeight() / 2 / PalidorGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 2;
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

        if(steps != null) {
            existingTime = existingTime + dt;

            if (existingTime >= timeForNextStep) {
                nextStep(dt);
                currentStepNumber++;
            }

            body.setLinearVelocity(direction);
        }

    }

    private void nextStep(float dt) {
        if(currentStepNumber>=steps.length)
            currentStepNumber = 0;
        currentStep = steps[currentStepNumber];
        if(currentStep == 'u')
            direction.set(0,1);
        if(currentStep == 'd')
            direction.set(0,-1);
        if(currentStep == 'r')
            direction.set(1,0);
        if(currentStep == 'l')
            direction.set(-1,0);

        timeForNextStep = existingTime + 1f;

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
        return program==null?"":program;
    }

    public String getRequiredKey() {
        return program;
    }

    public Array<GameItem> getItems() {
        return items;
    }
}
