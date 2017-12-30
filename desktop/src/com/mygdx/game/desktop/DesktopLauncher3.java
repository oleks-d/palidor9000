package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game3.HuntersGame;

public class DesktopLauncher3 {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = HuntersGame.TITLE;
		config.width = HuntersGame.WIDTH;
		config.height = HuntersGame.HIGHT;
		config.foregroundFPS  = 30;


		new LwjglApplication(new HuntersGame(), config);
	}
}
