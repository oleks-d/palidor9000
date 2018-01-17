package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.sprites.activities.ActivityWithEffect;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.sprites.triggers.Trigger;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.stuctures.descriptions.CreatureDescription;
import com.mygdx.game.stuctures.descriptions.ItemDescription;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by odiachuk on 12/20/17.
 *
 * All items and enemies
 */
public class LevelManager implements Disposable{

    com.mygdx.game.screens.GameScreen screen;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    JSONLoader loader;
    public Hero hero;

    public static final int GROUND_LAYER = 2+1;
    public static final int TRIGGERS_LAYER = 3+1;
    public static final int OBJECTS_LAYER = 4+1;
    public static final int CREATURES_LAYER = 5+1;
    public static final int JUMP_POINTS_LAYER = 6+1;

    public HashMap<String, ItemDescription> ITEMS_DESCRIPTIONS;
    public HashMap<String, CreatureDescription>  CREATURE_DESCRIPTIONS;

    public Array<GameItem> ITEMS;
    public Array<Creature> CREATURES;
    public Array<ActivityWithEffect> ACTIVITIES;

    public CreatureDescription DEFAULT_CREATURE_DESCRIPTION = new CreatureDescription("Unknown", "Unknown", "enemy1");
    public ItemDescription DEFAULT_ITEM_DESCRIPTION = new ItemDescription("Unknown","Unknown", "Unknown", "icon_blank", 0);
    //public Sprite background;

//    public String currentLevel;
//    public String previousLevel = "Start";

    public LevelManager(com.mygdx.game.screens.GameScreen screen) {

        this.screen = screen;
        mapLoader = new TmxMapLoader();

        //load definitions from file
        loader = new JSONLoader();
        ITEMS_DESCRIPTIONS = loader.loadItems();
        CREATURE_DESCRIPTIONS = loader.loadCreatures();
    }

    public void loadNextLevel(String name) {
        //dispose();
        hero.previousLevel = hero.currentLevel;
        loadLevel(name);
    }

    public void loadLevel(String levelname){

        hero.currentLevel = levelname;
        // physics
        screen.world = new World(new Vector2(0, -10f  ), true);
        screen.world.setContactListener(new com.mygdx.game.tools.WorldContactListener(screen));

        screen.debugRenderer = new Box2DDebugRenderer();

        map = mapLoader.load(PalidorGame.MAPS_DIR + File.separator + levelname + ".tmx");

        //TODO background
//        background = new Sprite();
//        background.setTexture(new Texture(PalidorGame.SPRITES_DIR + File.separator + levelname + ".jpg"));
//        background.setSize(PalidorGame.WIDTH/PalidorGame.PPM,PalidorGame.HIGHT/PalidorGame.PPM);
//        background.setRegion(0,0,640,400);

        screen.maprenderer = new OrthogonalTiledMapRenderer(map, 1 / PalidorGame.PPM);

        ITEMS = new Array<GameItem>();
        CREATURES = new Array<Creature> ();
        ACTIVITIES = new Array<ActivityWithEffect>();

        for (MapObject object : map.getLayers().get(GROUND_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            createGroundObject(screen.world, object);
        }

        for (MapObject object : map.getLayers().get(JUMP_POINTS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            createAIPoint(screen.world, object);
        }

        for (MapObject object : map.getLayers().get(TRIGGERS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            createTriggerPoint(screen.world, object);
        }

        // load objects
        for (MapObject object : map.getLayers().get(OBJECTS_LAYER).getObjects()) {
            //Rectangle rect = ((RectangleMapObject) object).getRectangle();
            createItemObject(screen, object);
        }

        // load enemies
        for (MapObject object : map.getLayers().get(CREATURES_LAYER).getObjects()) {

            if (object.getName().startsWith("hero")) {
                if(object.getName().contains(hero.previousLevel)) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
//                    if(screen.hero == null)
//                        screen.hero = new Hero(screen, rect.getX(), rect.getY());
//                    else
                        screen.hero.makeAlive(rect.getX(), rect.getY());
                };
            } else
                createCreature(screen, object);
        }
    }

    public void createItemObject(com.mygdx.game.screens.GameScreen screen, MapObject object) {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        //try {
            ITEMS.add(new GameItem(screen, rect.getX(), rect.getY(), ITEMS_DESCRIPTIONS.get(object.getName())));
        //}catch (Exception e) {
        //    ITEMS.add(new GameItem(screen, rect.getX(), rect.getY(), DEFAULT_ITEM_DESCRIPTION));
        //    Gdx.app.log("Items creation", "No such object type: " + object.getName());
        //}

    }

    public void createCreature(com.mygdx.game.screens.GameScreen screen, MapObject object) {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        //try {
            CREATURES.add(new Creature(screen, rect.getX() , rect.getY(), CREATURE_DESCRIPTIONS.get(object.getName())));
        //}catch (Exception e) {
        //    CREATURES.add(new Creature(screen, rect.getX() , rect.getY(), DEFAULT_CREATURE_DESCRIPTION));
        //    Gdx.app.log("Creature creation", "No such object type: " + object.getName());
        //}
    }

    public void createCreature(com.mygdx.game.screens.GameScreen screen, int x, int y, String object) {
        try {
            CREATURES.add(new Creature(screen, x, y, CREATURE_DESCRIPTIONS.get(object)));
        }catch (Exception e) {
            CREATURES.add(new Creature(screen, x , y, DEFAULT_CREATURE_DESCRIPTION));
            Gdx.app.log("Creature creation", "No such object type: " + object);
        }
    }
    private void createGroundObject(World world, MapObject object) {
        BodyDef bdef = new BodyDef();

        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rect.getX() + rect.getWidth() / 2) / PalidorGame.PPM, (rect.getY() + rect.getHeight() / 2) / PalidorGame.PPM);

        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth() / 2 / PalidorGame.PPM, rect.getHeight() / 2 / PalidorGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);//.setUserData("ground");

        Filter filter = new Filter();
        filter.categoryBits = PalidorGame.OBJECT_BIT;
        fixture.setFilterData(filter);
    }

    private void createAIPoint(World world, MapObject object) {
        BodyDef bdef = new BodyDef();

        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rect.getX() + rect.getWidth() / 2) / PalidorGame.PPM, (rect.getY() + rect.getHeight() / 2) / PalidorGame.PPM);

        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth() / 2 / PalidorGame.PPM, rect.getHeight() / 2 / PalidorGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        Fixture fixture = body.createFixture(fixtureDef);//.setUserData("ground");

        Filter filter = new Filter();
        if(object.getName().equals("no_right"))
            filter.categoryBits = PalidorGame.NO_RIGHT_POINT;
        else if(object.getName().equals("no_left"))
            filter.categoryBits = PalidorGame.NO_LEFT_POINT;
        else
            filter.categoryBits = PalidorGame.JUMP_POINT;

        fixture.setFilterData(filter);
    }

    private void createTriggerPoint(World world, MapObject object) {
        BodyDef bdef = new BodyDef();

        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rect.getX() + rect.getWidth() / 2) / PalidorGame.PPM, (rect.getY() + rect.getHeight() / 2) / PalidorGame.PPM);

        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth() / 2 / PalidorGame.PPM, rect.getHeight() / 2 / PalidorGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(new Trigger(object.getName())); // add trigger data


        Filter filter = new Filter();
        filter.categoryBits = PalidorGame.TRIGGER_POINT;
        fixture.setFilterData(filter);

    }

    public TiledMap getMap() {
        return map;
    }

    @Override
    public void dispose() {
        map.dispose();
    }


    public Hero loadHero(String name){
        String path = PalidorGame.SAVES_DIR + File.separator + name + ".json";
        hero = loader.loadHero(screen, Gdx.files.local(path));
        return hero;
    }

    public void saveHero(Hero hero) {
        loader.saveHero(hero);
    }

    public static ArrayList<String> getListOfSaveHeros(){
        ArrayList<String> listOfFiles = new ArrayList<String>();
        FileHandle dirHandle = Gdx.files.local(PalidorGame.SAVES_DIR);
        for (FileHandle entry: dirHandle.list()) {
            listOfFiles.add(entry.nameWithoutExtension());
        }
        return listOfFiles;
    }

    public static ArrayList<String> getListOfHeroTypes(){
        ArrayList<String> listOfFiles = new ArrayList<String>();
        FileHandle dirHandle = Gdx.files.internal(PalidorGame.DEFAULT_HERO_DIR);
        for (FileHandle entry: dirHandle.list()) {
            listOfFiles.add(entry.nameWithoutExtension());
        }
        return listOfFiles;
    }

    public Hero createNewHero(String heroName, String newHeroType) {
        String path = PalidorGame.DEFAULT_HERO_DIR + File.separator + newHeroType + ".json";
        hero = loader.loadHero(screen, Gdx.files.internal(path));
        hero.name = heroName;
        saveHero(hero);
        return hero;
    }

    public static void removeSave(String savedHeroLabel) {
        FileHandle file = Gdx.files.local(PalidorGame.SAVES_DIR + File.separator + savedHeroLabel + ".json");
        file.delete();
    }
}
