package com.mygdx.game.stuctures.descriptions;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.enums.GameObjectType;
import com.mygdx.game.stuctures.Effect;

/**
 * Created by odiachuk on 12/21/17.
 */
public class ObjectDescription {

    public String id;
    public GameObjectType type;
    public String program; // movement program / key_description etc.
    public String items; // content
    public String image;

    public ObjectDescription(String id, String type, String image, String program, String items) {
        this.id = id;
        this.image = image;
        this.type = GameObjectType.valueOf(type);
        this.program = program;
        this.items = items;
    }




}
