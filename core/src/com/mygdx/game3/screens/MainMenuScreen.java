package com.mygdx.game3.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game3.HuntersGame;

/**
 * Created by odiachuk on 12/16/17.
 */
public class MainMenuScreen implements Screen {

    private HuntersGame game;

    Texture bg;

    Texture newMenuItemImage;
    Texture newMenuItemImageInactive;
    Rectangle newMenuItem;


    OrthographicCamera camera;

    Viewport viewport;

    Pixmap pic;

    public MainMenuScreen(HuntersGame game) {
        this.game = game;

        //camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, HuntersGame.WIDTH, HuntersGame.HIGHT);
        camera.position.set(HuntersGame.WIDTH / 2, HuntersGame.HIGHT / 2, 0);

        viewport = new FitViewport(HuntersGame.WIDTH, HuntersGame.HIGHT, camera);
        bg = new Texture("forest2.jpg");

        newMenuItem = new Rectangle(HuntersGame.WIDTH/2 , HuntersGame.HIGHT/2, 40, 40);
        newMenuItemImage = new Texture("hero1.png");
        newMenuItemImageInactive = new Texture("hero.png");
        pic = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
        pic.setColor(Color.BLUE);
        pic.drawRectangle(1, 1, 30, 30);

    }


    @Override
    public void show() {

    }

    void update(float delta){



    }

    private void checkMenuItem(Rectangle newMenuItem) {
        //newMenuItemImage.draw(pic, 1, 1);
        if(newMenuItem.contains(Gdx.input.getX(), Gdx.input.getY())) {
            game.getBatch().draw(newMenuItemImage, HuntersGame.WIDTH/2 , HuntersGame.HIGHT/2 - newMenuItem.height);
        } else {
            game.getBatch().draw(newMenuItemImageInactive, HuntersGame.WIDTH/2 , HuntersGame.HIGHT/2 - newMenuItem.height);

        }
        if (Gdx.input.justTouched()) {
            if (newMenuItem.contains(Gdx.input.getX(), Gdx.input.getY()))
                game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void render(float delta) {



        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(bg, 0, 0);
        checkMenuItem(newMenuItem);
        game.getBatch().end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
