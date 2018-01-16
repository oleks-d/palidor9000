package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;

public class PalidorGame extends Game {

	public static final int WIDTH = 640;
	public static final int HIGHT = 400;
	public static final float PPM  = 100;

	public static final String TITLE = "HUNTER";
    public static final int TILE_SIZE = 32;
	public static final String DATA_DIR = "data";
	public static final String SPRITES_DIR = "sprites";
	public static final String MAPS_DIR = "maps";
	public static final String DEFAULT_HERO_DIR = PalidorGame.DATA_DIR + File.separator + "basic_heroes";
	public static final String SAVES_DIR = PalidorGame.DATA_DIR + File.separator + "saves";
	//public static final String STARTING_LEVEL = "Pit" ; //"Pit_Mob"; //"Forest";
	public static String gameDetails;
	public static boolean EXIT_FLAG = false;

	public String currentHero;

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
	public static final short NO_RIGHT_POINT = 128;
	public static final short NO_LEFT_POINT = 256;

	@Override
	public void create () {
		//currentHero = "firsthero";

		batch = new SpriteBatch();

		Gdx.gl.glClearColor(0, 0, 0, 1);

		setScreen(new com.mygdx.game.screens.MainMenuScreen(this));
		//setScreen(new GameScreen(this, currentHero));
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
