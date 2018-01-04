package com.mygdx.game3.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game3.HuntersGame;
import com.mygdx.game3.scenes.HeroAbilitiesPanel;
import com.mygdx.game3.scenes.HeroInventoryPanel;
import com.mygdx.game3.sprites.activities.ActivityWithEffect;
import com.mygdx.game3.enums.*;
import com.mygdx.game3.sprites.creatures.Creature;
import com.mygdx.game3.sprites.gameobjects.GameItem;
import com.mygdx.game3.tools.*;
import com.mygdx.game3.scenes.ControllerPanel;
import com.mygdx.game3.scenes.InfoPanel;
import com.mygdx.game3.sprites.creatures.Hero;

/**
 * Created by odiachuk on 12/16/17.
 */
public class GameScreen implements Screen {


    private HuntersGame game;

    //Texture bg;

    public Hero hero;

    public World world;

    OrthographicCamera camera;

    public OrthogonalTiledMapRenderer maprenderer;

    Viewport viewport;

    public Box2DDebugRenderer debugRenderer;

    public InfoPanel infoPanel;
    ControllerPanel controller;
    public HeroInventoryPanel heroInventoryPanel;
    public HeroAbilitiesPanel heroAbilitiesPanel;

    public LevelManager levelmanager;

    public AnimationHelper animationHelper;
    public BitmapFont font;

    boolean PAUSE;
    boolean onInventoryScreen = false;
    boolean onAbilitiesScreen = false;

    double CURRENTTIME = 0;

    double timetoExecuteClickOnAbility = 0;
    AbilityID abilityToExecuteNext = null;
    int abilityIndex = 0;

    boolean toZoomIn = false;
    boolean toZoomOut = false;
    float ZOOM_LEVEL = 1;

    public GameScreen(HuntersGame game) {
        this.game = game;

        //load font
        font = new BitmapFont(); //buildFont("arial.ttf", 8, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*");
        font.getData().setScale(0.01f);
        font.setUseIntegerPositions(false);

        //camera
        camera = new OrthographicCamera();
      //  camera.setToOrtho(false,HuntersGame.WIDTH,HuntersGame.HIGHT);
      //  camera.position.set(HuntersGame.WIDTH / 2, HuntersGame.HIGHT / 2, 0);

        viewport = new FitViewport(HuntersGame.WIDTH/ HuntersGame.PPM , HuntersGame.HIGHT/ HuntersGame.PPM, camera);

        animationHelper = new AnimationHelper();

        //controller
        controller = new ControllerPanel(game.getBatch(),animationHelper);

        //set camera to center
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);


        //load level
        levelmanager = new LevelManager(this); // load all creatures and items

        levelmanager.loadLevel(HuntersGame.STARTING_LEVEL);

        //info
        infoPanel = new InfoPanel(game.getBatch(), levelmanager);

        //info
        heroInventoryPanel = new HeroInventoryPanel(game.getBatch(), hero, animationHelper);
        heroAbilitiesPanel = new HeroAbilitiesPanel(game.getBatch(), hero, animationHelper);

        controller.update(hero);

        PAUSE = false;

        //Gdx.input.setInputProcessor(new SimpleGestureDetector(new SimpleGestureDetector.DirectionListener());

    }


    @Override
    public void show() {

    }

    void update(float delta){

        CURRENTTIME = CURRENTTIME + delta;

        ActivityWithEffect activity;

        handleInput(delta);

            // handle camera after hero position update
            if (hero.getBody().getPosition().x > viewport.getWorldWidth() / 2)
                camera.position.x = (hero.getBody().getPosition().x );
            else
                camera.position.x = viewport.getWorldWidth() / 2;
            if (hero.getBody().getPosition().y > viewport.getWorldHeight() / 2)
                camera.position.y = (hero.getBody().getPosition().y );
            else
                camera.position.y = viewport.getWorldHeight() / 2;

            if(toZoomIn) {
                ZOOM_LEVEL = 2f ;
                camera.zoom = 0.5f;
                viewport.setWorldSize(HuntersGame.WIDTH /2 / HuntersGame.PPM , HuntersGame.HIGHT/ 2/ HuntersGame.PPM);
                toZoomIn = false;
            }

            if(toZoomOut) {
                ZOOM_LEVEL = 1f;
                camera.zoom = 1f;
                viewport.setWorldSize(HuntersGame.WIDTH/ HuntersGame.PPM , HuntersGame.HIGHT/ HuntersGame.PPM);
                toZoomOut = false;
            }


        if(!PAUSE) {

            //update cooldowns
            controller.updateLabels(hero);

            //update hero
            hero.update(delta);

            if (hero.getAbilityToCast() != AbilityID.NONE && hero.finishedCasting()) {
                activity = hero.activateAbility(hero.getAbilityToCast());
                if (activity != null)
                    levelmanager.ACTIVITIES.add(activity);
            }

            //update all enemies
            for (Creature creature : levelmanager.CREATURES) {
                creature.update(delta);
                creature.nextStep();

                if (creature.getAbilityToCast() != AbilityID.NONE && creature.finishedCasting()) {
                    activity = creature.activateAbility(creature.getAbilityToCast());
                    if (activity != null)
                        levelmanager.ACTIVITIES.add(activity);
                }
                ;
            }

            //update items
            for (int i = 0; i < levelmanager.ITEMS.size; i++) {
                GameItem item = levelmanager.ITEMS.get(i);
                item.update(delta);
                if (item.isDestroyed())
                    levelmanager.ITEMS.removeIndex(i);
            }

            //update items
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

        if(abilityToExecuteNext != null && CURRENTTIME > timetoExecuteClickOnAbility) {
            if(abilityToExecuteNext.getType() == AbilityType.BUFF)
                controller.makeHelpIconInActive(abilityIndex);
            if(abilityToExecuteNext.getType() == AbilityType.CLOSE_RANGE_ATACK || abilityToExecuteNext.getType() == AbilityType.LONG_RANGE_ATACK)
                controller.makeAtackIconInActive(abilityIndex);
            if(abilityToExecuteNext.getType() == AbilityType.CLOSE_RANGE_DEFENSE || abilityToExecuteNext.getType() == AbilityType.LONG_RANGE_DEFENSE)
                controller.makeDefenceIconInActive(abilityIndex);

            hero.useAbility(abilityToExecuteNext);
            abilityToExecuteNext = null;

            abilityIndex = 0;
        }

        Vector3 coord;
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            onInventoryScreen = !onInventoryScreen;
            PAUSE = !PAUSE;
            if(!PAUSE)
                controller.update(hero);
            else
                heroInventoryPanel.update();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            onAbilitiesScreen = !onAbilitiesScreen;
            PAUSE = !PAUSE;
            if(!PAUSE)
                controller.update(hero);
            else
                heroAbilitiesPanel.update();
        }


        if (PAUSE) {
            if(Gdx.input.justTouched()) {
                 coord = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                getInfoFromObjectByCoordinates(coord.x,coord.y);
            }
        } else {
                ActivityWithEffect activity = null;

                if (controller.touchedUp ) {
                    hero.jump();
                }
                if (controller.touchedDown) {
                    //hero.hidden = true
                }
                if (controller.touchedLeft) {
                    hero.move(false);
                }
                if (controller.touchedRight) {
                    hero.move(true);
                }


            if (controller.touched0 || Gdx.input.isKeyJustPressed(Input.Keys.Q)) {


                if(abilityIndex >= hero.selectedAtackAbilities.size )
                    abilityIndex =0;
                if ( abilityToExecuteNext != null &&
                        !( abilityToExecuteNext.getType() != AbilityType.CLOSE_RANGE_ATACK || abilityToExecuteNext.getType() != AbilityType.LONG_RANGE_ATACK))
                    abilityIndex =0;
                controller.makeAtackIconActive(abilityIndex);
                abilityIndex++;

                    System.out.println(abilityIndex);
                    timetoExecuteClickOnAbility = CURRENTTIME + 0.5;
                    abilityToExecuteNext = hero.selectedAtackAbilities.get(abilityIndex - 1);
                    controller.touched0 = false;
            }
            if (controller.touched1 || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                if(abilityIndex >= hero.selectedDefenseAbilities.size)
                    abilityIndex =0;
                if (( abilityToExecuteNext != null &&
                        !( abilityToExecuteNext.getType() != AbilityType.CLOSE_RANGE_DEFENSE || abilityToExecuteNext.getType() != AbilityType.LONG_RANGE_DEFENSE)))
                    abilityIndex =0;
                controller.makeDefenceIconActive(abilityIndex);
                abilityIndex++;

                System.out.println(abilityIndex);
                timetoExecuteClickOnAbility = CURRENTTIME + 0.5;
                abilityToExecuteNext = hero.selectedDefenseAbilities.get(abilityIndex - 1);
                controller.touched1 = false;

            }
            if (controller.touched2 || Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                if(abilityIndex >= hero.selectedHelpAbilities.size )
                    abilityIndex =0;
                if ( abilityToExecuteNext != null && abilityToExecuteNext.getType() == AbilityType.BUFF)
                    abilityIndex =0;
                controller.makeHelpIconActive(abilityIndex);
                abilityIndex++;

                System.out.println(abilityIndex);
                timetoExecuteClickOnAbility = CURRENTTIME + 0.5;
                abilityToExecuteNext = hero.selectedHelpAbilities.get(abilityIndex - 1);
                controller.touched2 = false;

            }
//            if (controller.touched3) {
//                timetoExecuteClickOnAbility = CURRENTTIME + 1;
//            }

            // OLD way
//
//                if (controller.touched0) {
//                    hero.useAbility(hero.ABILITY0);
//                }
//                if (controller.touched1) {
//                    hero.useAbility(hero.ABILITY1);
//                }
//                if (controller.touched2) {
//                    hero.useAbility(hero.ABILITY2);
//                }
//                if (controller.touched3) {
//                    hero.useAbility(hero.ABILITY3);
//                }

         //       {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                        hero.jump();
                    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                        hero.move(true);
                    if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                        hero.move(false);

//
//                    if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
//                        if(abilityIndex >= hero.selectedAtackAbilities.size )
//                            abilityIndex =0;
//                        if ( abilityToExecuteNext != null &&
//                                !( abilityToExecuteNext.getType() != AbilityType.CLOSE_RANGE_ATACK || abilityToExecuteNext.getType() != AbilityType.LONG_RANGE_ATACK))
//                            abilityIndex =0;
//                        abilityIndex++;
//
//                        System.out.println(abilityIndex);
//                        timetoExecuteClickOnAbility = CURRENTTIME + 0.5;
//                        abilityToExecuteNext = hero.selectedAtackAbilities.get(abilityIndex - 1);
//                        controller.touched0 = false;
//                    }
//                    if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
//                        if(abilityIndex >= hero.selectedDefenseAbilities.size)
//                            abilityIndex =0;
//                        if (( abilityToExecuteNext != null &&
//                                !( abilityToExecuteNext.getType() != AbilityType.CLOSE_RANGE_DEFENSE || abilityToExecuteNext.getType() != AbilityType.LONG_RANGE_DEFENSE)))
//                            abilityIndex =0;
//                        abilityIndex++;
//
//                        System.out.println(abilityIndex);
//                        timetoExecuteClickOnAbility = CURRENTTIME + 0.5;
//                        abilityToExecuteNext = hero.selectedDefenseAbilities.get(abilityIndex - 1);
//                        controller.touched0 = false;
//                    }
//                    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                        if(abilityIndex >= hero.selectedHelpAbilities.size )
//                            abilityIndex =0;
//                        if ( abilityToExecuteNext != null && abilityToExecuteNext.getType() == AbilityType.BUFF)
//                            abilityIndex =0;
//                        abilityIndex++;
//
//                        System.out.println(abilityIndex);
//                        timetoExecuteClickOnAbility = CURRENTTIME + 0.5;
//                        abilityToExecuteNext = hero.selectedHelpAbilities.get(abilityIndex - 1);
//                        controller.touched0 = false;
//                    }

//                    if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
//                        hero.useAbility(hero.ABILITY0);
//                    };
//                    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                        hero.useAbility(hero.ABILITY1);
//                    };
//                    if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
//                        hero.useAbility(hero.ABILITY2);
//                    }
//                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
//                        hero.useAbility(hero.ABILITY3);
//                activity = hero.activateAbility(Hero.ABILITY2);
//                if (activity != null)
//                    levelmanager.ACTIVITIES.add(activity);
//                    };
                    if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                        hero.makeInvisible(!hero.isHidden());
                    }


                    if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                        levelmanager.ITEMS.add(hero.throwFromInventory(hero.getInventory().get(0))); // TODO adjust
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                        toZoomIn = true;
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                        toZoomOut = true;
                    }
                }
           // }


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

        update(delta);


            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            maprenderer.render(); // show map

            // render hero
            game.getBatch().setProjectionMatrix(camera.combined);
            game.getBatch().begin();

            //render all enemies
            for (Creature creature : levelmanager.CREATURES) {
                creature.draw(game.getBatch());
            }

            //render all items
            for (GameItem item : levelmanager.ITEMS) {
                if (!item.isDestroyed())
                    item.draw(game.getBatch());
            }

            //render all activities
            for (ActivityWithEffect activity : levelmanager.ACTIVITIES) {
                if (!activity.isDestroyed())
                    activity.draw(game.getBatch());
            }

            hero.draw(game.getBatch());

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
        //}
    }

    public boolean gameOver(){
        if(hero.getState() == State.DEAD){
            return true;
        }
        return false;
    }

    public boolean gameWin(){  // TODO extend
        for(GameItem item : hero.getInventory()){
            if(item.itemname.equals("Key from next level")){

            HuntersGame.gameDetails = "You found a Key!";
            return true;
            }
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
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
