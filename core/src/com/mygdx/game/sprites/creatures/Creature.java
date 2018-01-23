package com.mygdx.game.sprites.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.ai.AI;
import com.mygdx.game.enums.*;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.activities.ActivityWithEffect;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.sprites.gameobjects.GameObject;
import com.mygdx.game.tools.TriggerHandler;
import com.mygdx.game.stuctures.Characteristics;
import com.mygdx.game.stuctures.Effect;
import com.mygdx.game.stuctures.descriptions.CreatureDescription;
import com.mygdx.game.tools.AbilityHandler;
import com.mygdx.game.tools.EffectsHandler;
import com.mygdx.game.tools.Fonts;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by odiachuk on 12/20/17.
 */
public class Creature extends Sprite {

    protected World world;
    private Body body;
    public GameScreen screen;

    protected TextureRegion stand;
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
    public String description;
    public String spritesheetRegion;

    public double existingTime;
    public boolean stuned;
    public boolean hidden;
    public boolean IN_BATTLE;
    public String id;

    float JUMP_BASE = 5;
    float SPEED_BASE = 0.3f;

    public AI brain;
    private Set<Integer> enemyOrganizations;
    private Set<Integer> friendlyOrganizations;
    private int organization;

    public CreatureStatus statusbar;
    //public CreatureAim creatureAim;

    Creature closeNeighbor;  // close creature - you can talk with

    Array<Integer> dialogs; // list of dialog ids assigned to creature

    private int reputation; // representation of reputation

    boolean isActive;

    HashMap<AbilityID, Double> cooldowns;
    public Vector2 targetVector;

    public boolean canPickUpObjects;

    public Effect shieldEffect = null; // effect that will be applied to enemy who is trying to hit creature

    public CreatureStatus getStatusbar() {
        return statusbar;
    }

    public void resetTimeSpentOnCast() {
        this.timeSpentOnCast = 0;
        abilityToCast = AbilityID.NONE;
        abilityToCastExecutionTime = 0;
        setState(State.STANDING);
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
        };
        this.IN_BATTLE = IN_BATTLE;
    }

    public Creature(GameScreen screen, CreatureDescription description, String items){
        super();
        this.screen = screen;

        this.id = description.id;
        this.name = description.name;
        this.description = description.description;

        this.stats = new Characteristics(description.stats);

        this.organization = description.organization;

        this.dialogs = description.dialogs;

        canPickUpObjects = false;
        toDestroy = false;
        destroyed = false;

        this.activeEffects = new Array<Effect>();
        this.effectsToRemove = new Array<Integer>();
        this.inventory = new Array<GameItem>() ;

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

        this.brain = new AI();

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
        stand = screen.animationHelper.getTextureRegionByIDAndIndex(description.region , 0);
        deadBody = screen.animationHelper.getTextureRegionByIDAndIndex(description.region , 10);
        runAnimation = screen.animationHelper.getAnimationByID(description.region, 0.1f, 0,1);

        //pushAnimation = screen.animationHelper.getAnimationByID(description.region,0.3f,8,9);
        jumpAnimation = screen.animationHelper.getAnimationByID(description.region,0.3f,2,3);

        //abilityToCastAnimation = screen.animationHelper.getAnimationByID(description.region,0.3f,4,5);

//        shotAnimation = screen.animationHelper.getAnimationByID(description.region,0.3f,4,5);
//        kickAnimation = screen.animationHelper.getAnimationByID(description.region,0.3f,2,3);
//        castAnimation = screen.animationHelper.getAnimationByID(description.region,0.3f,6,7);

    }
    public Creature (GameScreen screen, float x, float y, CreatureDescription description, String items) {
        //super(screen.getAtlas().findRegion(description.region));

        this(screen, description, items);

        //equip
        for (String itemd : description.equiped){
            equipItem(new GameItem(screen, this.screen.levelmanager.ITEMS_DESCRIPTIONS.get(itemd.trim())));
        }

        makeAlive(x, y);
    }

    public void makeAlive(float x, float y){

        setBounds(0, 0, PalidorGame.TILE_SIZE/ PalidorGame.PPM, PalidorGame.TILE_SIZE/ PalidorGame.PPM);
        setRegion(stand);

        this.world = screen.world;

        setPosition(x, y);
        createBody();

        direction = new Vector2(0.5f,0.5f);
        targetVector = new Vector2(0.5f,0.5f);

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

        //PolygonShape shape = new PolygonShape();
        //shape.setAsBox(getWidth() / 2,getHeight() / 2);

        CircleShape shape = new CircleShape();
        shape.setRadius(PalidorGame.TILE_SIZE / 2 / PalidorGame.PPM );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        //fixtureDef.friction = 0.5f;
        //fixtureDef.restitution = 0.5f;

        fixtureDef.filter.categoryBits = PalidorGame.CREATURE_BIT;
        fixtureDef.filter.maskBits =
                PalidorGame.CREATURE_BIT|
                PalidorGame.GROUND_BIT |
                PalidorGame.ITEM_BIT |
                PalidorGame.ACTIVITY_BIT |
                PalidorGame.ATTACK_BIT|
                PalidorGame.JUMP_POINT |
                PalidorGame.MOVE_LEFT_POINT |
                PalidorGame.MOVE_RIGHT_POINT|
                        PalidorGame.STAND_POINT|
                        PalidorGame.OBJECT_BIT|
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
//        body.createFixture(fixtureDef).setUserData("arm");

    }

    public void update(float dt){

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

            checkEffects(dt);

            if(IN_BATTLE &&  existingTime >= timeInBattle)
                setIN_BATTLE(false);

            existingTime = existingTime + dt;

            if (getState() == State.CASTING || getState() == State.SHOTING || getState() == State.KICKING) {
                timeSpentOnCast = timeSpentOnCast + dt;
            }

            statusbar.updatePosition();
            //creatureAim.updatePosition(); //TODO aiming
            if(statusbar.removeMessageTime != 0 && statusbar.removeMessageTime <= existingTime)
                statusbar.removeMessage();

        }
    }

    public void nextStep(){
        brain.getNextStep(this,screen);
    }

    public AbilityID findAbility(AbilityType type) {
        for(AbilityID ability : abilities){
            if(ability.getType().equals(type) && cooldowns.get(ability) <= existingTime) {
                return ability;
            }
        };
        return null;
    }


    public void updatePosition(float dt){
        this.setPosition(body.getPosition().x - getWidth()/2 , body.getPosition().y - getHeight() /2);
        this.setRegion(getFrame(dt));

    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        switch (currentState) {

            case RUNNING:
                region = (TextureRegion) runAnimation.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                region = (TextureRegion) jumpAnimation.getKeyFrame(stateTimer);
                break;
            case CASTING:
                region = (TextureRegion)abilityToCastAnimation.getKeyFrame(stateTimer, true);
                //region = (TextureRegion) castAnimation.getKeyFrame(stateTimer, true);
                break;
            case KICKING:
                region = (TextureRegion)abilityToCastAnimation.getKeyFrame(stateTimer, true);
                //region = (TextureRegion) kickAnimation.getKeyFrame(stateTimer, true);
                break;
            case SHOTING:
                region = (TextureRegion)abilityToCastAnimation.getKeyFrame(stateTimer, true);
                //region = (TextureRegion) shotAnimation.getKeyFrame(stateTimer, true);
                break;

            default:
                region = stand;
        }

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
            if (body.getLinearVelocity().y > 0.1)
                return State.JUMPING;
            else if (body.getLinearVelocity().y < -0.1)
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

    // ACTIONS
    public void jump() {
//        if (getState() == State.CASTING || getState() == State.SHOTING || getState() == State.KICKING){
//            resetTimeSpentOnCast();
//        }

            //TODO
//            body.getFixtureList().get(0).setDensity(10);
//            body.resetMassData();

            if ( currentState != State.JUMPING && currentState != State.FALLING  ) {
                //getBody().setLinearVelocity(0,0);
                //getBody().applyLinearImpulse(new Vector2(directionRight?2*stats.speed.current:-2*stats.speed.current, (IN_BATTLE)?JUMP_BASE/2:JUMP_BASE * stats.jumphight.current), getBody().getWorldCenter(), true);
                powerjump();
            }
    }

    public void stop() {
        if(!stuned)
            getBody().setLinearVelocity(0,getBody().getLinearVelocity().y);
    }

    public void powerjump() {
        getBody().applyLinearImpulse(new Vector2(0, JUMP_BASE * stats.jumphight.current), getBody().getWorldCenter(), true);
        currentState = State.JUMPING;
    }

    public void move(boolean moveright){
        directionRight = moveright;
//        if (getState() == State.CASTING || getState() == State.SHOTING || getState() == State.KICKING){
//            resetTimeSpentOnCast();
//        }
        if(!stuned){
            if(getBody().getLinearVelocity().y == 0){
            if(moveright && getBody().getLinearVelocity().x < 2*stats.speed.current)
                getBody().applyLinearImpulse(new Vector2( SPEED_BASE * stats.speed.current,0), getBody().getWorldCenter(), true);
                //getBody().applyLinearImpulse(new Vector2( (IN_BATTLE)?SPEED_BASE/2:SPEED_BASE * stats.speed.current,0), getBody().getWorldCenter(), true);
            else
                if ( ! moveright && getBody().getLinearVelocity().x > -2*stats.speed.current)
                    getBody().applyLinearImpulse(new Vector2(-(SPEED_BASE) * stats.speed.current,0), getBody().getWorldCenter(), true);
            //getBody().applyLinearImpulse(new Vector2(-((IN_BATTLE)?SPEED_BASE/2:SPEED_BASE) * stats.speed.current,0), getBody().getWorldCenter(), true);
            } else {
                if(moveright && getBody().getLinearVelocity().x < 2*stats.speed.current)
                    getBody().applyLinearImpulse(new Vector2( SPEED_BASE,0), getBody().getWorldCenter(), true);
                    //getBody().applyLinearImpulse(new Vector2( (IN_BATTLE)?SPEED_BASE/2:SPEED_BASE * stats.speed.current,0), getBody().getWorldCenter(), true);
                else
                if ( ! moveright && getBody().getLinearVelocity().x > -2*stats.speed.current)
                    getBody().applyLinearImpulse(new Vector2(-(SPEED_BASE),0), getBody().getWorldCenter(), true);

            }
        }
    }

    public void useAbility(AbilityID ability){
        if(!stuned)
        {
            if(checkCooldownExpired(ability)) {
                abilityToCastExecutionTime = AbilityHandler.getAbilityCastTime(this, ability);
                //if(abilityToCastExecutionTime > 0.2){
                setState(ability.getState());
                //}
                setAbilityToCast(ability);
            } else {
                statusbar.addMessage( String.format("%s not ready: %.1f",ability.getName(),this.showWhenAbilityWillBeAvailable(ability)) , existingTime + 2d, Fonts.INFO);
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
            getBody().setLinearVelocity(0, 0); // STOP
            cooldowns.put(ability, AbilityHandler.getAbilityCooldownTime(this,ability) + existingTime);
            setIN_BATTLE(true);
            Gdx.app.log("Creature", "Locked " + ability);
    }

    private boolean checkCooldownExpired(AbilityID ability){
        return cooldowns.get(ability) <= existingTime;
    }

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
                    com.mygdx.game.tools.EffectsHandler.resetEffect(this, effect.id, effect.magnitude);
                    effectsToRemove.add(i);
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
                com.mygdx.game.tools.EffectsHandler.resetEffect(this, effect.id, effect.magnitude);
                effectsToRemove.add(i);
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
            if(effect.equals(activeEffects.get(i)) ){
                activeEffects.removeIndex(i);
                break;
            }
        }

    }

    // add item to inventory
    public void addToInventory(GameItem item) {
        //if(!toDestroy && !item.toDestroy) {
        if(canPickUpObjects){ // TODO make all Humans pickup objects
            item.destroyBody();
            inventory.add(item);
        }
    }

    // put item on and apply effect
    public void equipItem(GameItem item){

        for(Effect curEffect : item.getEffects()){
            applyEffect(curEffect);
        }
        switch (item.getType()){
            case HEAD:
                head = item;
                break;
            case ARMOR:
                armor = item;
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
                if(weapon1 == null)
                    weapon1 = item;
                else if (weapon2 == null)
                    weapon2 = item;
                break;
        }
        inventory.removeValue(item,true);
        if(statusbar != null)
            statusbar.update();
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
                if(weapon1 != null)
                    weapon1 = null;
                else if (weapon2 != null)
                    weapon2 = null;
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

    public void throwFromInventory() {
        if(inventory.size > 0)
            throwFromInventory(inventory.get(0));
    }

    //use item
    public void useItem(GameItem item) {
        for(Effect curEffect : item.getEffects()){
            applyEffect(curEffect);
        }
        inventory.removeValue(item,true);
    }

    public Array<GameItem> getInventory() {
        return inventory;
    }

    public void setAbilityToCast(AbilityID abilityToCast) {
        if(abilityToCast != AbilityID.NONE)
            this.abilityToCastAnimation = AbilityHandler.getAnimation(screen, abilityToCast, spritesheetRegion);
        this.abilityToCast = abilityToCast;
    }

    public boolean finishedCasting() {
        return timeSpentOnCast > abilityToCastExecutionTime;
    }

//    public Integer getID() {
//        return ID;
//    }

    public void toDie(){
        Gdx.app.log("Dead",name + " ");
        toDestroy = true;
        setState(State.DEAD);
        int inventorySize = getInventory().size;
        for(int i =0; i<inventorySize;i++)
            throwFromInventory(getInventory().get(0));
    }

    @Override
    public void draw(Batch batch) {
        if(!destroyed) {
            try {
                super.draw(batch);
                statusbar.draw(batch);
            }catch(Exception e){ //TODO
         //       batch.draw(region, getX()/100,getY()/100); //TODO
         //       statusbar.draw(batch);
            }
            //creatureAim.draw(batch);
        }else
            batch.draw(deadBody, getX(),getY(),getWidth(),getHeight());
    }

    public boolean isEnemy(Creature creature) {
        //if (this.enemyOrganizations.contains(creature.getOrganization()))
        if(this.organization != creature.getOrganization())
            return true;
        return false;
    }

    public int getOrganization() {
        return organization;
    }

    public void activateTrigger(com.mygdx.game.sprites.triggers.Trigger trigger) {
        TriggerHandler.runProcess(this, trigger);
    }

    public Array<AbilityID> getAbilities() {
        return abilities;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setInvisible(boolean b) {
        if(!b)
            removeEffectByID(EffectID.INVISIBLE);
        hidden = b;
    }



    public void setStun(boolean stuned) {
        this.stuned = stuned;
        //resetTimeSpentOnCast();
        if(stuned) lockAbility(abilityToCast);
    }

//    public void pushIt() {
//        resetTimeSpentOnCast();
//    }

    public int getToughness() {
        int result = stats.health.current + getReputation();
        return result;
    }

    public int getReputation() {
        return reputation;
    }

    public double showWhenAbilityWillBeAvailable(AbilityID ability) {
        if(cooldowns.get(ability) - existingTime <= 0)
            return 0;
        return Math.round(cooldowns.get(ability) - existingTime);
    }


    public Effect getEffect(EffectID id) {
        for(int i = activeEffects.size-1;i>=0;i--){
            if(activeEffects.get(i).id == id){
                return activeEffects.get(i);
            }
        }
        return null;
    }

    public float getEffectsSum(EffectID id) {
        float result = 0;
        for(int i = activeEffects.size-1;i>=0;i--){
            if(activeEffects.get(i).id == id){
                result = result + activeEffects.get(i).magnitude;
            }
        }
        return result;
    }

    public void setMoveLeft(boolean b) {
        this.brain.setMoveLeft(b);
    }

    public void setMoveRight(boolean b) {
        this.brain.setMoveRight(b);
    }

    public void setHasToJump(boolean hasToJump) {
        this.brain.setHasToJump(hasToJump);
    }

    public void setStandStill(boolean b) {
        this.brain.setStandStill(b);
    }

    // to status bar
    public void addStatusMessage(String message, Fonts font) {
        if(statusbar != null)
            statusbar.addMessage(message, existingTime + 2f, font);
    }

    public void setNeighbor(Creature neighbor) {
        this.closeNeighbor = neighbor;
    }

    public Array<Integer> getDialogs() {
        return dialogs;
    }

    public void makeActive() {
        isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void touchObject(GameObject object) {
        if (object.getType() == GameObjectType.DOOR) {
            if (checkInInventory(object.getRequiredKey())) {
                object.destroyBody();
            }
        }
        if (object.getType() == GameObjectType.CHEST) {
            if (checkInInventory(object.getRequiredKey())) {
                inventory.addAll(object.items);
                object.destroyBody();
            }
        }
        if (object.getType() == GameObjectType.SPIKE){
            doDamage(5);
        }
    }

    private void doDamage(int damageValue) {
        stats.health.current = stats.health.current - damageValue;
        addStatusMessage(String.valueOf(damageValue), Fonts.BAD);
    }

    private boolean checkInInventory(String itemID) {
        for(GameItem item: inventory){
            if(item.id.equals(itemID))
                return true;
        }
        return false;
    }
}
