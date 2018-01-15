package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MenuState;

public class MyGdxGame extends ApplicationAdapter {

	public static final int WIDTH = 640;
	public static final int HIGHT = 400;

	public static final String TITLE = "HUNTER";

	private GameStateManager gsm;

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		//img = new Texture("badlogic.jpg");

		Gdx.gl.glClearColor(1, 0, 0, 1);

		gsm.push(new MenuState(gsm));


	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(1, 0, 0, 1);

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);

		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
		*/
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
