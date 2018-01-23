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

    public Array<GameItem> ITEMS;
    public Array<Creature> CREATURES;
    public Array<GameObject> OBJECTS;
    public Array<ActivityWithEffect> ACTIVITIES;
    public Array<UnavailableObject> UNAVAILABLE_CREATURES; // creatures that does not correspond to condition
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
        OBJECTS = new Array<GameObject> ();
        ACTIVITIES = new Array<ActivityWithEffect>();
        UNAVAILABLE_CREATURES = new Array<UnavailableObject>();
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

    public void createItemObject(com.mygdx.game.screens.GameScreen screen, MapObject object) {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
            ITEMS.add(new GameItem(screen, rect.getX(), rect.getY(), ITEMS_DESCRIPTIONS.get(object.getName())));
    }

    public void createCreature(GameScreen screen, MapObject object) {
        String conditionKey;
        String conditionValue;
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        String items = (String)object.getProperties().get("items");
        String condition = (String)object.getProperties().get("condition");
        if(condition!=null && !"".equals(condition)) {
            conditionKey = condition.split(":")[0];
            conditionValue = condition.split(":")[1];
            if(!screen.hero.getGlobalState(conditionKey).equals(conditionValue)){
                UNAVAILABLE_CREATURES.add(new UnavailableObject(object.getName(), rect,  condition, items));
                return; //do not create if condition did not pass
            }

        }
        CREATURES.add(new Creature(screen, rect.getX() , rect.getY(), CREATURE_DESCRIPTIONS.get(object.getName()), items));

    }

    public void createCreature(com.mygdx.game.screens.GameScreen screen, int x, int y, String object) {
            CREATURES.add(new Creature(screen, x, y, CREATURE_DESCRIPTIONS.get(object), null));
    }

    public void createInteractiveObject(GameScreen screen, MapObject object) {
        String conditionKey;
        String conditionValue;
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        String items = (String)object.getProperties().get("items");
        String condition = (String)object.getProperties().get("condition");
        String program = (String)object.getProperties().get("program");
        if(condition!=null && !"".equals(condition)) {
            conditionKey = condition.split(":")[0];
            conditionValue = condition.split(":")[1];
            if(!screen.hero.getGlobalState(conditionKey).equals(conditionValue)){
                UNAVAILABLE_OBJECTS.add(new UnavailableObject(object.getName(), rect,  condition, items, program));
                return; //do not create if condition did not pass
            }

        }
        OBJECTS.add(new GameObject(screen, rect.getX() , rect.getY(), OBJECT_DESCRIPTIONS.get(object.getName()), items, program));

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
        if(object.getName().equals("move_right"))
            filter.categoryBits = PalidorGame.MOVE_RIGHT_POINT;
        else if(object.getName().equals("move_left"))
            filter.categoryBits = PalidorGame.MOVE_LEFT_POINT;
        else if(object.getName().equals("stand"))
            filter.categoryBits = PalidorGame.STAND_POINT;
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
        fixture.setUserData(new Trigger(object.getName(),
                (String)object.getProperties().get("key"),
                (String)object.getProperties().get("value"),
                (String)object.getProperties().get("text"))); // add trigger data


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
            if (!Gdx.files.local(PalidorGame.SAVES_DIR  + File.separator + saveDir).exists())
                Gdx.files.local(PalidorGame.SAVES_DIR  + File.separator + saveDir).mkdirs();

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
                BufferedReader reader = new BufferedReader(new FileReader(Gdx.files.local(PalidorGame.MAPS_DIR + File.separator +  previousLevel + ".tmx").file()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(Gdx.files.local(PalidorGame.SAVES_DIR  + File.separator + saveDir + File.separator + previousLevel + ".tmx").file()));
            ){
                String line = null;
                while((line = reader.readLine()) != null){

                    if(line.contains("<tileset firstgid=\"1\" source=\"Hunters.tsx\"/>")) {
                        writer.write("<tileset firstgid=\"1\" source=\"../../../maps/Hunters.tsx\"/>");
                        writer.write("\n");
                    }else if( line.contains("<objectgroup name=\"entities\"") ){
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
                for(int i=0;i<ITEMS.size;i++) {
                    writer.write("<object id=\"" + 200 + i + "\" name=\"" + ITEMS.get(i).id + "\" x=\"" + ITEMS.get(i).getBody().getPosition().x * PalidorGame.PPM + "\" y=\"" + (PalidorGame.MAP_HIGHT - ITEMS.get(i).getBody().getPosition().y * PalidorGame.PPM) + "\"/>");
                    writer.write("\n");
                }
                writer.write("</objectgroup>");
                writer.write("\n");

                // creatures
                writer.write("<objectgroup name=\"creatures\">");
                writer.write("\n");
                // write creatures
                for(int i=0;i<CREATURES.size;i++) {
                    writer.write("<object id=\"" + 400 + i + "\" name=\"" + CREATURES.get(i).id + "\" x=\"" + CREATURES.get(i).getBody().getPosition().x * PalidorGame.PPM + "\" y=\"" + (PalidorGame.MAP_HIGHT - CREATURES.get(i).getBody().getPosition().y * PalidorGame.PPM) + "\">\n");

                    writer.write("   <properties>\n" +
                            "    <property name=\"condition\" value=\"\"/>\n" +
                            "    <property name=\"items\" value=\"" + CREATURES.get(i).getInventory().toString().replaceAll("[\\[\\]]","") + "\"/>\n" +
                            "   </properties>\n");
                    writer.write("\n");
                    writer.write("</object>");
                }

                for(int i=0;i<UNAVAILABLE_CREATURES.size;i++) {
                    writer.write("<object id=\"" + 600 + i + "\" name=\"" + UNAVAILABLE_CREATURES.get(i).id + "\" x=\"" + UNAVAILABLE_CREATURES.get(i).rect.x  + "\" y=\"" + (PalidorGame.MAP_HIGHT - UNAVAILABLE_CREATURES.get(i).rect.y) + "\">\n");

                    writer.write("   <properties>\n" +
                            "    <property name=\"condition\" value=\""+UNAVAILABLE_CREATURES.get(i).condition+"\"/>\n" +
                            "    <property name=\"items\" value=\"" + UNAVAILABLE_CREATURES.get(i).items + "\"/>\n" +
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
                for(int i=0;i<OBJECTS.size;i++) {
                    writer.write("<object id=\"" + 800 + i + "\" name=\"" + OBJECTS.get(i).id + "\" x=\"" + OBJECTS.get(i).getBody().getPosition().x * PalidorGame.PPM + "\" y=\"" + (PalidorGame.MAP_HIGHT - OBJECTS.get(i).getBody().getPosition().y * PalidorGame.PPM) + "\">\n");

                    writer.write("   <properties>\n" +
                            "    <property name=\"condition\" value=\"\"/>\n" +
                            "    <property name=\"items\" value=\"" + OBJECTS.get(i).getItems().toString().replaceAll("[\\[\\]]","") + "\"/>\n" +
                            "    <property name=\"program\" value=\"" + OBJECTS.get(i).getProgram() + "\"/>\n" +
                            "   </properties>\n");
                    writer.write("\n");
                    writer.write("</object>");
                }

                for(int i=0;i<UNAVAILABLE_OBJECTS.size;i++) {
                    writer.write("<object id=\"" + 1000 + i + "\" name=\"" + UNAVAILABLE_OBJECTS.get(i).id + "\" x=\"" + UNAVAILABLE_OBJECTS.get(i).rect.x  + "\" y=\"" + (PalidorGame.MAP_HIGHT - UNAVAILABLE_OBJECTS.get(i).rect.y) + "\">\n");

                    writer.write("   <properties>\n" +
                            "    <property name=\"condition\" value=\""+UNAVAILABLE_OBJECTS.get(i).condition+"\"/>\n" +
                            "    <property name=\"items\" value=\"" + UNAVAILABLE_OBJECTS.get(i).items + "\"/>\n" +
                            "    <property name=\"program\" value=\"" + UNAVAILABLE_OBJECTS.get(i).details + "\"/>\n" +
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

    private class UnavailableObject {

        String id;
        Rectangle rect;
        String condition;
        String items;
        String details;


        public UnavailableObject(String id, Rectangle rect, String condition, String items) {
            this.id = id;
            this.rect = rect;
            this.condition = condition;
            this.items = items;
        }

        public UnavailableObject(String id, Rectangle rect, String condition, String items, String details) {
            this.id = id;
            this.rect = rect;
            this.condition = condition;
            this.items = items;
            this.details = details;
        }
    }
}
