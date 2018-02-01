package com.mygdx.game.sprites.activities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.ActivityAreaType;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.stuctures.Effect;

import static com.mygdx.game.PalidorGame.PPM;

/**
 * Created by odiachuk on 12/20/17.
 */
public class ActivityWithEffect extends Sprite {

    private final GameScreen screen;
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

    com.mygdx.game.enums.ActivityAreaType type;
    public Vector2 direction;

    public Creature createdBy;
    public Array<Creature> appliedTo;

    public TextureRegion region;
    String spriteRegionName;

    public ActivityWithEffect(com.mygdx.game.screens.GameScreen screen, float x, float y, Array<Effect> activeEffects, ActivityAreaType type, Vector2 direction, String spriteRegionName){
        super(screen.animationHelper.getAtlas().findRegion(spriteRegionName));
        //setBounds(0, 0, PalidorGame.TILE_SIZE/ PalidorGame.PPM, PalidorGame.TILE_SIZE/ PalidorGame.PPM);
        setBounds(0, 0, type.getWidth()/ PalidorGame.PPM, type.getHigth()/ PalidorGame.PPM);
        this.screen = screen;
        world = screen.world;

        this.spriteRegionName = spriteRegionName;

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

        region = new TextureRegion();
    }

    void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / PalidorGame.PPM, getY()/ PalidorGame.PPM );
        bodyDef.gravityScale= 0f;
        if(type == ActivityAreaType.ARROW)
            bodyDef.bullet = true;
        body = world.createBody(bodyDef);

        CircleShape shape;
        PolygonShape box;
        FixtureDef fixtureDef;
        switch(type){
            case ARROW:
                shape = new CircleShape();
                shape.setRadius(PalidorGame.TILE_SIZE / 4 / PalidorGame.PPM );

                fixtureDef= new FixtureDef();
                fixtureDef.shape = shape;

                fixtureDef.isSensor = true;

                fixtureDef.filter.categoryBits = PalidorGame.ACTIVITY_BIT;
                fixtureDef.filter.maskBits = PalidorGame.GROUND_BIT |
                        PalidorGame.ACTIVITY_BIT|
                        PalidorGame.CREATURE_BIT;

                body.createFixture(fixtureDef).setUserData(this);

                runAnimation = screen.animationHelper.getAnimationByID(spriteRegionName, type.getWidth(), type.getHigth(), 0.2f, 0, 1);

                break;
            case SPRAY:

                shape = new CircleShape();
                shape.setRadius(PalidorGame.TILE_SIZE / 3 / PalidorGame.PPM );

                fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 0;

                fixtureDef.isSensor = true;

                fixtureDef.filter.categoryBits = PalidorGame.ACTIVITY_BIT;
                fixtureDef.filter.maskBits = PalidorGame.ACTIVITY_BIT |
                        PalidorGame.GROUND_BIT |
                        PalidorGame.CREATURE_BIT;

                body.createFixture(fixtureDef).setUserData(this);
                runAnimation = screen.animationHelper.getAnimationByID(spriteRegionName, type.getWidth(), type.getHigth(), 0.2f, 0, 1);
                runAnimation.setPlayMode(Animation.PlayMode.NORMAL);

                break;
            case BIGBOX:
                shape = new CircleShape();
                shape.setRadius(PalidorGame.TILE_SIZE * 3 / PalidorGame.PPM );

                fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;

                fixtureDef.isSensor = true;

                fixtureDef.filter.categoryBits = PalidorGame.ATTACK_BIT;
                fixtureDef.filter.maskBits = PalidorGame.ACTIVITY_BIT |
                        PalidorGame.GROUND_BIT |
                        PalidorGame.CREATURE_BIT;

                body.createFixture(fixtureDef).setUserData(this);
                runAnimation = screen.animationHelper.getAnimationByID(spriteRegionName, type.getWidth(), type.getHigth(), 0.2f, 0, 1);
                break;
            case BOX:
                box = new PolygonShape();
                box.setAsBox(getWidth()/2,getHeight()/2);

                fixtureDef = new FixtureDef();
                fixtureDef.shape = box;

                fixtureDef.isSensor = true;

                fixtureDef.filter.categoryBits = PalidorGame.ATTACK_BIT;
                fixtureDef.filter.maskBits = PalidorGame.ACTIVITY_BIT |
                        PalidorGame.GROUND_BIT |
                        PalidorGame.CREATURE_BIT;

                body.createFixture(fixtureDef).setUserData(this);
                runAnimation = screen.animationHelper.getAnimationByID(spriteRegionName,  type.getWidth(), type.getHigth(), 0.2f, 0, 1);
                runAnimation.setPlayMode(Animation.PlayMode.NORMAL);
                break;
            default:
                box = new PolygonShape();
                box.setAsBox(getWidth()/2,getHeight()/2);

                fixtureDef = new FixtureDef();
                fixtureDef.shape = box;

                fixtureDef.isSensor = true;

                fixtureDef.filter.categoryBits = PalidorGame.ACTIVITY_BIT;
                fixtureDef.filter.maskBits = PalidorGame.ACTIVITY_BIT |
                        PalidorGame.GROUND_BIT |
                        PalidorGame.CREATURE_BIT;

                body.createFixture(fixtureDef).setUserData(this);
                runAnimation = screen.animationHelper.getAnimationByID(spriteRegionName, type.getWidth(), type.getHigth(), 0.2f, 0, 1);
                runAnimation.setPlayMode(Animation.PlayMode.NORMAL);
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
            region = (TextureRegion) runAnimation.getKeyFrame(stateTimer, false);

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

    @Override
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
