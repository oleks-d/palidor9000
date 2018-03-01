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
import com.mygdx.game.dialogs.GameDialog;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.State;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.activities.ActivityWithEffect;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.sprites.gameobjects.GameObject;
import com.mygdx.game.sprites.triggers.Trigger;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.stuctures.descriptions.CreatureDescription;
import com.mygdx.game.stuctures.descriptions.ItemDescription;
import com.mygdx.game.stuctures.descriptions.ObjectDescription;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.badlogic.gdx.utils.JsonValue.ValueType.object;

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

    public static final int GROUND_LAYER = 3;
    public static final int ROUTE_POINTS_LAYER = 4;
    public static final int TRIGGERS_LAYER = 5;
    public static final int ITEMS_LAYER = 6;
    public static final int CREATURES_LAYER = 7;
    public static final int OBJECTS_LAYER = 8;

    public HashMap<String, ItemDescription> ITEMS_DESCRIPTIONS;
    public HashMap<String, CreatureDescription>  CREATURE_DESCRIPTIONS;
    public HashMap<String, ObjectDescription>  OBJECT_DESCRIPTIONS;
    public HashMap<Integer, GameDialog> DIALOGS;

    public Array<GameItem> ITEMS;
    public Array<Creature> CREATURES;
    public Array<Creature> SUMMONED_CREATURES;
    public Array<GameObject> OBJECTS;
    public Array<ActivityWithEffect> ACTIVITIES;
    public Array<UnavailableCreatures> UNAVAILABLE_CREATURES; // creatures that does not correspond to condition
    public Array<UnavailableObject> UNAVAILABLE_OBJECTS;

    //public Sprite background;

//    public String currentLevel;
//    public String previousLevel = "Start";

    public LevelManager(GameScreen screen) {

        this.screen = screen;
        mapLoader = new TmxMapLoader();

        //load definitions from file
        loader = new JSONLoader();
        ITEMS_DESCRIPTIONS = loader.loadItems();
        CREATURE_DESCRIPTIONS = loader.loadCreatures();
        OBJECT_DESCRIPTIONS = loader.loadObjects();
        DIALOGS = loader.loadDialogs();
    }

    public void loadNextLevel(String levelName, String heroName) {
        //dispose();
        hero.previousLevel = hero.currentLevel;
        loadLevel(levelName,heroName);
    }

    public void loadLevel(String levelname,String heroName){

        hero.currentLevel = levelname;
        // physics
        screen.world = new World(new Vector2(0, -10f  ), true);
        screen.world.setContactListener(new com.mygdx.game.tools.WorldContactListener(screen));

        screen.debugRenderer = new Box2DDebugRenderer();

        if(Gdx.files.local(PalidorGame.SAVES_DIR + File.separator + heroName + File.separator + levelname + ".tmx").exists())
            map = mapLoader.load(PalidorGame.SAVES_DIR + File.separator + heroName + File.separator + levelname + ".tmx");
        else
            map = mapLoader.load(PalidorGame.MAPS_DIR + File.separator + levelname + ".tmx");

        //TODO background
//        background = new Sprite();
//        background.setTexture(new Texture(PalidorGame.SPRITES_DIR + File.separator + levelname + ".jpg"));
//        background.setSize(PalidorGame.WIDTH/PalidorGame.PPM,PalidorGame.HIGHT/PalidorGame.PPM);
//        background.setRegion(0,0,640,400);

        screen.maprenderer = new OrthogonalTiledMapRenderer(map, 1 / PalidorGame.PPM);

        ITEMS = new Array<GameItem>();
        CREATURES = new Array<Creature> ();
        SUMMONED_CREATURES = new Array<Creature> ();
        OBJECTS = new Array<GameObject> ();
        ACTIVITIES = new Array<ActivityWithEffect>();
        UNAVAILABLE_CREATURES = new Array<UnavailableCreatures>();
        UNAVAILABLE_OBJECTS = new Array<UnavailableObject>();

        for (MapObject object : map.getLayers().get(GROUND_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            createGroundObject(screen.world, object);
        }

        for (MapObject object : map.getLayers().get(ROUTE_POINTS_LAYER).getObjects().getByType(RectangleMapObject.class)) {

            if (object.getName().startsWith("hero")) {
                if(object.getName().contains(hero.previousLevel)) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    screen.hero.makeAlive(rect.getX(), rect.getY());
                };
            } else
                createAIPoint(screen.world, object);

        }

        for (MapObject object : map.getLayers().get(TRIGGERS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            createTriggerPoint(screen.world, object);
        }

        // load objects
        for (MapObject object : map.getLayers().get(ITEMS_LAYER).getObjects()) {
            //Rectangle rect = ((RectangleMapObject) object).getRectangle();
            createItemObject(screen, object);
        }

        // load enemies
        for (MapObject object : map.getLayers().get(CREATURES_LAYER).getObjects()) {
            createCreature(screen, object);
        }

        // load objects
        for (MapObject object : map.getLayers().get(OBJECTS_LAYER).getObjects()) {
            createInteractiveObject(screen, object);
        }
    }

    public void createItemObject(GameScreen screen, MapObject object) {
        Gdx.app.log("Loading", object.getName());
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        ITEMS.add(new GameItem(screen, rect.getX(), rect.getY(), ITEMS_DESCRIPTIONS.get(object.getName())));
    }

    public void createItemObject(GameScreen screen, float x, float y, String object) {
        ITEMS.add(new GameItem(screen, x,y, ITEMS_DESCRIPTIONS.get(object)));
    }

    public void createCreature(GameScreen screen, MapObject object) {
        Gdx.app.log("Loading", object.getName());
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        String items = (String)object.getProperties().get("items");
        String deathProcess = (String)object.getProperties().get("deathprocess");
        String organizationID = (String)object.getProperties().get("org");
        String uniqueID = (String)object.getProperties().get("uid");
        Integer organization =  organizationID==null ? 0 : Integer.valueOf(organizationID) ;
        String dialogs = (String)object.getProperties().get("dialogs");
        String condition = (String)object.getProperties().get("condition");
        String program = (String)object.getProperties().get("program");
        if(!(ConditionProcessor.conditionSatisfied(hero, condition))) {
                UNAVAILABLE_CREATURES.add(new UnavailableCreatures(object.getName(), rect,  condition, items, deathProcess, organization, dialogs, program, uniqueID));
                return; //do not create if condition did not pass
        }
        Creature mob = new Creature(screen, rect, CREATURE_DESCRIPTIONS.get(object.getName()), items==null?"":items, deathProcess==null?"":deathProcess, organization==null?0:organization, dialogs==null?"":dialogs , program==null?"":program);
        if(uniqueID == null || "".equals(uniqueID))
            mob.setUniqueID(CREATURES.size + 2);
        else
            mob.setUniqueID(Integer.valueOf(uniqueID));
        CREATURES.add(mob);

    }

    public void createCreatureFromUnavailableCreature(UnavailableCreatures unavailablemob){
        Creature mob = new Creature(screen, unavailablemob.rect, CREATURE_DESCRIPTIONS.get(unavailablemob.name), unavailablemob.items==null?"":unavailablemob.items, unavailablemob.deathProcess==null?"":unavailablemob.deathProcess, unavailablemob.organization==null?0:unavailablemob.organization, unavailablemob.dialogs==null?"":unavailablemob.dialogs , unavailablemob.program==null?"":unavailablemob.program);
        if(unavailablemob.getUid() == null || "".equals(unavailablemob.getUid()))
            mob.setUniqueID(CREATURES.size + 2);
        else
            mob.setUniqueID(Integer.valueOf(unavailablemob.getUid()));
        CREATURES.add(mob);
    }

    public void createSummonedCreature(GameScreen screen, float x, float y, String object, Creature owner) {
            Creature mob = new Creature(screen, x, y, CREATURE_DESCRIPTIONS.get(object), null, null , owner.getOrganization() , null, null);
            mob.setOwner(owner);
            mob.setUniqueID(SUMMONED_CREATURES.size + 1000 + 2);

            mob.useAbility(AbilityID.APPEAR);

            SUMMONED_CREATURES.add(mob);
    }

    public void createCreature(GameScreen screen, float x, float y, String object, int org) {
        Creature mob = new Creature(screen, x, y, CREATURE_DESCRIPTIONS.get(object), null, null , org , null, null);
        mob.setUniqueID(CREATURES.size + 2);
        CREATURES.add(mob);
    }

    public void createInteractiveObject(GameScreen screen, MapObject object) {
        Gdx.app.log("Loading", object.getName());
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        String items = (String)object.getProperties().get("items");
        String condition = (String)object.getProperties().get("condition");
        String program = (String)object.getProperties().get("program");
        String activationTrigger = (String)object.getProperties().get("trigger");
        if(!(ConditionProcessor.conditionSatisfied(hero, condition))) {
                UNAVAILABLE_OBJECTS.add(new UnavailableObject(object.getName(), rect,  condition, items, program, activationTrigger));
                return; //do not create if condition did not pass
        }

        OBJECTS.add(new GameObject(screen, rect, OBJECT_DESCRIPTIONS.get(object.getName()),condition, items, program, activationTrigger));

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
        filter.categoryBits = PalidorGame.GROUND_BIT;
        fixture.setFilterData(filter);


        // set critical points
//TODO specil points generation

//        shape = new PolygonShape();
//        shape.setAsBox(12 / PalidorGame.PPM, 8 / PalidorGame.PPM, new Vector2(-(rect.getWidth()/ 2 / PalidorGame.PPM), rect.getHeight() / PalidorGame.PPM), 0);
//
//        fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//
//        fixture = body.createFixture(fixtureDef);
//        fixture.setSensor(true);
//
//        filter = new Filter();
//            filter.categoryBits = PalidorGame.MOVE_RIGHT_POINT;
//        fixture.setFilterData(filter);
//
//
//
//        shape = new PolygonShape();
//        shape.setAsBox(12 / PalidorGame.PPM, 8 / PalidorGame.PPM, new Vector2(rect.getWidth()/ 2 / PalidorGame.PPM, rect.getHeight() / PalidorGame.PPM ), 0);
//
//        fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//
//        fixture = body.createFixture(fixtureDef);
//        fixture.setSensor(true);
//
//        filter = new Filter();
//            filter.categoryBits = PalidorGame.MOVE_LEFT_POINT;
//        fixture.setFilterData(filter);

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
        if(object.getName().equals("mr"))
            filter.categoryBits = PalidorGame.MOVE_RIGHT_POINT;
        else if(object.getName().equals("ml"))
            filter.categoryBits = PalidorGame.MOVE_LEFT_POINT;
        else if(object.getName().equals("st"))
            filter.categoryBits = PalidorGame.STAND_POINT;
        else if(object.getName().equals("jl"))
            filter.categoryBits = PalidorGame.JUMP_LEFT;
        else if(object.getName().equals("jr"))
            filter.categoryBits = PalidorGame.JUMP_RIGHT;
        else
            filter.categoryBits = PalidorGame.STAND_POINT;

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
        fixture.setUserData(new Trigger(object.getName(),
                (String) object.getProperties().get("key"),
                (String) object.getProperties().get("value"),
                (String) object.getProperties().get("text"),
                (String) object.getProperties().get("condition"),
                (String) object.getProperties().get("process")
        )); // add trigger data


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
            if(!entry.isDirectory())
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
        file = Gdx.files.local(PalidorGame.SAVES_DIR + File.separator + savedHeroLabel);
        file.deleteDirectory();
    }

    public void saveLevel(String previousLevel, String saveDir) {

        //create save dir
        if (!Gdx.files.local(PalidorGame.SAVES_DIR + File.separator + saveDir).exists())
            Gdx.files.local(PalidorGame.SAVES_DIR + File.separator + saveDir).mkdirs();

//        //copy TSX file
//        if(!Gdx.files.local(PalidorGame.SAVES_DIR  + File.separator + saveDir + File.separator + "Hunters.tsx").exists())
//            Gdx.files.local(PalidorGame.MAPS_DIR + File.separator +   "Hunters.tsx")
//                .copyTo(Gdx.files.local(PalidorGame.SAVES_DIR  + File.separator + saveDir + File.separator + "Hunters.tsx"));
//
//        //copy png file
//        if(!Gdx.files.local(PalidorGame.SAVES_DIR  + File.separator + saveDir + File.separator + "Tileset.png").exists())
//            Gdx.files.local(PalidorGame.MAPS_DIR + File.separator +   "Tileset.png")
//                    .copyTo(Gdx.files.local(PalidorGame.SAVES_DIR  + File.separator + saveDir + File.separator + "Tileset.png"));

//        //copy map file
//        Gdx.files.local(PalidorGame.MAPS_DIR + File.separator +  previousLevel + ".tmx")
//                .copyTo(Gdx.files.local(PalidorGame.SAVES_DIR  + File.separator + saveDir + File.separator + previousLevel + ".tmx"));

        try (
                BufferedReader reader = new BufferedReader(new FileReader(Gdx.files.local(PalidorGame.MAPS_DIR + File.separator + previousLevel + ".tmx").file()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(Gdx.files.local(PalidorGame.SAVES_DIR + File.separator + saveDir + File.separator + previousLevel + ".tmx").file()));
        ) {
            String line = null;
            while ((line = reader.readLine()) != null) {

                if (line.contains("<tileset firstgid=\"1\" source=\"Hunters.tsx\"/>")) {
                    writer.write("<tileset firstgid=\"1\" source=\"../../../maps/Hunters.tsx\"/>");
                    writer.write("\n");
                } else if (line.contains("<objectgroup name=\"entities\"")) {
                    break;
                } else {
                    writer.write(line);
                    writer.write("\n");
                }

            }

            // fill map
            writer.write("<objectgroup name=\"entities\">");
            writer.write("\n");
            // write all entities
            for (int i = 0; i < ITEMS.size; i++) {
                writer.write("<object id=\"" + 200 + i + "\" name=\"" + ITEMS.get(i).id + "\" x=\"" + ITEMS.get(i).getBody().getPosition().x * PalidorGame.PPM + "\" y=\"" + (PalidorGame.MAP_HIGHT - ITEMS.get(i).getBody().getPosition().y * PalidorGame.PPM) + "\"/>");
                writer.write("\n");
            }
            writer.write("</objectgroup>");
            writer.write("\n");

            // creatures
            writer.write("<objectgroup name=\"creatures\">");
            writer.write("\n");
            // write creatures
            for (int i = 0; i < CREATURES.size; i++) {
                if(CREATURES.get(i).originalRect.width != 0)
                    writer.write("<object id=\"" + 400 + i + "\" name=\"" + CREATURES.get(i).descriptionID + "\" x=\"" + CREATURES.get(i).originalRect.x + "\" y=\"" + (PalidorGame.MAP_HIGHT - CREATURES.get(i).originalRect.y - CREATURES.get(i).originalRect.getHeight()) + "\" width=\"" + CREATURES.get(i).originalRect.getWidth() + "\" height=\"" + CREATURES.get(i).originalRect.getHeight() + "\">\n");
                else
                    writer.write("<object id=\"" + 400 + i + "\" name=\"" + CREATURES.get(i).descriptionID + "\" x=\"" + CREATURES.get(i).originalRect.x + "\" y=\"" + (PalidorGame.MAP_HIGHT - CREATURES.get(i).originalRect.y) + "\">\n");

                // writer.write("<object id=\"" + 400 + i + "\" name=\"" + CREATURES.get(i).descriptionID + "\" x=\"" + CREATURES.get(i).getBody().getPosition().x * PalidorGame.PPM + "\" y=\"" + (PalidorGame.MAP_HIGHT - CREATURES.get(i).getBody().getPosition().y * PalidorGame.PPM) + "\">\n");

                writer.write("   <properties>\n" +
                        "    <property name=\"condition\" value=\"\"/>\n" +
                        "    <property name=\"items\" value=\"" + CREATURES.get(i).getInventory().toString().replaceAll("[\\[\\]]", "") + "\"/>\n" +
                        "    <property name=\"deathprocess\" value=\"" + CREATURES.get(i).getDeathProcess() + "\"/>" +
                        "    <property name=\"org\" value=\"" + CREATURES.get(i).getOrganization() + "\"/>" +
                        "    <property name=\"uid\" value=\"" + CREATURES.get(i).getID() + "\"/>" +
                        "    <property name=\"dialogs\" value=\"" + CREATURES.get(i).getDialogs().toString().replaceAll("[\\[\\]]", "") + "\"/>" +
                        "    <property name=\"program\" value=\"" + CREATURES.get(i).getProgram().toString().replaceAll("[\\[\\]]", "") + "\"/>" +
                        "   </properties>\n");
                writer.write("\n");
                writer.write("</object>");
            }

            for (int i = 0; i < UNAVAILABLE_CREATURES.size; i++) {
                writer.write("<object id=\"" + 600 + i + "\" name=\"" + UNAVAILABLE_CREATURES.get(i).name + "\" x=\"" + UNAVAILABLE_CREATURES.get(i).rect.x + "\" y=\"" + (PalidorGame.MAP_HIGHT - UNAVAILABLE_CREATURES.get(i).rect.y) + "\" width=\"" + UNAVAILABLE_CREATURES.get(i).rect.getWidth() + "\" height=\"" + UNAVAILABLE_CREATURES.get(i).rect.getHeight() + "\">\n");

                writer.write("   <properties>\n" +
                        "    <property name=\"condition\" value=\"" + UNAVAILABLE_CREATURES.get(i).getConditions() + "\"/>\n" +
                        "    <property name=\"items\" value=\"" + UNAVAILABLE_CREATURES.get(i).getItems() + "\"/>\n" +
                        "    <property name=\"deathprocess\" value=\"" + UNAVAILABLE_CREATURES.get(i).getDeathProcess() + "\"/>" +
                        "    <property name=\"org\" value=\"" + UNAVAILABLE_CREATURES.get(i).getOrganization() + "\"/>" +
                        "    <property name=\"dialogs\" value=\"" + UNAVAILABLE_CREATURES.get(i).getDialogs() + "\"/>" +
                        "    <property name=\"program\" value=\"" + UNAVAILABLE_CREATURES.get(i).getProgram() + "\"/>" +
                        "    <property name=\"uid\" value=\"" + UNAVAILABLE_CREATURES.get(i).getUid() + "\"/>" +
                        "   </properties>\n");
                writer.write("\n");
                writer.write("</object>");
            }

            writer.write("</objectgroup>");
            writer.write("\n");

            //objects

            writer.write("<objectgroup name=\"objects\">");
            writer.write("\n");
            // write objects
            for (int i = 0; i < OBJECTS.size; i++) {
                if (OBJECTS.get(i).originalRectangle == null)
                    writer.write("<object id=\"" + 800 + i + "\" name=\"" + OBJECTS.get(i).id + "\" x=\"" + (OBJECTS.get(i).getBody().getPosition().x - OBJECTS.get(i).getWidth() / 2) * PalidorGame.PPM + "\" y=\"" + (PalidorGame.MAP_HIGHT - (OBJECTS.get(i).getBody().getPosition().y + OBJECTS.get(i).getHeight() / 2) * PalidorGame.PPM) + "\" width=\"" + OBJECTS.get(i).getWidth() * PalidorGame.PPM + "\" height=\"" + OBJECTS.get(i).getHeight() * PalidorGame.PPM + "\" >\n");
                else
                    writer.write("<object id=\"" + 800 + i + "\" name=\"" + OBJECTS.get(i).id + "\" x=\"" + OBJECTS.get(i).originalRectangle.x + "\" y=\"" + (PalidorGame.MAP_HIGHT - OBJECTS.get(i).originalRectangle.y - OBJECTS.get(i).originalRectangle.height) + "\" width=\"" + OBJECTS.get(i).originalRectangle.width + "\" height=\"" + OBJECTS.get(i).originalRectangle.height + "\" >\n");

                writer.write("   <properties>\n" +
                        "    <property name=\"condition\" value=\"" + OBJECTS.get(i).getCondition() + "\"/>\n" +
                        "    <property name=\"items\" value=\"" + OBJECTS.get(i).getItems().toString().replaceAll("[\\[\\]]", "") + "\"/>\n" +
                        "    <property name=\"program\" value=\"" + OBJECTS.get(i).getProgram() + "\"/>\n" +
                        "    <property name=\"trigger\" value=\"" + OBJECTS.get(i).getTrigger() + "\"/>\n" +
                        "   </properties>\n");
                writer.write("\n");
                writer.write("</object>");
            }

            for (int i = 0; i < UNAVAILABLE_OBJECTS.size; i++) {
                writer.write("<object id=\"" + 1000 + i + "\" name=\"" + UNAVAILABLE_OBJECTS.get(i).getID() + "\" x=\"" + (UNAVAILABLE_OBJECTS.get(i).rect.x - UNAVAILABLE_OBJECTS.get(i).rect.width / 2) + "\" y=\"" + (PalidorGame.MAP_HIGHT - UNAVAILABLE_OBJECTS.get(i).rect.y - UNAVAILABLE_OBJECTS.get(i).rect.height) + "\" width=\"" + UNAVAILABLE_OBJECTS.get(i).rect.getWidth() + "\" height=\"" + UNAVAILABLE_OBJECTS.get(i).rect.getHeight() + "\" >\n");

                writer.write("   <properties>\n" +
                        "    <property name=\"condition\" value=\"" + UNAVAILABLE_OBJECTS.get(i).getCondition() + "\"/>\n" +
                        "    <property name=\"items\" value=\"" + UNAVAILABLE_OBJECTS.get(i).getItems() + "\"/>\n" +
                        "    <property name=\"program\" value=\"" + UNAVAILABLE_OBJECTS.get(i).getProgram() + "\"/>\n" +
                        "    <property name=\"trigger\" value=\"" + UNAVAILABLE_OBJECTS.get(i).getTrigger() + "\"/>\n" +
                        "   </properties>\n");
                writer.write("\n");
                writer.write("</object>");
            }

            writer.write("</objectgroup>");
            writer.write("\n");

            writer.write("</map>");
            writer.write("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeDeadBodies() {
        for(int i = 0; i < CREATURES.size; i++)
            if(CREATURES.get(i).getState() == State.DEAD)
                CREATURES.removeIndex(i);
    }

    public void setProgramTo(String creatureID, String program) {
        for(int i = 0; i < CREATURES.size; i++)
            if(CREATURES.get(i).getID() == Integer.valueOf(creatureID))
                CREATURES.get(i).brain.resetProgram(program);
    }

    private class UnavailableObject {

        String id;
        Rectangle rect;
        String condition;
        String items;
        String program;
        String activationTrigger;


        public UnavailableObject(String id, Rectangle rect, String condition, String items, String program, String activationTrigger) {
            this.id = id;
            this.rect = rect;
            this.condition = condition;
            this.items = items;
            this.program = program;
            this.activationTrigger = activationTrigger;
        }

        public String getID() {
            return id;
        }

        public String getCondition() {
            return condition==null?"":condition;
        }

        public String getItems() {
            return items==null?"":items;
        }

        public String getProgram() {
            return program==null?"":program;
        }

        public String getTrigger() {
            return activationTrigger==null?"":activationTrigger;
        }
    }

    public class UnavailableCreatures {
        String name;
        Rectangle rect;
        private String program;
        String uid;

        public UnavailableCreatures(String name, Rectangle rect, String condition, String items, String deathProcess, Integer organization, String dialogs, String program, String uid ) {
            this.name = name;
            this.rect = rect;
            this.condition = condition;
            this.items = items;
            this.deathProcess = deathProcess;
            this.organization = organization;
            this.dialogs = dialogs;
            this.program = program;
            this.uid = uid;
        }

        String condition;
        String items;
        String deathProcess;
        Integer organization;
        String dialogs;


        public String getDialogs() {
            return dialogs==null?"":dialogs;
        }

        public Integer getOrganization() {
            return organization==null?0:organization;
        }

        public String getDeathProcess() {
            return deathProcess==null?"":deathProcess;
        }

        public String getItems() {
            return items==null?"":items;
        }

        public String getConditions() {
            return condition==null?"":condition;
        }

        public String getProgram() {
            return program==null?"":program;
        }

        public String getUid() {
            return uid==null?"":uid;
        }
    }
}
