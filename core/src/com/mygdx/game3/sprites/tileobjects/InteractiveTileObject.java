package com.mygdx.game3.sprites.tileobjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game3.HuntersGame;

/**
 * Created by odiachuk on 12/18/17.
 */
public abstract class InteractiveTileObject{

    private World world;
    public Body body;
    TiledMap map;
    Rectangle bounds;

    Fixture fixture;

    public InteractiveTileObject (World world, TiledMap map, Rectangle bounds){
        this.world = world;
        this.bounds =   bounds;
        this.map = map;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX() + bounds.getWidth()/2 ) / HuntersGame.PPM, (bounds.getY() + bounds.getHeight()/2 ) / HuntersGame.PPM);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((bounds.getWidth() / 2) / HuntersGame.PPM,bounds.getHeight() / 2 / HuntersGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        fixtureDef.isSensor = true;

        fixture  = body.createFixture(fixtureDef);


    }

    public abstract void onHit();

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * HuntersGame.PPM / HuntersGame.TILE_SIZE), (int) (body.getPosition().y * HuntersGame.PPM / HuntersGame.TILE_SIZE));
    }
}
