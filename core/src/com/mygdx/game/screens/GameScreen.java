package com.mygdx.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.State;
import com.mygdx.game.scenes.*;
import com.mygdx.game.sprites.activities.ActivityWithEffect;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.sprites.gameobjects.GameObject;
import com.mygdx.game.tools.AnimationHelper;
import com.mygdx.game.tools.LevelManager;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.mygdx.game.PalidorGame.PPM;

/**
 * Created by odiachuk on 12/16/17.
 */
public class GameScreen implements Screen {


    private PalidorGame game;
    float timeSinceLastRender;

    //Texture bg;

    public Hero hero;

    boolean GAME_FINISHED = false;

    public World world;

    OrthographicCamera camera;

    public OrthogonalTiledMapRenderer maprenderer;

    Viewport viewport;

    public Box2DDebugRenderer debugRenderer;

    public DialogPanel dialogPanel;
    ControllerPanel controller;
    HeroInventoryPanel heroInventoryPanel;
    HeroAbilitiesPanel heroAbilitiesPanel;

    public LevelManager levelmanager;

    public AnimationHelper animationHelper;

    boolean PAUSE;
    boolean onMainScreen = true;
    boolean onInventoryScreen = false;
    boolean onAbilitiesScreen = false;
    boolean onDialogScreen = false;

    double CURRENTTIME = 0;

    float holdingTimeUP = 0;
    float holdingTimeDOWN = 0;
    float holdingTimeABILITY1 = 0;
    float holdingTimeABILITY2 = 0;
    float holdingTimeJUMP = 0;
    float holdingTimeUSE = 0;
    float summonButtonHoldingTime = 0;

    boolean jumpWasJustPressed;
    double whenJumpWasJustPressed;
    boolean useWasJustPressed;
    double whenUseWasJustPressed;
    boolean a1WasJustPressed;
    double whenA1WasJustPressed;
    boolean a2WasJustPressed;
    double whenA2WasJustPressed;

    AbilityID abilityToExecuteNext = null;
    int abilityIndex = 0;

    boolean toZoomIn = false;
    boolean toZoomOut = false;
    float ZOOM_LEVEL = 1;
    private boolean STOP_GAME;

    Vector2 mouseVector;

    Array<Integer> itemsToRemove;

    int[] layersToRender; //day and night
    boolean isDay;

    FPSLogger fpslogger;

    double shakeTime = -4;
    boolean shakeRight = true;

    private double slowdownTime;
    int curSlowdownFrame;


    boolean TO_RENDER = false;
    public RandomXS128 randomizer;


    public Array<String> creaturesToCreate;
    public Array<String> creaturesToDie;
    public Array<String> itemsToCreate;
    public Array<ActivityWithEffect> activitiesToCreate;

    public GameScreen(PalidorGame game, String heroName){
        this(game,heroName,null);
    }

    public GameScreen(PalidorGame game, String heroName, String newHeroType) {
        this.game = game;
        this.game.currentHero = heroName;
        fpslogger = new FPSLogger();
        setDay(true);

        STOP_GAME = false;

        randomizer = new RandomXS128();

        //camera
        camera = new OrthographicCamera();
      //  camera.setToOrtho(false,PalidorGame.WIDTH,PalidorGame.HIGHT);
      //  camera.position.set(PalidorGame.WIDTH / 2, PalidorGame.HIGHT / 2, 0);

        viewport = new FitViewport(PalidorGame.WIDTH/ PPM , PalidorGame.HIGHT/ PPM, camera);

        animationHelper = new AnimationHelper();

        animationHelper.greenAuraAnimation = animationHelper.getAnimationByID("green_aura", 64, 64, 0.1f, 0);
        animationHelper.yellowAuraAnimation= animationHelper.getAnimationByID("yellow_aura", 64, 64, 0.1f, 0);
        animationHelper.redAuraAnimation= animationHelper.getAnimationByID("red_aura", 64, 64, 0.1f, 0);

        //controller
        controller = new ControllerPanel(game.getBatch(),animationHelper);

        //set camera to center
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        //load level
        levelmanager = new LevelManager(this); // load all creatures and items

        if(newHeroType != null) {
            hero = levelmanager.createNewHero(heroName,newHeroType);
        } else
            hero = levelmanager.loadHero(heroName);

        levelmanager.loadLevel(hero.currentLevel, hero.name);

        creaturesToCreate = new Array<String>();
        creaturesToDie = new Array<String>();
        itemsToCreate = new Array<String>();
        activitiesToCreate = new Array<ActivityWithEffect>();

        //penels
        dialogPanel = new DialogPanel(game.getBatch(), this);
        heroInventoryPanel = new HeroInventoryPanel(game.getBatch(), hero, animationHelper);
        heroAbilitiesPanel = new HeroAbilitiesPanel(game.getBatch(), hero, animationHelper);

        //controller
        controller.update(hero);

        PAUSE = false;

        //Gdx.input.setInputProcessor(new SimpleGestureDetector(new SimpleGestureDetector.DirectionListener());
        mouseVector = new Vector2(1,1);

        itemsToRemove = new Array<Integer>();

    }

    private void setDay(boolean b) {
        isDay = b;
        if(b)
        layersToRender = new int[]{0, 2};
        else
            layersToRender = new int[]{1, 2};
    }


    @Override
    public void show() {

    }

    void update(float delta){

//        if(slowdownTime > hero.existingTime) {
//            hero.existingTime = hero.existingTime + delta;
//            if(curSlowdownFrame<1)
//                return;
//        }

        Array<ActivityWithEffect> activity;

        handleInput(delta);

        // handle camera after hero position update //

//            if (hero.getBody().getPosition().x > viewport.getWorldWidth() / 2 )
                camera.position.x = (hero.getBody().getPosition().x );
//            else
//                camera.position.x = viewport.getWorldWidth() / 2;
//            if (hero.getBody().getPosition().y > viewport.getWorldHeight() / 2)
                camera.position.y = (hero.getBody().getPosition().y );
//            else
//                camera.position.y = viewport.getWorldHeight() / 2;

        if(shakeTime > hero.existingTime)
            if(shakeRight) {
                camera.position.y = camera.position.y + 0.05f;
                shakeRight = false;
            }else {
                camera.position.y = camera.position.y - 0.05f;
                shakeRight = true;
            }


            if(toZoomIn) {
                ZOOM_LEVEL = 2f ;
                camera.zoom = 0.5f;
                viewport.setWorldSize(PalidorGame.WIDTH /2 / PPM , PalidorGame.HIGHT/ 2/ PPM);
                toZoomIn = false;
            }

            if(toZoomOut) {
                ZOOM_LEVEL = 0.5f;
                camera.zoom = 2f;
                viewport.setWorldSize(PalidorGame.WIDTH/ 2*PPM , PalidorGame.HIGHT/ PPM);
                toZoomOut = false;
            }


        if(!PAUSE) {

            CURRENTTIME = CURRENTTIME + delta; // time to use

            //update cooldowns
            controller.updateLabels(hero);

            //update hero
            hero.update(delta);

            //summon creature
            if(creaturesToCreate.size > 0) {
                for(String line : creaturesToCreate) {


                    if(line.contains(":")) {
                        String typeOfCreature = line.split(":")[1];
                        String conditionValue = line.split(":")[2];
                        float newCreatureX = Float.valueOf(conditionValue.split(",")[0]);
                        float newCreatureY = Float.valueOf(conditionValue.split(",")[1]);
                        int newCreatureOrg = Integer.valueOf(conditionValue.split(",")[2]);
                        hero.screen.levelmanager.createCreature(hero.screen, newCreatureX, newCreatureY, typeOfCreature, newCreatureOrg);
                    } else
                        for (LevelManager.UnavailableCreatures creature : hero.screen.levelmanager.UNAVAILABLE_CREATURES)
                            if (creature.getUid().equals(line))
                                hero.screen.levelmanager.createCreatureFromUnavailableCreature(creature);

                };
                creaturesToCreate.clear();

            }

            //kill creature
            if(creaturesToDie.size > 0) {
                for(String line : creaturesToDie) {
                    for(Creature current: hero.screen.levelmanager.CREATURES) {
                        if( current.getID() == Integer.valueOf(line))
                            current.toDie();
                    }
                };
                creaturesToDie.clear();

            }

            //summon item
            if(itemsToCreate.size > 0) {
                for(String line : itemsToCreate) {
                    String typeOfItem = line.split(":")[1];
                    String conditionValue = line.split(":")[2];
                    float newItemX = Float.valueOf(conditionValue.split(",")[0]);
                    float newItemY = Float.valueOf(conditionValue.split(",")[1]);
                    hero.screen.levelmanager.createItemObject(hero.screen, newItemX, newItemY, typeOfItem );
                };
                itemsToCreate.clear();

            }

            //start activities
            if(activitiesToCreate.size > 0) {
                for(ActivityWithEffect act : activitiesToCreate){
                    act.createBody();
                }
                    hero.screen.levelmanager.ACTIVITIES.addAll(activitiesToCreate);
                activitiesToCreate.clear();
            }

            if (hero.getAbilityToCast() != AbilityID.NONE && hero.finishedCasting()) {
                activity = hero.activateAbility(hero.getAbilityToCast());
                if (activity != null)
                    levelmanager.ACTIVITIES.addAll(activity);
            }

            //update all enemies
            for (Creature creature : levelmanager.CREATURES) {
//                if(!creature.isActive()) { //TODO add validation on activ state
//                    if (creature.getBody().getPosition().x < camera.position.x + viewport.getWorldWidth() / 2 && creature.getBody().getPosition().x > camera.position.x - viewport.getWorldWidth() / 2)
//                        creature.makeActive();
//                }else
                {
                    //if(hero.IN_BATTLE)
                    //        if(hero.isEnemy(creature))
                                //creature.startAnimation(animationHelper.redAuraAnimation);
                    //          //  creature.setAura();
                    //        else
                                //creature.startAnimation(animationHelper.yellowAuraAnimation);
                    //        //creature.setAura();


                    creature.update(delta);
                    //creature.weaponSprite.update(delta);
                    creature.nextStep(delta);

                    if (creature.getAbilityToCast() != AbilityID.NONE && creature.finishedCasting()) {
                        activity = creature.activateAbility(creature.getAbilityToCast());
                        if (activity != null)
                            levelmanager.ACTIVITIES.addAll(activity);
                    };
                }
            }


            //update all summoned
            for (Creature creature : levelmanager.SUMMONED_CREATURES) {
//                if(creature.owner != null && creature.owner.getID() == hero.getID())
//                    creature.startAnimation(animationHelper.greenAuraAnimation);
//                else
//                    if(hero.isEnemy(creature))
//                        creature.startAnimation(animationHelper.redAuraAnimation);
//                    else
//                        creature.startAnimation(animationHelper.yellowAuraAnimation);

                    creature.update(delta);

                    creature.nextStep(delta);

                    if (creature.getAbilityToCast() != AbilityID.NONE && creature.finishedCasting()) {
                        activity = creature.activateAbility(creature.getAbilityToCast());
                        if (activity != null)
                            levelmanager.ACTIVITIES.addAll(activity);
                    };
                }

            //update all objects
            for (int i = 0; i < levelmanager.OBJECTS.size; i++){
                GameObject object = levelmanager.OBJECTS.get(i);
                object.update(delta);
                if (object.isDestroyed())
                    levelmanager.OBJECTS.removeIndex(i);
            }

            //update items
            for (int i = 0; i < levelmanager.ITEMS.size; i++) {
                GameItem item = levelmanager.ITEMS.get(i);
                item.update(delta);
                if (item.isDestroyed())
                    levelmanager.ITEMS.removeIndex(i);
                    //itemsToRemove.add(i);
            }

            //update activities
            for (int i = 0; i < levelmanager.ACTIVITIES.size; i++) {
                ActivityWithEffect activityWithEffect = levelmanager.ACTIVITIES.get(i);
                activityWithEffect.update(delta);
                if (activityWithEffect.isDestroyed())
                    levelmanager.ACTIVITIES.removeIndex(i);
            }

            // update world
            world.step(Gdx.graphics.getDeltaTime(), 6, 2);

            //update camera
            camera.update();

            maprenderer.setView(camera); // make Map renderer render only part in a cam

        } else {
            //System.out.println("pause");
        }
    }

    private void handleInput(float delta) {

        Vector3 coord;

        if (Gdx.input.isKeyJustPressed(Input.Keys.I) || controller.inventoryToched || heroInventoryPanel.closeTouched) {

            heroInventoryPanel.closeTouched = false;
            controller.inventoryToched = false;

            if (onMainScreen) {
                heroInventoryPanel.update();
                onInventoryScreen = true;
                onMainScreen = false;
                PAUSE = true;
                Gdx.graphics.setContinuousRendering(false);
            } else {
                controller.update(hero);
                onInventoryScreen = false;
                onMainScreen = true;
                PAUSE = false;
                Gdx.graphics.setContinuousRendering(true);
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.O) || controller.abilitiesToched || heroAbilitiesPanel.closeTouched) {
            heroAbilitiesPanel.closeTouched = false;
            controller.abilitiesToched = false;

            if (onMainScreen) {
                heroAbilitiesPanel.update();
                onAbilitiesScreen = true;
                onMainScreen = false;
                PAUSE = true;
                Gdx.graphics.setContinuousRendering(false);
            } else {
                controller.update(hero);
                onAbilitiesScreen = false;
                onMainScreen = true;
                PAUSE = false;
                Gdx.graphics.setContinuousRendering(true);
            }
        }

        //TODO temp
        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            setDay(!isDay);
        }

        if (PAUSE) {
//            if(Gdx.input.justTouched()) {
//                 coord = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
//                getInfoFromObjectByCoordinates(coord.x,coord.y);
//            }
            if (onDialogScreen)
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched())
                    if (dialogPanel.currentReplic == 0) endDialog();
        } else {
            ActivityWithEffect activity = null;

            // update aim

            /*
                coord = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                mouseVector.set(-(coord.x - hero.getBody().getPosition().x), coord.y - hero.getBody().getPosition().y);
                //hero.direction.set(mouseVector.x > 1 ? 1: (mouseVector.x < -1 ? -1f : mouseVector.x), mouseVector.y > 1 ? 1:(mouseVector.y < -1 ? -1f : mouseVector.y));
                hero.direction.set(mouseVector.x, mouseVector.y).clamp(1, 1);
                //hero.direction = hero.direction.rotateRad(mouseVector.angleRad(hero.direction));
*/

//            if(holdingTimeUP > 0) {
//                if (!controller.touchedUp && !Gdx.input.isKeyPressed(Input.Keys.W)){
//                    if(holdingTimeUP > 3)
//                        hero.shout();
//                    //hero.statusbar.resetCastbar();
//                    holdingTimeUP = 0;
//                }
// else
//                    hero.statusbar.updateCastBar("Shout", holdingTimeUP, 1f);
//            }
//            if(holdingTimeDOWN > 0) {
//                if (!controller.touchedDown && !Gdx.input.isKeyPressed(Input.Keys.S)){
//                    if(holdingTimeDOWN > 3)
//                        hero.hide();
//                    holdingTimeDOWN = 0;
//                    //hero.statusbar.resetCastbar();
//                }
            //else
//                hero.statusbar.updateCastBar("Hiding...", holdingTimeDOWN, 1f);


            //TODO holding jump button
//            if(holdingTimeJUMP > 0) {
//                if (!controller.touchedJump && !Gdx.input.isKeyPressed(Input.Keys.UP)){
//                    if(holdingTimeJUMP < 1)
//                        hero.jump();
//                    else
//                        if (holdingTimeUP>0)
//                            hero.fly();
//                        else if (holdingTimeDOWN>0)
//                            hero.jumpBack();
//                        else if (controller.touchedLeft || Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.touchedRight || Gdx.input.isKeyPressed(Input.Keys.RIGHT) )
//                            hero.dash();
//                    else
//                            hero.jump();
//                    holdingTimeJUMP = 0;
//                }
//            }


//
//            if (holdingTimeABILITY1> 0) {
//                if (! controller.touchedAbility1 && !Gdx.input.isKeyPressed (Input.Keys.A)) {
//                    if (holdingTimeABILITY1 <2)
//                        hero.attack (true, false);
//                    if (holdingTimeABILITY1>= 2)
//                        hero.attack (true, true);
//
//                    holdingTimeABILITY1 = 0;
//                    }
//            }
//
//            if (holdingTimeABILITY2> 0) {
//                if (! controller.touchedAbility2 &&! Gdx.input.isKeyPressed (Input.Keys.D)) {
//                    if (holdingTimeABILITY2 <2)
//                        hero.attack (false, false);
//                    if (holdingTimeABILITY2>= 2)
//                        hero.attack (false, true);
//
//                holdingTimeABILITY2 = 0;
//                }
//            }


            if (controller.touchedUp || Gdx.input.isKeyPressed(Input.Keys.W)) {
                //holdingTimeUP = holdingTimeUP + delta;
                hero.direction.set(hero.direction.x, 1);
            } else if (controller.touchedDown || Gdx.input.isKeyPressed(Input.Keys.S)) {
                //holdingTimeDOWN = holdingTimeDOWN + delta;
                hero.direction.set(hero.direction.x, -1);
            } else hero.direction.set(hero.direction.x, 0);

            if (controller.touchedLeft || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                hero.direction.set(-1, hero.direction.y);
//                for (Creature creature : levelmanager.SUMMONED_CREATURES) {
//                    creature.move(false);
//                    if (hero.directionRight) creature.jump(); // change position
//                    creature.move(false);
//
//                }
                hero.move(false);
                //Gdx.app.log(hero.getBody().getLinearVelocity().x+"", hero.getBody().getLinearVelocity().y + "");

            } else if (controller.touchedRight || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                hero.direction.set(1, hero.direction.y);
//                for (Creature creature : levelmanager.SUMMONED_CREATURES) {
//                    creature.move(true);
//                    if (!hero.directionRight) creature.jump(); // change position
//                    creature.move(true);
//                }
                hero.move(true);
                //Gdx.app.log(hero.getBody().getLinearVelocity().x+"", hero.getBody().getLinearVelocity().y + "");
            } //else hero.direction.set(0, hero.direction.y);


            if (controller.touchedJump || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                //holdingTimeJUMP = holdingTimeJUMP + delta;

                if (whenJumpWasJustPressed + 0.5 > hero.existingTime) {
                    //hero.haste();
                    hero.powerjump();
                } else if (whenUseWasJustPressed + 0.5 > hero.existingTime) {
                    hero.dash();
                } else {
                    //hero.powerjump();
                    hero.jump();
//                    for (Creature creature : levelmanager.SUMMONED_CREATURES) {
//                        creature.jump();
//                    }
                    whenJumpWasJustPressed = hero.existingTime;
                }
                controller.touchedJump = false;
            }
            if (controller.touchedUse || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                controller.touchedUse = false;

                if (hero.getNeighbor() != null)
                    startDialog(hero.getNeighbor());
                else if (!hero.onAGround){
                    hero.fly();
                }
                else {
                    if (whenUseWasJustPressed + 0.5 > hero.existingTime) {
                        if (hero.isHidden())
                            hero.pickpocket();
                        else
                            hero.hide();

                    } else {
                        hero.stop();
                        whenUseWasJustPressed = hero.existingTime;
                    }
                }

            }
            if (controller.touchedAbility1 || Gdx.input.isKeyJustPressed(Input.Keys.A)) {

                if (whenUseWasJustPressed + 0.5 > hero.existingTime) {
                    hero.attack(true, true);
                } else {
                    hero.attack(true, false);
                    whenA1WasJustPressed = hero.existingTime;
                }

                //holdingTimeABILITY1 = holdingTimeABILITY1 + delta;
            }
            if (controller.touchedAbility2 || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                if (whenUseWasJustPressed + 0.5 > hero.existingTime) {
                    hero.attack(false, true);
                } else {
                    hero.attack(false, false);
                    whenA2WasJustPressed = hero.existingTime;
                }
                //holdingTimeABILITY2 = holdingTimeABILITY2 + delta;
            }

            if (controller.touchedSummon0 || Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
                summonButtonHoldingTime = summonButtonHoldingTime + delta;
            }

            if (!controller.touchedSummon0 && !Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
                if (summonButtonHoldingTime > 0) {
                    if (summonButtonHoldingTime > 1) {
                        hero.summonCreature(controller.currentSummonCreatureIndex);
                    } else {
                        controller.currentSummonCreatureIndex++;
                        if (controller.currentSummonCreatureIndex > hero.summonAbilities.size - 1)
                            controller.currentSummonCreatureIndex = 0;
                        controller.update(hero);
                    }
                    ;
                    summonButtonHoldingTime = 0;
                }
            }

            //hero.direction.clamp(1, 1); // TODO make sure it works correctly


            //if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            //    hero.direction = hero.direction.rotate(30);
            //if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            //    hero.direction = hero.direction.rotate(-30);


            if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
                hero.throwFromInventory();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                toZoomIn = true;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                toZoomOut = true;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                TO_RENDER = !TO_RENDER;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || controller.escapeTouched) {
                STOP_GAME = true;
            }

            //     }
            // }
        }
    }


    // for trigger messages
    public void showDialog(){
        PAUSE = true;
        onDialogScreen = true;
        Gdx.graphics.setContinuousRendering(false);
        onMainScreen = false;

        dialogPanel.update();
        dialogPanel.currentReplic = 0;
    }

    public void startDialog(Creature actor){
        if(actor.getDialogs() != null && actor.getDialogs().size > 0) {

            //short dialog processing
            if(dialogPanel.checkAndProcessIfDialogIsShort(actor))
                return;

            //long dialog processing
            PAUSE = true;

            onDialogScreen = true;
            Gdx.graphics.setContinuousRendering(false);
            onMainScreen = false;

            dialogPanel.setDialogs(actor);
            dialogPanel.update();
        }
    }

    public void endDialog(){
        dialogPanel.reset();
        PAUSE = false;
        onDialogScreen = false;
        Gdx.graphics.setContinuousRendering(true);
        onMainScreen = true;

        controller.update();

    }


//    private void getInfoFromObjectByCoordinates(float x, float y) {
//        for(GameItem item : levelmanager.ITEMS){
//            if(item.getX() < x && item.getX() + item.getWidth() > x && item.getY() < y && item.getY() + item.getHeight() > y){
//                Gdx.app.log("Details", item.itemdescription);
//            } //else Gdx.app.log("Details", item.getX() + " - " + x + "-" + (item.getX() + item.getWidth()) + " | " + item.getY() + " - " + y + "-" + (item.getY() + item.getHeight()) + y);
//        }
//        for(Creature item : levelmanager.CREATURES){
//            if(item.getX() < x && item.getX() + item.getWidth() > x && item.getY() < y && item.getY() + item.getHeight() > y){
//                Gdx.app.log("Details", item.creatureDescription);
//            } //else Gdx.app.log("Details", item.getX() + " - " + x + "-" + (item.getX() + item.getWidth()) + " | " + item.getY() + " - " + y + "-" + (item.getY() + item.getHeight()) + y);
//        }
//    }


    @Override
    public void render(float delta) {
//        timeSinceLastRender += delta;
//        if (timeSinceLastRender >= PalidorGame.MIN_FRAME_LENGTH) {
//            // Do the actual rendering, pass timeSinceLastRender as delta time.
//            Gdx.graphics.requestRendering();
//            timeSinceLastRender = 0f;
            //System.out.println(timeSinceLastRender);

//        System.out.println(Gdx.graphics.getDeltaTime());


        update(delta);


        //fpslogger.log();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //TODO slowtime
//        if(slowdownTime > hero.existingTime) {
//            curSlowdownFrame ++;
//
//            if(curSlowdownFrame == 3)
//                curSlowdownFrame = 0;
//
//            if(curSlowdownFrame<1)
//                return;
//            return;
//        }



        {

            //maprenderer.render(); // show map
            maprenderer.render(layersToRender);


            game.getBatch().setProjectionMatrix(camera.combined);
            game.getBatch().begin();

            //levelmanager.background.draw(game.getBatch()); // TODO background


            //render all objects
            for (GameObject object : levelmanager.OBJECTS) {
                object.draw(game.getBatch());
            }


            //render all enemies
            for (Creature creature : levelmanager.CREATURES) {

                    creature.draw(game.getBatch());
                //creature.weaponSprite.draw(game.getBatch());
            }

            //render all summoned
            for (Creature creature : levelmanager.SUMMONED_CREATURES) {

                    creature.draw(game.getBatch());
                //creature.weaponSprite.draw(game.getBatch());
            }

            //render hero
            hero.draw(game.getBatch());


            //render all items
            for (GameItem item : levelmanager.ITEMS) {
                if (!item.isDestroyed())
                    item.draw(game.getBatch());
            }

            //render all activities
            for (ActivityWithEffect activity : levelmanager.ACTIVITIES) {
                if (!activity.isDestroyed())
                    //try {
                    //TODO fix
                    //game.getBatch().draw(activity.region, activity.getX(), activity.getY(), activity.region.getRegionWidth() / PPM / 2, activity.region.getRegionHeight() / PPM / 2, activity.region.getRegionWidth() / PPM, activity.region.getRegionHeight() / PPM, 1, 1, (activity.direction.angle()));
                    //}catch(Exception e){
                    activity.draw(game.getBatch());
                //}
                //activity.draw(game.getBatch());
                //game.getBatch().draw(activity.getFrame(delta), activity.getX(), activity.getY(), activity.getFrame(delta).getRegionWidth() / PPM / 2, activity.getFrame(delta).getRegionHeight() / PPM / 2, activity.getFrame(delta).getRegionWidth() / PPM, activity.getFrame(delta).getRegionHeight() / PPM, 1, 1, (activity.direction.angle()));

            }


            //hero.weaponSprite.draw(game.getBatch());
            //if(hero.armorSprite.pictureName != null)
            //    hero.armorSprite.draw(game.getBatch());
            //hero.creatureAim.draw(game.getBatch()); // aming

            game.getBatch().end();

            //render boxes
            if(TO_RENDER)
                debugRenderer.render(world, camera.combined); // render boxes from World


            //show info panel
//            game.getBatch().setProjectionMatrix(dialogPanel.stage.getCamera().combined);
//            dialogPanel.stage.draw();

            //if (Gdx.app.getType() == Application.ApplicationType.Android)
                controller.stage.draw();

        }


        if(PAUSE) {
            if (onInventoryScreen){
                //show info panel
                //heroInventoryPanel.update();
                game.getBatch().setProjectionMatrix(heroInventoryPanel.stage.getCamera().combined);
                heroInventoryPanel.stage.draw();
            } else if (onAbilitiesScreen) {
                //heroAbilitiesPanel.update();
                game.getBatch().setProjectionMatrix(heroAbilitiesPanel.stage.getCamera().combined);
                heroAbilitiesPanel.stage.draw();
            }
            else if (onDialogScreen){
                game.getBatch().setProjectionMatrix(dialogPanel.stage.getCamera().combined);
                dialogPanel.stage.draw();
            }

        }


            if (gameOver()) {
                game.setScreen(new GameOverScreen(game));
                dispose();
            }

            if (GAME_FINISHED) {
                game.setScreen(new VictoryScreen(game));
                dispose();
            }

            if (STOP_GAME) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }


//        } else
//            System.out.println(timeSinceLastRender);
    }

    public boolean gameOver(){
        if(hero.getState() == State.DEAD){
            return true;
        }
        return false;
    }

    public boolean gameWin(String message){  // TODO extend
//        for(GameItem item : hero.getInventory()){
//            if(item.itemname.equals("Final Key")){
//
//            PalidorGame.gameDetails = "You found a Key!";
//            return true;
//            }
//        }
            PalidorGame.gameDetails = message;
            GAME_FINISHED = true;


        return true;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        controller.viewport.update(width,height);

        //TODO add other panels
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //dispose of all our opened resources
        maprenderer.dispose();
        world.dispose();
        debugRenderer.dispose();
        controller.dispose();
        dialogPanel.dispose();
        heroInventoryPanel.dispose();
        heroAbilitiesPanel.dispose();
        levelmanager.dispose();

    }

    private static BitmapFont buildFont(String filename, int size, String characters) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filename));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.characters = characters;
        parameter.kerning = true;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        BitmapFont font = generator.generateFont(parameter);
        font.getData().markupEnabled = true;
        font.getData().setScale(0.03f);
        font.setUseIntegerPositions(false);
        generator.dispose();
        return font;
    }


    public void shake(double length) {
        shakeTime = hero.existingTime + length;
    }

//    public void slowDown(double length) {
//        slowdownTime = hero.existingTime + length;
//        curSlowdownFrame = 0;
//    }
}
