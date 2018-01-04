package com.mygdx.game3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game3.screens.GameScreen;

public class HuntersGame extends Game {

	public static final int WIDTH = 640;
	public static final int HIGHT = 400;
	public static final float PPM  = 100;

	public static final String TITLE = "HUNTER";
    public static final int TILE_SIZE = 32;
	public static final String SPRITES_DIR = "sprites";
	public static final String MAPS_DIR = "maps";
	public static final String STARTING_LEVEL = "Pit" ; //"Pit_Mob"; //"Forest";
	public static String gameDetails;

	private GameStateManager gsm;

	public SpriteBatch getBatch() {
		return batch;
	}

	SpriteBatch batch;

	//flags
	public static final short OBJECT_BIT = 1;
	public static final short HERO_BIT = 2;
	public static final short ITEM_BIT = 4;
	public static final short CREATURE_BIT = 8;
	public static final short ACTIVITY_BIT = 16;
	public static final short JUMP_POINT = 32;
	public static final short TRIGGER_POINT = 64;

	@Override
	public void create () {

		batch = new SpriteBatch();

		Gdx.gl.glClearColor(0, 0, 0, 1);

		//setScreen(new MainMenuScreen(this));
		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
