package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.scenes.ControllerPanel;
import com.mygdx.game.scenes.HeroAbilitiesPanel;
import com.mygdx.game.scenes.HeroInventoryPanel;
import com.mygdx.game.sprites.activities.ActivityWithEffect;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.sprites.gameobjects.GameObject;
import com.mygdx.game.tools.AnimationHelper;
import com.mygdx.game.tools.LevelManager;

import static com.mygdx.game.PalidorGame.PPM;

/**
 * Created by odiachuk on 12/16/17.
 */
public class GameScreen implements Screen {


    private PalidorGame game;

    //Texture bg;

    public Hero hero;

    public World world;

    OrthographicCamera camera;

    public OrthogonalTiledMapRenderer maprenderer;

    Viewport viewport;

    public Box2DDebugRenderer debugRenderer;

    public com.mygdx.game.scenes.MainPanel mainPanel;
    public com.mygdx.game.scenes.InfoPanel infoPanel;
    com.mygdx.game.scenes.ControllerPanel controller;
    public com.mygdx.game.scenes.HeroInventoryPanel heroInventoryPanel;
    public com.mygdx.game.scenes.HeroAbilitiesPanel heroAbilitiesPanel;

    public com.mygdx.game.tools.LevelManager levelmanager;

    public com.mygdx.game.tools.AnimationHelper animationHelper;

    boolean PAUSE;
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

    boolean jumpWasJustPressed;
    boolean useWasJustPressed;
    boolean a1WasJustPressed;
    boolean a2WasJustPressed;

    com.mygdx.game.enums.AbilityID abilityToExecuteNext = null;
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

    public GameScreen(PalidorGame game, String heroName){
        this(game,heroName,null);
    }

    public GameScreen(PalidorGame game, String heroName, String newHeroType) {
        this.game = game;
        this.game.currentHero = heroName;
        fpslogger = new FPSLogger();
        setDay(true);

        STOP_GAME = false;

        //camera
        camera = new OrthographicCamera();
      //  camera.setToOrtho(false,PalidorGame.WIDTH,PalidorGame.HIGHT);
      //  camera.position.set(PalidorGame.WIDTH / 2, PalidorGame.HIGHT / 2, 0);

        viewport = new FitViewport(PalidorGame.WIDTH/ PPM , PalidorGame.HIGHT/ PPM, camera);

        animationHelper = new AnimationHelper();

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

        //info
        infoPanel = new com.mygdx.game.scenes.InfoPanel(game.getBatch(), levelmanager);

        //info
        heroInventoryPanel = new HeroInventoryPanel(game.getBatch(), hero, animationHelper);
        heroAbilitiesPanel = new HeroAbilitiesPanel(game.getBatch(), hero, animationHelper);

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

        Array<ActivityWithEffect> activity;

        handleInput(delta);

        // handle camera after hero position update
            if (hero.getBody().getPosition().x > viewport.getWorldWidth() / 2 ) //&& hero.getBody().getPosition().x < 32-viewport.getWorldWidth() / 2)
                camera.position.x = (hero.getBody().getPosition().x );
            else
                camera.position.x = viewport.getWorldWidth() / 2;
            if (hero.getBody().getPosition().y > viewport.getWorldHeight() / 2 )
                camera.position.y = (hero.getBody().getPosition().y );
            else
                camera.position.y = viewport.getWorldHeight() / 2;

            if(toZoomIn) {
                ZOOM_LEVEL = 2f ;
                camera.zoom = 0.5f;
                viewport.setWorldSize(PalidorGame.WIDTH /2 / PPM , PalidorGame.HIGHT/ 2/ PPM);
                toZoomIn = false;
            }

            if(toZoomOut) {
                ZOOM_LEVEL = 1f;
                camera.zoom = 1f;
                viewport.setWorldSize(PalidorGame.WIDTH/ PPM , PalidorGame.HIGHT/ PPM);
                toZoomOut = false;
            }


        if(!PAUSE) {

            CURRENTTIME = CURRENTTIME + delta; // time to use

            //update cooldowns
            controller.updateLabels(hero);

            //update hero
            hero.update(delta);

            if (hero.getAbilityToCast() != AbilityID.NONE && hero.finishedCasting()) {
                activity = hero.activateAbility(hero.getAbilityToCast());
                if (activity != null)
                    levelmanager.ACTIVITIES.addAll(activity);
            }

            //update all enemies
            for (Creature creature : levelmanager.CREATURES) {
                if(!creature.isActive()) {
                    if (creature.getBody().getPosition().x < camera.position.x + viewport.getWorldWidth() / 2 && creature.getBody().getPosition().x > camera.position.x - viewport.getWorldWidth() / 2)
                        creature.makeActive();
                }else {
                    creature.update(delta);
                    creature.nextStep();

                    if (creature.getAbilityToCast() != AbilityID.NONE && creature.finishedCasting()) {
                        activity = creature.activateAbility(creature.getAbilityToCast());
                        if (activity != null)
                            levelmanager.ACTIVITIES.addAll(activity);
                    };
                }
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

        }
    }

    private void handleInput(float delta) {

        Vector3 coord;

        if (Gdx.input.isKeyJustPressed(Input.Keys.I) || controller.inventoryToched || heroInventoryPanel.closeTouched) {
            heroInventoryPanel.closeTouched = false;
            controller.inventoryToched = false;
            onInventoryScreen = !onInventoryScreen;
            PAUSE = !PAUSE;
            if(!PAUSE)
                controller.update(hero);
            else
                heroInventoryPanel.update();
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.O) || controller.abilitiesToched || heroAbilitiesPanel.closeTouched) {
            heroAbilitiesPanel.closeTouched = false;
            controller.abilitiesToched = false;
            onAbilitiesScreen = !onAbilitiesScreen;
            PAUSE = !PAUSE;
            if(!PAUSE)
                controller.update(hero);
            else
                heroAbilitiesPanel.update();
        }

        //TODO temp
        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            setDay(!isDay);
        }

        if (PAUSE) {
            if(Gdx.input.justTouched()) {
                 coord = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                getInfoFromObjectByCoordinates(coord.x,coord.y);
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                endDialog();
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
            }

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
                }else if (controller.touchedDown || Gdx.input.isKeyPressed(Input.Keys.S)) {
                    //holdingTimeDOWN = holdingTimeDOWN + delta;
                    hero.direction.set(hero.direction.x, -1);
                }else hero.direction.set(hero.direction.x, 0);

                if (controller.touchedLeft || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    hero.direction.set(1, hero.direction.y);
                    hero.move(false);
                } else if (controller.touchedRight || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    hero.direction.set(-1, hero.direction.y);
                    hero.move(true);
                } //else hero.direction.set(0, hero.direction.y);


                if (controller.touchedJump || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    //holdingTimeJUMP = holdingTimeJUMP + delta;

                    if(jumpWasJustPressed) {
                        hero.powerjump();
                        jumpWasJustPressed = false;
                    } else {
                        hero.jump();
                        jumpWasJustPressed = true;
                    }
                    controller.touchedJump = false;
                }
                if (controller.touchedUse || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    controller.touchedUse = false;

                    if(hero.getNeighbor() != null)
                        startDialog(hero.getNeighbor());
                    else {
                        if (useWasJustPressed) {
                            if (hero.isHidden())
                                hero.useAbility(AbilityID.PICKPOCKET);
                            else
                                hero.hide();
                            useWasJustPressed = false;
                        } else {
                            hero.stop();
                            useWasJustPressed = true;
                        }
                    }

                }
                if (controller.touchedAbility1 || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    if(!a1WasJustPressed){
                        hero.attack(true, false);
                        a1WasJustPressed = true;
                    }else{
                        hero.attack(true, true);
                        a1WasJustPressed = false;
                    }
                    //holdingTimeABILITY1 = holdingTimeABILITY1 + delta;
                }
                if (controller.touchedAbility2 || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    if(!a2WasJustPressed){
                        hero.attack(false, false);
                        a2WasJustPressed = true;
                    }else{
                        hero.attack(false, true);
                        a2WasJustPressed = false;
                    }
                    //holdingTimeABILITY2 = holdingTimeABILITY2 + delta;
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

                    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)  || controller.escapeTouched) {
                        STOP_GAME = true;
                    }

           //     }
           // }
    }

    void startDialog(Creature actor){
        PAUSE = true;
        //TODO
        Gdx.app.log("Dialog", "Started'");

    }

    void endDialog(){
        PAUSE = false;
        Gdx.app.log("Dialog", "Closed'");
    }


    private void getInfoFromObjectByCoordinates(float x, float y) {
        for(GameItem item : levelmanager.ITEMS){
            if(item.getX() < x && item.getX() + item.getWidth() > x && item.getY() < y && item.getY() + item.getHeight() > y){
                Gdx.app.log("Details", item.itemdescription);
            } //else Gdx.app.log("Details", item.getX() + " - " + x + "-" + (item.getX() + item.getWidth()) + " | " + item.getY() + " - " + y + "-" + (item.getY() + item.getHeight()) + y);
        }
        for(Creature item : levelmanager.CREATURES){
            if(item.getX() < x && item.getX() + item.getWidth() > x && item.getY() < y && item.getY() + item.getHeight() > y){
                Gdx.app.log("Details", item.description);
            } //else Gdx.app.log("Details", item.getX() + " - " + x + "-" + (item.getX() + item.getWidth()) + " | " + item.getY() + " - " + y + "-" + (item.getY() + item.getHeight()) + y);
        }
    }


    @Override
    public void render(float delta) {

        //fpslogger.log();

        update(delta);

            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            //maprenderer.render(); // show map
        maprenderer.render(layersToRender);


            game.getBatch().setProjectionMatrix(camera.combined);
            game.getBatch().begin();

            //levelmanager.background.draw(game.getBatch()); // TODO background

            //render all items
            for (GameItem item : levelmanager.ITEMS) {
                if (!item.isDestroyed())
                    item.draw(game.getBatch());
            }


            //render all enemies
            for (Creature creature : levelmanager.CREATURES) {
                creature.draw(game.getBatch());
            }

        //render all objects
        for (GameObject object : levelmanager.OBJECTS) {
            object.draw(game.getBatch());
        }
            //render all activities
            for (ActivityWithEffect activity : levelmanager.ACTIVITIES) {
                if (!activity.isDestroyed())
                    try {
                    //TODO fix
                        //game.getBatch().draw(activity.region, activity.getX(), activity.getY(), activity.region.getRegionWidth() / PPM / 2, activity.region.getRegionHeight() / PPM / 2, activity.region.getRegionWidth() / PPM, activity.region.getRegionHeight() / PPM, 1, 1, (activity.direction.angle()));
                    }catch(Exception e){
                        activity.draw(game.getBatch());
                    }
                //activity.draw(game.getBatch());
                    //game.getBatch().draw(activity.getFrame(delta), activity.getX(), activity.getY(), activity.getFrame(delta).getRegionWidth() / PPM / 2, activity.getFrame(delta).getRegionHeight() / PPM / 2, activity.getFrame(delta).getRegionWidth() / PPM, activity.getFrame(delta).getRegionHeight() / PPM, 1, 1, (activity.direction.angle()));

            }

            hero.draw(game.getBatch());
            //hero.creatureAim.draw(game.getBatch()); //todo aiming

            game.getBatch().end();

            //render boxes
            debugRenderer.render(world, camera.combined); // render boxes from World

        if(PAUSE) {
            if (onInventoryScreen){
                //show info panel
                //heroInventoryPanel.update();
                game.getBatch().setProjectionMatrix(heroInventoryPanel.stage.getCamera().combined);
                heroInventoryPanel.stage.draw();
            } else if (onAbilitiesScreen){
                //heroAbilitiesPanel.update();
                game.getBatch().setProjectionMatrix(heroAbilitiesPanel.stage.getCamera().combined);
                heroAbilitiesPanel.stage.draw();
            } else if (onDialogScreen){
                infoPanel.stage.draw();
            }

        } else {

            //show info panel
            game.getBatch().setProjectionMatrix(infoPanel.stage.getCamera().combined);
            infoPanel.stage.draw();

            //if (Gdx.app.getType() == Application.ApplicationType.Android){
            controller.stage.draw();
        }


            if (gameOver()) {
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
            if (gameWin()) {
                game.setScreen(new VictoryScreen(game));
                dispose();
            }
        if (STOP_GAME) {
            game.setScreen(new com.mygdx.game.screens.MainMenuScreen(game));
            dispose();
        }
        //}
    }

    public boolean gameOver(){
        if(hero.getState() == com.mygdx.game.enums.State.DEAD){
            return true;
        }
        return false;
    }

    public boolean gameWin(){  // TODO extend
        for(GameItem item : hero.getInventory()){
            if(item.itemname.equals("Key from next level")){

            PalidorGame.gameDetails = "You found a Key!";
            return true;
            }
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        controller.viewport.update(width,height);
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
        infoPanel.dispose();
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

}
