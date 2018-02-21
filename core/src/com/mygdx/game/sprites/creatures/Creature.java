package com.mygdx.game.sprites.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.ai.AI;
import com.mygdx.game.ai.BehaviourPattern;
import com.mygdx.game.enums.*;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.activities.ActivityWithEffect;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.sprites.gameobjects.GameObject;
import com.mygdx.game.sprites.triggers.Trigger;
import com.mygdx.game.tools.*;
import com.mygdx.game.stuctures.Characteristics;
import com.mygdx.game.stuctures.Effect;
import com.mygdx.game.stuctures.descriptions.CreatureDescription;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by odiachuk on 12/20/17.
 */
public class Creature extends Sprite {


    public boolean isHero;
    private BehaviourPattern pattern;
    private CreatureDescription description;
    protected World world;
    private Body body;
    public GameScreen screen;

    protected TextureRegion stand;
    protected TextureRegion hideStand;
    protected TextureRegion damaged;
    protected TextureRegion deadBody;

    TextureRegion region;

    protected State currentState;
    protected State previousState;

    protected float stateTimer;

    protected Animation kickAnimation;
    protected Animation runAnimation;
    protected Animation castAnimation;
    protected Animation shotAnimation;
    protected Animation pushAnimation;
    protected Animation jumpAnimation;

    public Characteristics stats;

    public boolean directionRight;

    public Vector2 direction; // TODO process if required aming

    public Array<Effect> activeEffects;
    Array<Integer> effectsToRemove;

    Array<AbilityID> abilities;

    Array<GameItem> inventory;
    public GameItem head;
    public GameItem armor;
    public GameItem weapon1;
    public GameItem weapon2;
    GameItem boots;
    GameItem gloves;
    GameItem belt;
    GameItem ring;
    GameItem neck;

    public String name;
    public String creatureDescription;
    public String spritesheetRegion;

    public double existingTime;
    public boolean stuned;
    public boolean hidden;
    double wasHidden;
    public boolean IN_BATTLE;
    public String id;

    float JUMP_BASE = 0.48f;
    float SPEED_BASE = 0.02f;
    public float sight = 300;

    public AI brain;
    private Set<Integer> enemyOrganizations;
    private Set<Integer> friendlyOrganizations;
    private int organization = 0;

    public CreatureStatus statusbar;
    //public CreatureAim creatureAim;

    public WeaponSprite weaponSprite;
    public WeaponSprite weaponSprite2;
    public ArmorSprite armorSprite;


    double shakeTime = -4;
    boolean shakeRight = true;
    float distortionY = 0;

    Creature closeNeighbor;  // close creature - you can talk with

    Array<Integer> dialogs; // list of dialog ids assigned to creature

    private int reputation; // representation of reputation

    boolean isActive;

    HashMap<AbilityID, Double> cooldowns;
    public Vector2 targetVector;

    public boolean canPickUpObjects;

    public Effect shieldEffect = null; // effect that will be applied to enemy who is trying to hit creature
    public String deathProcess = null;
    public TextureRegion icon;

    private boolean isHeavy = true;

    private boolean onAGround = false;

    private Animation cutAnimation;
    private Animation crushAnimation;
    private Animation fireAnimation;
    private Animation iceAnimation;
    private Animation deathAnimation;
    public Animation hideAnimation;

    Array<Animation> animationsList;
    Array<TextureRegion> animationRegions;
    Array<Float> animationStateTimers;

    public String program;

    public String summonedCreature = "";
    private Creature owner;
    private int uniqueID;

    public void resetTimeSpentOnCast() {
        this.timeSpentOnCast = 0;
        abilityToCast = AbilityID.NONE;
        abilityToCastExecutionTime = 0;
        setState(State.STANDING);

        weaponSprite.resetState();
        weaponSprite2.resetState();
    }

    public AbilityID getAbilityToCast() {
        return abilityToCast;
    } // TODO set all to setters/getters

    public AbilityID abilityToCast;
    public Animation abilityToCastAnimation;
    public float timeSpentOnCast;
    public float abilityToCastExecutionTime;


    protected boolean toDestroy;
    public boolean destroyed;

    public double timeInBattle;
    public void setIN_BATTLE(boolean IN_BATTLE) {
        if(IN_BATTLE)
            timeInBattle = existingTime + 5;
        else {
            timeInBattle = 0;
            brain.resetProgram();
        };
        this.IN_BATTLE = IN_BATTLE;
    }

    public Creature(GameScreen screen, CreatureDescription description, String items, String deathProcess, int organization, String dialogs, String program){
        super();
        this.screen = screen;

        this.description = description;

        if(description.id.equals("HERO"))
        this.isHero = true;
        else this.isHero = false;

        this.id = description.id;
        this.name = description.name;
        this.creatureDescription = description.description;
        this.pattern = description.pattern;

        this.stats = new Characteristics(description.stats);

        this.organization = organization;

        this.deathProcess = deathProcess==null?"":deathProcess;

        canPickUpObjects = false;
        toDestroy = false;
        destroyed = false;

        this.activeEffects = new Array<Effect>();
        this.effectsToRemove = new Array<Integer>();
        this.inventory = new Array<GameItem>() ;
        this.dialogs = new Array<Integer>();


        directionRight = false;
        stateTimer = 0f;
        currentState = State.STANDING;
        previousState = State.STANDING;

        existingTime = 0d;
        timeSpentOnCast =0f;
        setAbilityToCast(AbilityID.NONE);

        stuned = false;
        hidden = false;
        IN_BATTLE = false;

        isActive = false;

        this.brain = new AI(this,screen,program,pattern);

        for(Effect effect: description.effects){
            applyEffect(effect);
        }

        // get abilities list
        this.abilities = description.abilities;

        // init cooldowns
        cooldowns = new HashMap<AbilityID, Double>();
        for(AbilityID ability : abilities){
            cooldowns.put(ability,0d);
        }

        // add all items from inventory  -  get items from item descriptions in levelmanager
        if(items != null && !"".equals(items))
        for (String itemd : items.split(",")){
            inventory.add(new GameItem(screen, this.screen.levelmanager.ITEMS_DESCRIPTIONS.get(itemd.trim())));
        }


        // set animations
        spritesheetRegion = description.region;

        if(description.id.contains("animal")) {
            stand = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 0);
            hideStand = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 8);
            damaged = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 7);
            icon = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 0); //TODO
            deadBody = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 6);
            runAnimation = screen.animationHelper.getAnimationByID(description.region, 0.2f, 1, 2);
            jumpAnimation = screen.animationHelper.getAnimationByID(description.region, 0.3f, 3);

            weaponSprite = new WeaponSprite(this, "weapon_paw", true);
            weaponSprite2 = new WeaponSprite(this, "weapon_paw", false);
            armorSprite = new ArmorSprite(this);
        }else {
            stand = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 0);
            hideStand = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 8);
            icon = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 0); //TODO
            damaged = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 7);
            deadBody = screen.animationHelper.getTextureRegionByIDAndIndex(description.region, 6);
            runAnimation = screen.animationHelper.getAnimationByID(description.region, 0.2f, 1, 2);
            jumpAnimation = screen.animationHelper.getAnimationByID(description.region, 0.3f, 3);

            weaponSprite = new WeaponSprite(this, "weapon_hand",true);
            weaponSprite2 = new WeaponSprite(this, "weapon_hand", false);
            armorSprite = new ArmorSprite(this);
        }


        cutAnimation = screen.animationHelper.getAnimationByID("blood", 32, 32, 0.1f, 0, 1, 2);
        crushAnimation= screen.animationHelper.getAnimationByID("smash", 32, 32, 0.1f, 0, 1, 2);
        fireAnimation= screen.animationHelper.getAnimationByID("firedamage", 32, 32, 0.1f, 0, 1, 2);
        iceAnimation= screen.animationHelper.getAnimationByID("icedamage", 32, 32, 0.1f, 0, 1, 2);
        deathAnimation= screen.animationHelper.getAnimationByID("death", 32, 64, 0.2f, 0, 1, 2);
        hideAnimation= screen.animationHelper.getAnimationByID("hide", 32, 64, 0.2f, 0, 1, 2);

        animationsList = new Array<Animation>();
        animationStateTimers = new Array<Float>();
        animationRegions = new Array<TextureRegion>();

        //abilityToCastAnimation = screen.animationHelper.getAnimationByID(creatureDescription.region,0.3f,4,5);

//        shotAnimation = screen.animationHelper.getAnimationByID(creatureDescription.region,0.3f,4,5);
//        kickAnimation = screen.animationHelper.getAnimationByID(creatureDescription.region,0.3f,2,3);
//        castAnimation = screen.animationHelper.getAnimationByID(creatureDescription.region,0.3f,6,7);

        if(dialogs != null && !dialogs.equals("")) {
            for (String curItem : dialogs.split(",")) {
                if(!curItem.equals(""))
                    this.dialogs.add(Integer.valueOf(curItem.trim()));
            }
        }


         this.program = program;


    }
    public Creature (GameScreen screen, float x, float y, CreatureDescription description, String items, String deathProcess, int organization, String dialogs, String program) {
        //super(screen.getAtlas().findRegion(creatureDescription.region));

        this(screen, description, items, deathProcess, organization, dialogs, program);
        this.description = description;
        makeAlive(x, y);

        //equip
        for (String itemd : description.equiped){
            equipItem(new GameItem(screen, this.screen.levelmanager.ITEMS_DESCRIPTIONS.get(itemd.trim())));
        }
    }

    public void makeAlive(float x, float y){

        setBounds(0, 0, PalidorGame.TILE_SIZE/ PalidorGame.PPM, PalidorGame.TILE_SIZE/ PalidorGame.PPM);
        setRegion(stand);

        this.world = screen.world;

        setPosition(x, y);
        createBody();

        direction = new Vector2(1f,0f);
        targetVector = new Vector2(1f,0f);

        statusbar = new CreatureStatus(this);
        //creatureAim  = new CreatureAim(this); //TODO process aiming

    }

    public void createBody(float x, float y) {
        setPosition(x, y);
        this.world = screen.world;
        createBody();
    }

    public void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / PalidorGame.PPM, getY()/ PalidorGame.PPM );
        body = world.createBody(bodyDef);

//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(getWidth() / 3,getHeight() / 3);

        CircleShape shape = new CircleShape();
        shape.setRadius(PalidorGame.TILE_SIZE * 1/3 / PalidorGame.PPM );
        //shape.setRadius(PalidorGame.TILE_SIZE /2 / PalidorGame.PPM );
        //shape.setRadius(0.2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.3f;
        //fixtureDef.restitution = 0.5f;
        //fixtureDef.density = 0.001f;

        fixtureDef.filter.categoryBits = PalidorGame.CREATURE_BIT;
        fixtureDef.filter.maskBits =
                PalidorGame.CREATURE_BIT|
                PalidorGame.GROUND_BIT |
                PalidorGame.ITEM_BIT |
                PalidorGame.ACTIVITY_BIT |
                PalidorGame.ATTACK_BIT|
                PalidorGame.JUMP_LEFT |
                        PalidorGame.JUMP_RIGHT |
                PalidorGame.MOVE_LEFT_POINT |
                PalidorGame.MOVE_RIGHT_POINT|
                        PalidorGame.STAND_POINT|
                        PalidorGame.OBJECT_BIT|
                        PalidorGame.CREATURE_BOTTOM|
                PalidorGame.TRIGGER_POINT;

        body.createFixture(fixtureDef).setUserData(this);


//        EdgeShape arm = new EdgeShape();
//        arm.set(new Vector2(10 / PPM, 20/ PPM ), new Vector2(10 / PPM, 10/ PPM ));
//        fixtureDef.shape = arm;
//        fixtureDef.isSensor = false; // not PHISICAL body - just sensor
//
//        //fixtureDef.filter.categoryBits = 1; can be collided anyone with this mask
//        //fixtureDef.filter.maskBits = sda | asd | das - can collide with
//
        EdgeShape bottom = new EdgeShape();
        bottom.set(new Vector2(-10 / PalidorGame.PPM, -24 / PalidorGame.PPM ), new Vector2(10 / PalidorGame.PPM, -24 / PalidorGame.PPM ));
        fixtureDef.shape = bottom;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = PalidorGame.CREATURE_BOTTOM;
        fixtureDef.filter.maskBits =
                PalidorGame.CREATURE_BIT|
                        PalidorGame.GROUND_BIT |
                        PalidorGame.OBJECT_BIT;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void update(float dt){
        setAnimationFrames(dt);

        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
            Gdx.app.log("Destroyed", name);
        }

        if(!toDestroy && !destroyed) {
            if (stats.health.current <= 0)
                toDie();

            if(getBody().getPosition().y < 0)
                toDie();

            updatePosition(dt);

            weaponSprite.update(dt);
            weaponSprite2.update(dt);
            armorSprite.update(dt);

            if(currentState == State.FALLING)
                updateGravity(true);
            else
                updateGravity(false);

            checkEffects(dt);

            if(IN_BATTLE &&  existingTime >= timeInBattle)
                setIN_BATTLE(false);

            existingTime = existingTime + dt;

            if ((getState() == State.CASTING || getState() == State.SHOTING || getState() == State.KICKING) && !stuned) {
                timeSpentOnCast = timeSpentOnCast + dt;
            }

            statusbar.updatePosition();
            //creatureAim.updatePosition(); //TODO aiming
            if(statusbar.removeMessageTime != 0 && statusbar.removeMessageTime <= existingTime)
                statusbar.removeMessage();


        }
    }

    private void updateGravity(boolean makeHeavy) {
//        if (makeHeavy){
//            if(!isHeavy) {
//                body.getFixtureList().get(0).setDensity(10f);
//                body.resetMassData();
//            }
//        }else {
//            if(isHeavy) {
//                body.getFixtureList().get(0).setDensity(10f);
//                body.resetMassData();
//            }
//        }
//        isHeavy = makeHeavy;
    }


    public void updatePosition(float dt){

        if(shakeTime > existingTime)
            if(shakeRight) {
                distortionY =  + 0.05f;
                shakeRight = false;
            }else {
                distortionY = - 0.05f;
                shakeRight = true;
            }
        else
            distortionY = 0;

        this.setPosition(body.getPosition().x - getWidth()/2 , body.getPosition().y - getHeight() /2 + distortionY);
        this.setRegion(getFrame(dt));

    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        if(!stuned) {
            switch (currentState) {

                case RUNNING:
                    region = (TextureRegion) runAnimation.getKeyFrame(stateTimer, true);
                    break;
                case JUMPING:
                    region = (TextureRegion) jumpAnimation.getKeyFrame(stateTimer);
                    break;
                case CASTING:
                    region = (TextureRegion) abilityToCastAnimation.getKeyFrame(stateTimer, true);
                    //region = (TextureRegion) castAnimation.getKeyFrame(stateTimer, true);
                    break;
                case KICKING:
                    region = (TextureRegion) abilityToCastAnimation.getKeyFrame(stateTimer, false);
                    //region = (TextureRegion) kickAnimation.getKeyFrame(stateTimer, true);
                    break;
                case SHOTING:
                    region = (TextureRegion) abilityToCastAnimation.getKeyFrame(stateTimer, false);
                    //region = (TextureRegion) shotAnimation.getKeyFrame(stateTimer, true);
                    break;

                default:
                    region = stand;
            }
        } else
            region = damaged;

        if(hidden)
            region =  hideStand;

        //if ((body.getLinearVelocity().x < 0 || !directionRight) && !region.isFlipX()){
        if (!directionRight && !region.isFlipX()){
            region.flip(true, false);
            //directionRight = false;
        } else
            //if ((body.getLinearVelocity().x > 0 || directionRight) && region.isFlipX()){
            if (directionRight && region.isFlipX()){
                region.flip(true,false);
                //directionRight = true;
            }

        stateTimer = currentState == previousState ? stateTimer + dt: 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(currentState == State.CASTING)
            return State.CASTING;
        if(currentState == State.KICKING)
            return State.KICKING;
        if(currentState == State.SHOTING)
            return State.SHOTING;

        if(currentState != State.DEAD) {
//            if (body.getLinearVelocity().y > 0.01)
//                return State.JUMPING;
//            else if (body.getLinearVelocity().y < -0.01)
//                return State.FALLING;
//            else if (body.getLinearVelocity().x != 0)
//                return State.RUNNING;
//            else

            if (body.getLinearVelocity().y > 0.01 && !onAGround)
                return State.JUMPING;
            else if (body.getLinearVelocity().y < -0.01 && !onAGround)
                return State.FALLING;
            else if (body.getLinearVelocity().x != 0)
                return State.RUNNING;
            else
                return State.STANDING;
        } else return State.DEAD;
    }

    public void setState(State state) {
        this.currentState = state;
    }

    public Body getBody() {
        return body;
    }


    @Override
    public void draw(Batch batch) {

            if (!destroyed) {
                if(screen.hero.isAbleToSee(this) || isHero) {
                    try {
                        //                if(this.id.equals("animal_bolfir")){
                        //                    Gdx.app.log("error", "");
                        //                }

                        weaponSprite.draw(batch);
                        super.draw(batch);
                        armorSprite.draw(batch);
                        statusbar.draw(batch);
                        weaponSprite2.draw(batch);


                    } catch (Exception e) { //TODO
                        Gdx.app.log("error", e.getMessage());
                        e.printStackTrace();
                        //       batch.draw(region, getX()/100,getY()/100); //TODO
                        //       statusbar.draw(batch);
                    }
                    //creatureAim.draw(batch);
                }
            } else {
                if (getY() > 0 && owner == null) {
                    batch.draw(deadBody, getX(), getY(), getWidth(), getHeight());
                    statusbar.draw(batch);
                }
            }


        for(int i = 0 ; i<animationRegions.size;i++) {
            TextureRegion animregion = animationRegions.get(i);
            batch.draw(animregion, getX(), getY() + getHeight()/2, animregion.getRegionWidth() / PalidorGame.PPM, animregion.getRegionHeight() / PalidorGame.PPM);
        }
    }

    ///// AI

    //next AI step
    public void nextStep(float delta){
        brain.getNextStep(delta);
    }

    // find ability by type for AI
    public AbilityID findAbility(AbilityType type) {
        for(AbilityID ability : abilities){
            if(ability.getType().equals(type) && cooldowns.get(ability) <= existingTime) {
                return ability;
            }
        };
        return null;
    }

    //TODO activity tracking
    public void makeActive() {
        isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setMoveLeft(boolean b) {
        this.brain.setMoveLeft(b);
    }

    public void setMoveRight(boolean b) {
        this.brain.setMoveRight(b);
    }

    public void setHasToJump(boolean right, boolean hasToJump) {
        this.brain.setHasToJump(right, hasToJump);
    }

    public void setStandStill(boolean b) {
        this.brain.setStandStill(b);
    }


    public int getToughness() { //TODO
        int result = stats.health.current + getReputation();
        return result;
    }

    public int getReputation() {
        return reputation;
    }

    public boolean isEnemy(Creature creature) {  // TODO isEnemy
        if(screen.hero.getGlobalState(String.valueOf(this.organization)).contains(String.valueOf(creature.getOrganization())))
            return true;
        return false;
    }

    public int getOrganization() {
        return organization;
    }


    // ACTIONS
    public void jump() {
//        if (getState() == State.CASTING || getState() == State.SHOTING || getState() == State.KICKING){
//            resetTimeSpentOnCast();
//        }

            //TODO
//            body.getFixtureList().get(0).setDensity(10);
//            body.resetMassData();

            if ( onAGround ) {
                //getBody().setLinearVelocity(0,0);
                //getBody().applyLinearImpulse(new Vector2(directionRight?2*stats.speed.current:-2*stats.speed.current, (IN_BATTLE)?JUMP_BASE/2:JUMP_BASE * stats.jumphight.current), getBody().getWorldCenter(), true);
                moveUp();
                onAGround(false);
            }
    }

    public void stop() {
        if(!stuned)
            getBody().setLinearVelocity(0,getBody().getLinearVelocity().y);
    }

    public void moveUp() {
        getBody().applyLinearImpulse(new Vector2(0, JUMP_BASE * stats.jumphight.current), getBody().getWorldCenter(), true);
        //Gdx.app.log("","'"+stats.jumphight.current);
        //currentState = State.JUMPING;
    }

    public void move(boolean moveright){
        directionRight = moveright;
//        if (getState() == State.CASTING || getState() == State.SHOTING || getState() == State.KICKING){
//            resetTimeSpentOnCast();
//        }
        if(!stuned && abilityToCast == AbilityID.NONE){
            if(getBody().getLinearVelocity().y == 0){
            if(moveright && getBody().getLinearVelocity().x < 0.2 * stats.speed.current)
                getBody().applyLinearImpulse(new Vector2( SPEED_BASE * stats.speed.current,0), getBody().getWorldCenter(), true);
                //getBody().applyLinearImpulse(new Vector2( (IN_BATTLE)?SPEED_BASE/2:SPEED_BASE * stats.speed.current,0), getBody().getWorldCenter(), true);
            else
                if ( ! moveright && getBody().getLinearVelocity().x > -0.2 * stats.speed.current)
                    getBody().applyLinearImpulse(new Vector2(-(SPEED_BASE) * stats.speed.current,0), getBody().getWorldCenter(), true);
            //getBody().applyLinearImpulse(new Vector2(-((IN_BATTLE)?SPEED_BASE/2:SPEED_BASE) * stats.speed.current,0), getBody().getWorldCenter(), true);
            } else {
                if(moveright && getBody().getLinearVelocity().x < 0.1 * stats.speed.current)
                    getBody().applyLinearImpulse(new Vector2( SPEED_BASE* stats.speed.current,0), getBody().getWorldCenter(), true);
                    //getBody().applyLinearImpulse(new Vector2( (IN_BATTLE)?SPEED_BASE/2:SPEED_BASE * stats.speed.current,0), getBody().getWorldCenter(), true);
                else
                if ( ! moveright && getBody().getLinearVelocity().x > -0.1 * stats.speed.current)
                    getBody().applyLinearImpulse(new Vector2(-(SPEED_BASE* stats.speed.current),0), getBody().getWorldCenter(), true);

            }
        }

    }

    public void useAbility(AbilityID ability){
        if(!stuned && abilityToCast == AbilityID.NONE)
        {
            if(checkCooldownExpired(ability)) {

                abilityToCastExecutionTime = AbilityHandler.getAbilityCastTime(this, ability);

                //if(abilityToCastExecutionTime > 0.2){
                setState(ability.getState());
                //}
                setAbilityToCast(ability);
            } else {
                statusbar.addMessage( String.format("%s not ready: %.1f",ability.getName(),this.showWhenAbilityWillBeAvailable(ability)) , existingTime + 1d, Fonts.INFO);
            }
        }
    }

    public Array<ActivityWithEffect> activateAbility(AbilityID ability) {
        if(!stuned) {
            setInvisible(false); // make visible
            resetTimeSpentOnCast(); // reset casting time
            //getBody().setLinearVelocity(0, 0); // STOP
            cooldowns.put(ability, AbilityHandler.getAbilityCooldownTime(this,ability) + existingTime); // put cooldown
            setIN_BATTLE(true); // set In_Battle state
            return AbilityHandler.getAbilityAndUseIt(screen, this, ability); // return ability
        } return null;
    }

    //results of defense actions
    public void lockAbility(AbilityID ability) {
            resetTimeSpentOnCast();
            //getBody().setLinearVelocity(0, 0); // STOP
            cooldowns.put(ability, AbilityHandler.getAbilityCooldownTime(this,ability) + existingTime);
            setIN_BATTLE(true);
            //Gdx.app.log("Creature", "Locked " + ability);
    }

    public boolean checkCooldownExpired(AbilityID ability){
        return cooldowns.get(ability) <= existingTime;
    }


    public Array<AbilityID> getAbilities() {
        return abilities;
    }

    public boolean isHidden() {
        if(!hidden && wasHidden + 0.5 > existingTime) // 0.5 sec added to avaid Instance detection
            return true;
        return hidden;
    }

    public void setInvisible(boolean b) {
        if(hidden && !b) {
            wasHidden = existingTime;
            removeEffectByID(EffectID.INVISIBLE);
            //lockAbility(AbilityID.MASK);
        }
        hidden = b;
    }



    public void setStun(boolean stuned, boolean severe) {
        this.stuned = stuned;
        //resetTimeSpentOnCast();
        if(severe && stuned) lockAbility(abilityToCast);
    }



    public double showWhenAbilityWillBeAvailable(AbilityID ability) {
        if(cooldowns.get(ability) - existingTime <= 0)
            return 0;
        return Math.round(cooldowns.get(ability) - existingTime);
    }

    ///// EFFECTS

    public void applyEffect(Effect neweffect){
        activeEffects.add(new Effect(
                neweffect.id,
                neweffect.duration ,
                neweffect.magnitude,
                neweffect.dotDuration,
                (neweffect.dotDuration > 0 ? neweffect.dotDuration + existingTime : 0),
                neweffect.duration + existingTime));
        EffectsHandler.applyEffect(this, neweffect.id, neweffect.magnitude);
        if(statusbar!=null)
            statusbar.update();
    }

    private void checkEffects(float dt) {

        Effect effect;

        //check if effect is still active
        for(int i = 0; i < activeEffects.size; i++){
            effect = activeEffects.get(i);
            if (effect.duration != 0) {//not constant effect
                if (effect.removeTime <= existingTime) {
                    effectsToRemove.add(i);
                    EffectsHandler.resetEffect(this, effect.id, effect.magnitude);
                }
            };
            if (effect.dotDuration > 0 && effect.refreshTime <= existingTime) { // tick DoT
                effect.refreshTime = effect.refreshTime + effect.dotDuration;
                EffectsHandler.applyEffect(this, effect.id, effect.magnitude);
            }
        }

        // remove effects from active list
        if (effectsToRemove.size > 0) {
            for (int i = effectsToRemove.size-1; i >= 0  ; i--) {
                activeEffects.removeIndex(effectsToRemove.get(i));
                statusbar.update();
            }
            effectsToRemove.clear();
        }
    }

    public void removeEffect(Effect effect) {

        //check if effect is still active
        for(int i = 0; i < activeEffects.size; i++){
            if(effect.equals(activeEffects.get(i)) ){
                effectsToRemove.add(i);
                EffectsHandler.resetEffect(this, effect.id, effect.magnitude);

            }
        }

        // remove effects from active list
        if (effectsToRemove.size > 0) {
            for (int i = effectsToRemove.size-1; i >= 0  ; i--) {
                activeEffects.removeIndex(effectsToRemove.get(i));
                statusbar.update();
            }
            effectsToRemove.clear();
        }

    }

    // remove effect WITHOUT UNHANDLING (works for flag-type effect : Shields, Immune)
    public void removeEffectByID(EffectID effect) {

        //check if effect is still active
        for(int i = 0; i < activeEffects.size; i++){
            if(effect.equals(activeEffects.get(i).id) ){
                activeEffects.get(i).removeTime = existingTime;
                break;
            }
        }

    }


    // get first effect of a type
    public Effect getEffect(EffectID id) {
        for(int i = activeEffects.size-1;i>=0;i--){
            if(activeEffects.get(i).id == id){
                return activeEffects.get(i);
            }
        }
        return null;
    }

    // get number of effect of a type
    public int getNumberOfEffects(EffectID id) {
        int res = 0;
        for(int i = activeEffects.size-1;i>=0;i--){
            if(activeEffects.get(i).id == id){
                res++;
            }
        }
        return res;
    }

    //check effect
    public boolean checkEffect(EffectID id) {
        return getEffect(id) != null;
    }
    //get sum of all effects
    public float getEffectsSum(EffectID id) {
        float result = 0;
        for(int i = activeEffects.size-1;i>=0;i--){
            if(activeEffects.get(i).id == id){
                result = result + activeEffects.get(i).magnitude;
            }
        }
        return result;
    }


    /// INVENTORY

    // add item to inventory
    public void addToInventory(GameItem item) {
        //if(!toDestroy && !item.toDestroy) {
        if(canPickUpObjects){ // TODO make all Humans pickup objects
            item.destroyBody();
            inventory.add(item);
        }
    }

    //check item in inventory
    public boolean checkInInventory(String itemID) {
        for(GameItem item: inventory){
            if(item.id.equals(itemID))
                return true;
        }
        return false;
    }

    // remove item from inventory
    public void removeItemByID(String itemID) {
        if(checkInInventory(itemID)){
                for(int i=0; i<inventory.size;i++) {
                    GameItem item = inventory.get(i);
                    if (item.id.equals(itemID)){
                        inventory.removeIndex(i);
                        return;
                    }
                }
        }
    }

    //add item to inventory
    public void addItemByID(String itemd) {
        inventory.add(new GameItem(screen, this.screen.levelmanager.ITEMS_DESCRIPTIONS.get(itemd.trim())));
    }

    // put item on and apply effect
    public String equipItem(GameItem item){

        for(Effect curEffect : item.getEffects()){
            applyEffect(curEffect);
        }
        switch (item.getType()){
            case HEAD:
                head = item;
                break;
            case ARMOR:
                armor = item;
                armorSprite.setPicture(item.getPicture());
                this.stats.jumphight.current = this.stats.jumphight.current - 1;
                break;
            // TODO add all
            case WEAPON_MAGIC_ICE:
            case WEAPON_MAGIC_FIRE:
            case WEAPON_MAGIC_NATURE:
            case WEAPON_MAGIC_DEATH:
            case    WEAPON_AXE:
            case    WEAPON_SWORD:
            case    WEAPON_HUMMER:
            case    WEAPON_BOW:
            case    WEAPON_SLING:
            case    WEAPON_XBOW:
            case    WEAPON_SHIELD:
                if(weapon1 == null) {
                    weapon1 = item;
                    weaponSprite.setPicture(item.getPicture());
                }
                else if (weapon2 == null) {
                    weapon2 = item;
                    weaponSprite2.setPicture(item.getPicture());
                } else {
                    return "Item does not fit slot (remove equiped item)";
                }
                break;
        }
        inventory.removeValue(item,true);
        if(statusbar != null)
            statusbar.update();

        return "";
    }

    //take off item - undo effect
    public void unEquipItem(GameItem item){

        for(Effect curEffect : item.getEffects()){
            removeEffect(curEffect);
        }
        switch (item.getType()){
            case HEAD:
                head = null;
                break;
            case ARMOR:
                armor = null;
                armorSprite.setPicture(null);
                this.stats.jumphight.current = this.stats.jumphight.current + 1;
                break;
            case WEAPON_MAGIC_ICE:
            case WEAPON_MAGIC_FIRE:
            case WEAPON_MAGIC_NATURE:
            case WEAPON_MAGIC_DEATH:
            case    WEAPON_AXE:
            case    WEAPON_SWORD:
            case    WEAPON_HUMMER:
            case    WEAPON_BOW:
            case    WEAPON_SLING:
            case    WEAPON_XBOW:
            case    WEAPON_SHIELD:
                if(weapon1 != null) {
                    weapon1 = null;
                    weaponSprite.setPicture("weapon_hand");
            }
                else if (weapon2 != null) {
                    weapon2 = null;
                    weaponSprite2.setPicture("weapon_hand");
                }
                break;
            // TODO add all
        }

        inventory.add(item);
        statusbar.update();
    }


    //throw to ground
    public void throwFromInventory(GameItem item) {
        inventory.removeValue(item, true);
        float direction = directionRight ?  -(PalidorGame.TILE_SIZE + PalidorGame.TILE_SIZE/2) : (PalidorGame.TILE_SIZE + PalidorGame.TILE_SIZE/2) ;
        item.createBody((getBody().getPosition().x ) * PalidorGame.PPM + direction, getBody().getPosition().y * PalidorGame.PPM);

        if(item!=null)
            screen.levelmanager.ITEMS.add(item);
    }

    public void throwFromInventory(String itemID) {
        GameItem item = null;
        for(int i = 0; i<inventory.size;i++){
            if(inventory.get(i).id.equals(itemID)) {
                item = inventory.get(i);
                inventory.removeIndex(i);
            }
        }

        if(item != null) {
            float direction = directionRight ? -(PalidorGame.TILE_SIZE + PalidorGame.TILE_SIZE / 2) : (PalidorGame.TILE_SIZE + PalidorGame.TILE_SIZE / 2);
            item.createBody((getBody().getPosition().x) * PalidorGame.PPM + direction, getBody().getPosition().y * PalidorGame.PPM);

            if (item != null)
                screen.levelmanager.ITEMS.add(item);
        }
    }

    public void throwFromInventory() {
        if(inventory.size > 0)
            throwFromInventory(inventory.get(0));
    }

    //use item
    public String useItem(GameItem item) {

        if(item.getEffects() != null && item.getEffects().size>0) {
            for (Effect curEffect : item.getEffects()) {
                applyEffect(curEffect);
            }
        }
        if(item.getProcess() != null && !"".equals(item.getProcess()))
            ConditionProcessor.conditionProcess(this,item.getProcess());
        inventory.removeValue(item,true);
        addStatusMessage(item.itemname + " was used", Fonts.INFO);

        return "";
    }

    public Array<GameItem> getInventory() {
        return inventory;
    }


    /// CASTING PROCESS

    public void setAbilityToCast(AbilityID abilityToCast) {

        this.abilityToCast = abilityToCast;
        if(abilityToCast != AbilityID.NONE) {
            this.abilityToCastAnimation = AbilityHandler.getAnimation(screen, abilityToCast, spritesheetRegion);
            armorSprite.updateAbilityToCastAnimation(); //TODO find another solution
        }
    }

    public boolean finishedCasting() {
        return timeSpentOnCast > abilityToCastExecutionTime;
    }


    // DEATH processing

    public void toDie(){
        ConditionProcessor.conditionProcess(this, deathProcess);

        toDestroy = true;
        setState(State.DEAD);
        int inventorySize = getInventory().size;
        for(int i =0; i<inventorySize;i++)
            throwFromInventory(getInventory().get(0));

        startAnimation(deathAnimation);

        if(owner != null) {
            owner.summonedCreature = "";
            owner.addStatusMessage("Summoned creature defeated", Fonts.IMPORTANT);
        }

        //screen.slowDown(1);
    }


    public void activateTrigger(Trigger trigger) {
        TriggerHandler.runProcess(this, trigger);
    }



    // to status bar
    public void addStatusMessage(String message, Fonts font) {
        if(statusbar != null)
            statusbar.addMessage(message, existingTime + 1f, font);
    }

    public void doDamage(int damageValue, EffectID damageType) {
        stats.health.current = stats.health.current - damageValue;
        addStatusMessage(String.valueOf(damageValue), Fonts.BAD);

        //getBody().applyLinearImpulse(new Vector2(0,1), getBody().getWorldCenter(), true);

        switch (damageType){
            case CUT_DAMAGE:
                startAnimation(cutAnimation);
                break;
            case CRUSH_DAMAGE:
                startAnimation(crushAnimation);
                break;
            case FIRE_DAMAGE:
                startAnimation(fireAnimation);
                break;
            case ICE_DAMAGE:
                startAnimation(iceAnimation);
                break;
        }


    }

    public void startAnimation(Animation animation) {
        if(animation != null) {
            animationsList.add(animation);
            animationStateTimers.add(0f);
        }
    }

    public void setAnimationFrames(float dt){
        TextureRegion animregion;
        animationRegions.clear();
        for(int i = 0 ; i<animationsList.size;i++) {
            Animation anim = animationsList.get(i);
            animregion = (TextureRegion) anim.getKeyFrame(animationStateTimers.get(i), false);
            if (anim.isAnimationFinished(animationStateTimers.get(i))) {
                animationsList.removeIndex(i);
                animationStateTimers.removeIndex(i);
            } else {
                animationRegions.add(animregion);
                animationStateTimers.set(i, animationStateTimers.get(i) + dt);
            }
        }
    }

    public void setNeighbor(Creature neighbor) {
            this.closeNeighbor = neighbor;
    }


    public Creature getNeighbor() {  // TODO add dialogs between peacefull
        return this.closeNeighbor;
    }

    public Array<Integer> getDialogs() {
        return dialogs;
    }


    public void touchObject(GameObject object) {
        ObjectTouchProcessor.touchObject(this,object);
    }


    public String getDeathProcess() {
        return deathProcess;
    }

    public void attackIfEnemyIsNear() { //TODO - add logic
        useAbility(abilities.first());
    }

    public void removeShield() {
        shieldEffect = null;
        weaponSprite.holding = false;
        weaponSprite.resetState();
        weaponSprite2.holding = false;
        weaponSprite2.resetState();
    }

    public void shake() {
        shakeTime = existingTime + 1d;
    }

    public String getProgram() {
        return program;
    }

    public void setOrganization(int organization) {
        this.organization = organization;
    }

    public void onAGround(boolean val) {
        onAGround = val;
    }

    public void setOwner(Creature owner) {
        this.owner = owner;
    }

    public int getID() {
        return uniqueID;
    }

    public void setUniqueID(int i) {
        uniqueID = i;
    }
}
