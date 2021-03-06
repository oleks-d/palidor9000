package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import com.mygdx.game.dialogs.DialogAnswer;
import com.mygdx.game.dialogs.DialogReplic;
import com.mygdx.game.dialogs.GameDialog;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.stuctures.Characteristics;
import com.mygdx.game.stuctures.Skill;
import com.mygdx.game.stuctures.descriptions.CreatureDescription;
import com.mygdx.game.stuctures.descriptions.ItemDescription;
import com.mygdx.game.stuctures.descriptions.ObjectDescription;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by odiachuk on 12/21/17.
 */
public class JSONLoader {

    public HashMap<String, CreatureDescription> loadCreatures(){
        HashMap<String, CreatureDescription>  results = new HashMap<String, CreatureDescription>() ;
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("data" + File.separator+ "creatures.json"));

        for (JsonValue component : base.get("creatures"))
        {
            results.put(component.getString("id"),
                    new CreatureDescription(
                            component.getString("id"),
                            component.getString("name"),
                            component.getString("description"),
                            component.getString("spritesheetregion"),
                            new Characteristics(
                                    Integer.valueOf(component.getString("health")),
                                    Integer.valueOf(component.getString("speed")),
                                    Integer.valueOf(component.getString("jump"))),
                            //Integer.valueOf(component.getString("org")),
                            tryToGetValue(component, "abilities") != null ? component.getString("abilities") : "",
                            tryToGetValue(component, "effects") != null ? component.getString("effects") : "",
                            tryToGetValue(component, "equipeditems") != null ? component.getString("equipeditems") : "",
                            tryToGetValue(component, "pattern") != null ? component.getString("pattern") : ""
                            ));
            //System.out.println(component.get("asset").getString("relativePath"));
        }

        return results;
    }


    public HashMap<String, ItemDescription> loadItems(){
        HashMap<String,ItemDescription>  results = new HashMap<String,ItemDescription> ();
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("data" + File.separator + "items.json"));

        for (JsonValue component : base.get("items"))
        {
            results.put(component.getString("id"),
                    new ItemDescription(
                            component.getString("id"),
                            component.getString("name"),
                            component.getString("description"),
                            component.getString("image"),
                            Integer.valueOf(component.getString("value")),
                            tryToGetValue(component, "type") != null ? com.mygdx.game.enums.EquipmentType.valueOf(component.getString("type")) : com.mygdx.game.enums.EquipmentType.NONE,
                            tryToGetValue(component, "usable") != null ? Boolean.valueOf(component.getString("usable")) : false,
                            tryToGetValue(component, "effects") != null ? component.getString("effects") : "",
                            tryToGetValue(component, "process") != null ? component.getString("process") : "",
                            tryToGetValue(component, "condition") != null ? component.getString("condition") : "",
                            tryToGetValue(component, "picture") != null ? component.getString("picture") : ""
                    )
            );
        }

        return results;
    }

    public HashMap<Integer, GameDialog> loadDialogs(){
        HashMap<Integer,GameDialog>  results = new HashMap<Integer,GameDialog> ();
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("data" + File.separator + "dialogs.json"));

        for (JsonValue component : base.get("dialogs"))
        {
            GameDialog dialog = new GameDialog(component.getInt("id"));
            dialog.setTitle(component.getString("title"));
            String condition = tryToGetValue(component,"condition");
            if(condition != null && !"".equals(condition))
                dialog.setCondition(condition);

            for(JsonValue replicsComponent : component.get("replics")) {
                DialogReplic replic = new DialogReplic();

                replic.setID(replicsComponent.getInt("id"));
                replic.setText(replicsComponent.getString("text"));
                String conditionForReplic = tryToGetValue(component,"condition");
                if(conditionForReplic != null && !"".equals(conditionForReplic))
                    replic.setCondition(conditionForReplic);

                for (JsonValue answer : replicsComponent.get("answers")) {
                    DialogAnswer dialogOption = new DialogAnswer();
                    dialogOption.setText(answer.getString("text"));
                    String value = tryToGetValue(answer, "condition");
                    dialogOption.setCondition(value == null ? "" : value);
                    value = tryToGetValue(answer, "process");
                    dialogOption.setProcess(value == null ? "" : value);
                    dialogOption.setNext(answer.getInt("next"));
                    replic.addAnswer(dialogOption);
                }

                dialog.addReplic(replic.getID(),replic);

            }
            results.put(component.getInt("id"), dialog);
        }

        return results;
    }

    private String  tryToGetValue(JsonValue component, String fieldname) {
        String result = null;
        try{
        result = component.getString(fieldname);
        }catch (Exception e){
            //
        }
        return result;
    }

    public Hero loadHero(GameScreen screen, FileHandle file){

        CreatureDescription heroDescription;

        String currentLevel = "";
        String previousLevel = "";
        HashMap<String,String> globalStates = new HashMap<String,String>();

        Array<AbilityID> selectedAtackAbilities = new Array<AbilityID>();
        Array<AbilityID> selectedDefenseAbilities = new Array<AbilityID>();
//        Array<AbilityID> selectedHelpAbilities = new Array<AbilityID>();
        Array<AbilityID> summonAbilities = new Array<AbilityID>();

        Array<Skill> skills = new Array<Skill>();
        String items = null;

        int experience= 0;
        int money= 0;

        JsonReader json = new JsonReader();
        try {
            JsonValue base = json.parse(file);


            JsonValue component = base.get("hero");

            heroDescription = new CreatureDescription(
                    "HERO",
                    component.getString("name"),
                    component.getString("description"),
                    component.getString("spritesheetregion"),
                    new Characteristics(
                            Integer.valueOf(component.getString("health")),
                            Integer.valueOf(component.getString("speed")),
                            Integer.valueOf(component.getString("jump"))),
                    //Integer.valueOf(component.getString("org")),
                    tryToGetValue(component, "abilities") != null ? component.getString("abilities") : "",
                    tryToGetValue(component, "effects") != null ? component.getString("effects") : "",
                    tryToGetValue(component, "equipeditems") != null ? component.getString("equipeditems") : "",
                    ""
                    //"" // no dialogs for hero
            );

            items = tryToGetValue(base, ("items"));

            for (JsonValue state : base.get("globalStates")) {
                globalStates.put(state.getString("key"), state.getString("value"));
            }

            experience = Integer.valueOf(base.getString("exp"));
            money = Integer.valueOf(base.getString("money"));


            if(base.getString("selectedAtackAbilities") != null && !"".equals(base.getString("selectedAtackAbilities").trim()))
            for (String item : base.getString("selectedAtackAbilities").split(","))
                selectedAtackAbilities.add(AbilityID.valueOf(item.trim()));
            if(base.getString("selectedDefenseAbilities") != null && !"".equals(base.getString("selectedDefenseAbilities").trim()))
            for (String item : base.getString("selectedDefenseAbilities").split(","))
                selectedDefenseAbilities.add(AbilityID.valueOf(item.trim()));
            if(base.getString("summon") != null && !"".equals(base.getString("summon").trim()))
                for (String item : base.getString("summon").split(","))
                    summonAbilities.add(AbilityID.valueOf(item.trim()));

            if(base.getString("skills") != null && !"".equals(base.getString("skills").trim()))
            for (String item : base.getString("skills").split(","))
                skills.add(Skill.valueOf(item.trim()));

            currentLevel = base.getString("currentLevel");
            previousLevel = base.getString("previousLevel");

            return new Hero(screen, heroDescription, currentLevel, previousLevel, globalStates, selectedAtackAbilities, selectedDefenseAbilities, summonAbilities, skills, experience, money, items);
        }catch (Exception e) {
            //no hero found

            Gdx.app.log("No hero found", "Loading failed " + file );
            e.printStackTrace();
            return null;
        }
    }

    void saveHero(Hero hero){

        String globalStates ="";

        for(Map.Entry<String,String> entry : hero.getGlobalStates().entrySet()){
            if(globalStates.equals(""))
                globalStates = "{ \"key\" : \"" + entry.getKey() + "\", \"value\" : \"" + entry.getValue() + "\"}";
            else
                globalStates = globalStates + ",\n{ \"key\" : \"" + entry.getKey() + "\", \"value\" : \"" + entry.getValue() + "\"}";
        }

        String equipedItems = "";
        if(hero.head != null)
            equipedItems = equipedItems + hero.head.id +",";
        if(hero.armor != null)
            equipedItems = equipedItems + hero.armor.id +",";
        if(hero.weapon1 != null)
            equipedItems = equipedItems + hero.weapon1.id +",";
        if(hero.weapon2 != null)
            equipedItems = equipedItems + hero.weapon2.id +",";
 // TODO add other equipment types

        String stringContent ="" +
                "{\n" +
                "  \"hero\": {\n" +
                "    \"name\": \"" + hero.name +"\",\n" +
                "    \"description\": \"" + hero.creatureDescription + "\",\n" +
                "    \"health\": \""  + hero.stats.health.base + "\",\n" +
                "    \"speed\": \""  + hero.stats.speed.base + "\",\n" +
                "    \"jump\": \""  + hero.stats.jumphight.base + "\",\n" +
                "    \"spritesheetregion\": \"" + hero.spritesheetRegion + "\",\n" +
                "    \"abilities\": \"" + hero.getAbilities().toString(",") + "\",\n" +
                "    \"org\": \"0\",\n" +
                "    \"equipeditems\": \"" + equipedItems + "\"\n" +
                "  },\n" +
                "\n" +
                "    \"items\": \"" + hero.getInventory().toString().replaceAll("[\\[\\]]","") + "\",\n" +
                "  \"globalStates\" : [\n" +
                globalStates +
                "  ],\n" +
                "\n" +
                "  \"currentLevel\" : \"" + hero.currentLevel + "\",\n" +
                "  \"previousLevel\" : \"" + hero.previousLevel + "\",\n" +
                "  \"exp\" : \"" + hero.experience + "\",\n" +
                "  \"money\" : \"" + hero.money + "\",\n" +
                "\n" +
                "  \"selectedAtackAbilities\" : \"" + hero.selectedAtackAbilities.toString().replaceAll("[\\[\\]]", "") + "\",\n" +
                "  \"selectedDefenseAbilities\": \"" + hero.selectedDefenseAbilities.toString().replaceAll("[\\[\\]]", "")+ "\",\n" +
                "  \"summon\": \"" + hero.summonAbilities.toString().replaceAll("[\\[\\]]", "")+ "\",\n" +
                "  \"skills\": \"" + hero.skills.toString().replaceAll("[\\[\\]]", "")+ "\",\n" +
//                "  \"selectedHelpAbilities\": \"" + hero.selectedHelpAbilities.toString().replaceAll("[\\[\\]]", "") + "\"\n" +
                "\n" +
                "\n" +
                "}" ;


        try {
            Json content = new Json();
            if (!Gdx.files.local("data" + File.separator + "saves").exists())
                Gdx.files.local("data" + File.separator + "saves").mkdirs();
            JsonWriter jw = new JsonWriter(new FileWriter(Gdx.files.local("data" + File.separator + "saves" + File.separator + hero.name + ".json").file()));
            jw.write((stringContent));
            jw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, ObjectDescription> loadObjects() {
        HashMap<String, ObjectDescription> results = new HashMap<>();
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("data" + File.separator + "objects.json"));

        for (JsonValue component : base.get("objects"))
        {
            results.put(component.getString("id"),
                    new ObjectDescription(
                            component.getString("id"),
                            component.getString("type"),
                            component.getString("image"),
                            tryToGetValue(component, "program") != null ? component.getString("program") : "",
                            tryToGetValue(component, "items") != null ? component.getString("items") : ""
                            )
            );
        }

        return results;
    }
}
