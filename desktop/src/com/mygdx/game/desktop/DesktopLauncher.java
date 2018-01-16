package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.PalidorGame;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.forceExit = false;
		config.title = PalidorGame.TITLE;
		config.width = PalidorGame.WIDTH;
		config.height = PalidorGame.HIGHT;
		config.foregroundFPS  = 30;


		new LwjglApplication(new PalidorGame(), config);
	}
}
