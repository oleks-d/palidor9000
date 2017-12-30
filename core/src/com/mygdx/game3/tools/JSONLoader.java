package com.mygdx.game3.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game2.objects.InteractiveTileObject;
import com.mygdx.game3.enums.EquipmentType;
import com.mygdx.game3.stuctures.Characteristics;
import com.mygdx.game3.stuctures.descriptions.CreatureDescription;
import com.mygdx.game3.stuctures.descriptions.ItemDescription;

import java.io.File;
import java.util.HashMap;

/**
 * Created by odiachuk on 12/21/17.
 */
public class JSONLoader {

    public HashMap<String,CreatureDescription> loadCreatures(){
        HashMap<String,CreatureDescription>  results = new HashMap<String,CreatureDescription>() ;
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("data" + File.separator+ "creatures.json"));


        for (JsonValue component : base.get("creatures"))
        {
            results.put(component.getString("id"),
                    new CreatureDescription(
                            component.getString("name"),
                            component.getString("description"),
                            component.getString("spritesheetregion"),
                            new Characteristics(
                                    Integer.valueOf(component.getString("health")),
                                    Integer.valueOf(component.getString("speed")),
                                    Integer.valueOf(1)),
                            Integer.valueOf(component.getString("org")),
                            tryToGetValue(component, "abilities") != null ? component.getString("abilities") : "",
                            tryToGetValue(component, "effects") != null ? component.getString("effects") : "",
                            tryToGetValue(component, "items") != null ? component.getString("items") : ""
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
                            component.getString("name"),
                            component.getString("description"),
                            component.getString("image"),
                            Integer.valueOf(component.getString("value")),
                            tryToGetValue(component, "type") != null ? EquipmentType.valueOf(component.getString("type")) : EquipmentType.NONE,
                            tryToGetValue(component, "usable") != null ? Boolean.valueOf(component.getString("usable")) : false,
                            tryToGetValue(component, "effects") != null ? component.getString("effects") : ""
                    )
            );
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


}
