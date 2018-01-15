package com.mygdx.game3.sprites.activities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game3.HuntersGame;
import com.mygdx.game3.enums.ActivityAreaType;
import com.mygdx.game3.screens.GameScreen;
import com.mygdx.game3.sprites.creatures.Creature;
import com.mygdx.game3.stuctures.Effect;

import static com.mygdx.game3.HuntersGame.PPM;

/**
 * Created by odiachuk on 12/20/17.
 */
public class ActivityWithEffect extends Sprite {

    protected World world;
    private Body body;

    protected float stateTimer;
    float liveTime;

    protected Animation runAnimation;


    public Array<Effect> activeEffects;

    String name;

    protected boolean toDestroy;
    protected boolean destroyed;

    boolean directionRight;

    ActivityAreaType type;
    Vector2 direction;

    public Creature createdBy;
    public Array<Creature> appliedTo;

    TextureRegion region;

    public ActivityWithEffect(GameScreen screen, float x, float y, Array<Effect> activeEffects, ActivityAreaType type, Vector2 direction, String spriteRegionName){
        super(screen.animationHelper.getAtlas().findRegion(spriteRegionName));
        setBounds(0, 0, HuntersGame.TILE_SIZE/ HuntersGame.PPM, HuntersGame.TILE_SIZE/ HuntersGame.PPM);
        world = screen.world;

        appliedTo = new Array<Creature>();

        this.activeEffects = activeEffects;
        this.type = type;
        this.direction = direction;

        setPosition(x, y);
        createBody();

        directionRight = false;
        stateTimer = 0f;


        liveTime = type.getLiveTime();


        Array<TextureRegion> frames = new Array<TextureRegion>();

        runAnimation = screen.animationHelper.getAnimationByID(spriteRegionName, 0.5f, 0, 1);
        region = new TextureRegion();
    }

    void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / HuntersGame.PPM, getY()/ HuntersGame.PPM );
        bodyDef.gravityScale= 0.1f;
        if(type == ActivityAreaType.ARROW)
            bodyDef.bullet = true;
        body = world.createBody(bodyDef);

        CircleShape shape;
        FixtureDef fixtureDef;
        switch(type){
            case ARROW:
                shape = new CircleShape();
                shape.setRadius(HuntersGame.TILE_SIZE / 4 / HuntersGame.PPM );

                fixtureDef= new FixtureDef();
                fixtureDef.shape = shape;

                fixtureDef.isSensor = true;

                fixtureDef.filter.categoryBits = HuntersGame.ACTIVITY_BIT;
                fixtureDef.filter.maskBits = HuntersGame.HERO_BIT |
                        HuntersGame.OBJECT_BIT |
                        HuntersGame.CREATURE_BIT;

                body.createFixture(fixtureDef).setUserData(this);


                break;
            case SPRAY:

                shape = new CircleShape();
                shape.setRadius(HuntersGame.TILE_SIZE / 3 / HuntersGame.PPM );

                fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;

                fixtureDef.isSensor = true;

                fixtureDef.filter.categoryBits = HuntersGame.ACTIVITY_BIT;
                fixtureDef.filter.maskBits = HuntersGame.HERO_BIT |
                        HuntersGame.OBJECT_BIT |
                        HuntersGame.CREATURE_BIT;

                body.createFixture(fixtureDef).setUserData(this);
                break;
            default:
                PolygonShape box = new PolygonShape();
                box.setAsBox(getWidth()/4,getHeight()/4);

                fixtureDef = new FixtureDef();
                fixtureDef.shape = box;

                fixtureDef.isSensor = true;

                fixtureDef.filter.categoryBits = HuntersGame.ACTIVITY_BIT;
                fixtureDef.filter.maskBits = HuntersGame.HERO_BIT |
                        HuntersGame.OBJECT_BIT |
                        HuntersGame.CREATURE_BIT;

                body.createFixture(fixtureDef).setUserData(this);
        }

        body.applyLinearImpulse(direction, body.getWorldCenter(), true);

    }

    public void update(float dt){
        if (stateTimer >= liveTime)
            destroyBody();
        if(!toDestroy && !destroyed)
            updatePosition(dt);

        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void updatePosition(float dt){
        this.setPosition(body.getPosition().x - getWidth()/2 , body.getPosition().y - getHeight() /2);
        this.setRegion(getFrame(dt));

    }

    public TextureRegion getFrame(float dt) {

        if(!destroyed && !toDestroy) {
            region = (TextureRegion) runAnimation.getKeyFrame(stateTimer, true);

            if ((body.getLinearVelocity().x < 0 || !directionRight) && !region.isFlipX()) {
                //region.flip(true, false);
                directionRight = false;
            } else if ((body.getLinearVelocity().x > 0 || directionRight) && region.isFlipX()) {
                //region.flip(true, false);
                directionRight = true;
            }
        }
        stateTimer = stateTimer + dt;
        return region;
    }


    public Body getBody() {
        return body;
    }

    public void onHit() {
        switch(type) {
            case ARROW:
                destroyBody();
                break;
            case SPRAY:
                break;
            default:
                destroyBody();
        }
    }

    public void draw(Batch batch) {
        if (!destroyed)
            //super.draw(batch);
            try {
                batch.draw(region, getX(), getY(), region.getRegionWidth() / PPM / 2, region.getRegionHeight() / PPM / 2, region.getRegionWidth() / PPM, region.getRegionHeight() / PPM, 1, 1, (direction.angle()));
            } catch (Exception e) {
                System.currentTimeMillis();
            }
    }

    public void destroyBody(){
        toDestroy = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void addTargetToAlreadyProcessed(Creature target){
        appliedTo.add(target);
    }

//    public boolean isTargetWasAlreadyProcessed(Creature target){
//        for (int i = 0; i < appliedTo.size; i++){
//            if (appliedTo.get(i).getID() == target.getID())
//                return true;
//        }
//        return false;
//
//    }

    public void setCreatedBy(Creature target){
        createdBy = target;
    }

    public boolean isTargetACreator(Creature target){
        return createdBy.equals(target);
    }

    public Creature getCreator() {
        return createdBy;
    }
}
