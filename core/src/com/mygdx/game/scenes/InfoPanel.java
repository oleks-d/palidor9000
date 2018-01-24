package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.tools.LevelManager;

import java.io.File;

/**
 * Created by odiachuk on 12/18/17.
 */
public class InfoPanel implements Disposable {

    public Stage stage;
    Viewport viewport;

    String background = "default-pane";

    Label levelLabel;

    Label textLabel;
    Label nameLabel;
    Image pictureImage;
    Image pictureClose;

    SpriteBatch sb;
    GameScreen screen;

    Skin skin;

    public InfoPanel(SpriteBatch sb, GameScreen screen){

        this.sb = sb;
        this.screen = screen;
        viewport = new FitViewport(PalidorGame.WIDTH, PalidorGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);

        //pictureClose = new Image(screen.animationHelper.getTextureRegionByIDAndIndex("icon_cross"));

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));;

//        Table table = new Table();
//        table.left().top();
//        table.setFillParent(true);

        //levelLabel = new Label(lm.hero.currentLevel, new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        //scoreLabel = new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.BROWN));

        //table.add(levelLabel).expandX().padTop(10);

//        stage.addActor(table);
    }

    public void update(){

        stage.dispose();

        // locatin of all widgets
        stage = new Stage(viewport,sb);
        if(textLabel != null) {
            //stage.addActor(background);

            Table table = new Table(skin);
            table.setPosition(10,200);
            table.setBackground(background);
            table.center().top();

            //table.setFillParent(true);
            //table.setHeight(1);

            //levelLabel = new Label(lm.hero.currentLevel, new Label.LabelStyle(new BitmapFont(), Color.BROWN));
            //scoreLabel = new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.BROWN));

            //table.add(levelLabel).expandX().padTop(10);

            table.row();
            table.add(pictureImage);
            table.row();
            table.add(nameLabel);
            table.row();
            table.add(textLabel);
            table.row();
            table.pack();


            stage.addActor(table);
        }

    }

    @Override
    public void dispose() { stage.dispose(); }

    public void addMessage(String text, String who, String picture){
        //background = new Image(new Texture(PalidorGame.SPRITES_DIR + File.separator + "dialog_background.png"));
        nameLabel = new Label(who, new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        textLabel = new Label(text, new Label.LabelStyle(new BitmapFont(), Color.BLACK));;
        pictureImage = new Image(screen.animationHelper.getTextureRegionByIDAndIndex(picture));;
        update();
    }

    public void reset(){
        pictureImage = null;
        nameLabel = null;
        textLabel = null;
        update();
    }
}
