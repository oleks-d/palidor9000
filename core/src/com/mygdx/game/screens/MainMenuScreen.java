package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.scenes.MainPanel;

import java.io.File;

/**
 * Created by odiachuk on 12/16/17.
 */
public class MainMenuScreen implements Screen {

    private PalidorGame game;

    Texture bg;

    Texture newMenuItemImage;
    Texture newMenuItemImageInactive;
    Rectangle newMenuItem;

    Texture loadMenuItemImage;
    Texture loadMenuItemImageInactive;
    Rectangle loadMenuItem;

    Texture exitMenuItemImage;
    Texture exitMenuItemImageInactive;
    Rectangle exitMenuItem;

    OrthographicCamera camera;

    Viewport viewport;

    Pixmap pic;


    public MainPanel mainPanel;


    public MainMenuScreen(PalidorGame game) {
        this.game = game;

        //camera
        camera = new OrthographicCamera();
//        camera.setToOrtho(false, PalidorGame.WIDTH, PalidorGame.HIGHT);
//        camera.position.set(PalidorGame.WIDTH / 2, PalidorGame.HIGHT / 2, 0);
//
        viewport = new FitViewport(PalidorGame.WIDTH, PalidorGame.HIGHT, camera);
        bg = new Texture(PalidorGame.SPRITES_DIR + File.separator +"background_mainmenu.png");
//
//        newMenuItem = new Rectangle(PalidorGame.WIDTH/2 , PalidorGame.HIGHT/2 + 40, 40, 40);
//        newMenuItemImage = new Texture(PalidorGame.SPRITES_DIR + File.separator +"new_hero.png");
//        newMenuItemImageInactive = new Texture(PalidorGame.SPRITES_DIR + File.separator +"new_hero_active.png");
//
//        loadMenuItem = new Rectangle(PalidorGame.WIDTH/2 , PalidorGame.HIGHT/2 + 80, 40, 40);
//        loadMenuItemImage = new Texture(PalidorGame.SPRITES_DIR + File.separator +"select_hero.png");
//        loadMenuItemImageInactive = new Texture(PalidorGame.SPRITES_DIR + File.separator +"select_hero_active.png");
//
//        loadMenuItem = new Rectangle(PalidorGame.WIDTH/2 , PalidorGame.HIGHT/2 + 120, 40, 40);
//        loadMenuItemImage = new Texture(PalidorGame.SPRITES_DIR + File.separator +"exit.png");
//        loadMenuItemImageInactive = new Texture(PalidorGame.SPRITES_DIR + File.separator +"exit_active.png");
//
//        pic = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
//        pic.setColor(Color.BLUE);
//        pic.drawRectangle(1, 1, 30, 30);

        mainPanel = new MainPanel(game.getBatch());

        //set camera to center
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);



    }


    @Override
    public void show() {

    }

    void update(float delta){



    }

//    private void checkMenuItem(Rectangle menuItem) {
//        //newMenuItemImage.draw(pic, 1, 1);
//        if(menuItem.contains(Gdx.input.getX(), Gdx.input.getY())) {
//            game.getBatch().draw(newMenuItemImage, PalidorGame.WIDTH/2 , PalidorGame.HIGHT/2 - newMenuItem.height);
//        } else {
//            game.getBatch().draw(newMenuItemImageInactive, PalidorGame.WIDTH/2 , PalidorGame.HIGHT/2 - newMenuItem.height);
//
//        }
//        if (Gdx.input.justTouched()) {
//            if (newMenuItem.contains(Gdx.input.getX(), Gdx.input.getY()))
//                game.setScreen(new GameScreen(game, game.currentHero));
//        }
//    }

    @Override
    public void render(float delta) {

        camera.update();
        game.getBatch().setProjectionMatrix(mainPanel.stage.getCamera().combined);
        mainPanel.stage.draw();
        //mainPanel.stage.act();

//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        game.getBatch().setProjectionMatrix(camera.combined);
//        game.getBatch().begin();
//        game.getBatch().draw(bg, 0, 0);
//        checkMenuItem(newMenuItem);
//        checkMenuItem(loadMenuItem);
//        checkMenuItem(exitMenuItem);
//        game.getBatch().end();

        if(mainPanel.createNewFlag){
            game.setScreen(new GameScreen(game, mainPanel.heroName, mainPanel.createHeroWithType));
            dispose();
        } else if(mainPanel.loadHeroFlag){
            game.setScreen(new GameScreen(game, mainPanel.heroName));
            dispose();
        }


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
