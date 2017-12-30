package com.mygdx.game3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game3.CustomDialog;
import com.mygdx.game3.HuntersGame;
import com.mygdx.game3.tools.LevelManager;

/**
 * Created by odiachuk on 12/18/17.
 */
public class InfoPanel implements Disposable {

    public Stage stage;
    Viewport viewport;

    //Integer score;

    Label levelLabel;
    Label scoreLabel;

    SpriteBatch sb;
    LevelManager lm;

    public InfoPanel(SpriteBatch sb, LevelManager lm){

        this.sb = sb;
        this.lm = lm;
        viewport = new FitViewport(HuntersGame.WIDTH, HuntersGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);

        Table table = new Table();
        table.left().top();
        table.setFillParent(true);

        levelLabel = new Label(lm.currentLevel, new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        //scoreLabel = new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.BROWN));

        table.add(levelLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX().padTop(10);

        stage.addActor(table);
    }

    public void update(){

        stage.dispose();

        // locatin of all widgets
        stage = new Stage(viewport,sb);

        Table table = new Table();
        table.left().top();
        table.setFillParent(true);

        levelLabel = new Label(lm.currentLevel, new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        //scoreLabel = new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.BROWN));

        table.add(levelLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX().padTop(10);

        stage.addActor(table);
/*
        Skin uiskin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        CustomDialog dialog = new CustomDialog("Exit?", uiskin);
        stage.addActor(dialog);
        dialog.show(stage);
        //dialog.act();
        */

    }

    @Override
    public void dispose() { stage.dispose(); }
}
