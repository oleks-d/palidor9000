package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.tools.AnimationHelper;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by odiachuk on 12/18/17.
 */
public class InfoPanel implements Disposable{

    public Stage stage;
    public Viewport viewport;


    Image healthBar;
    Image castBar;

    Table tableHeroBars;
    Table tableCreaturesBars;

    Image menuImage;
    Image inventoryImage;
    Image abilitiesImage;

    LinkedList<Label> creaturesLabels;

    //Label heroName;


    AnimationHelper animhelper;

    private Hero hero;

    private GameScreen screen;

    Label heroNameLabel;

    Image heroHealthBar;


    public InfoPanel(SpriteBatch sb, AnimationHelper animhelper){

        viewport = new FitViewport(PalidorGame.WIDTH, PalidorGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);
        this.animhelper = animhelper;

        Gdx.input.setInputProcessor(stage);

        heroNameLabel = new Label("Unknown", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        heroHealthBar = new Image(animhelper.getTextureRegionByIDAndIndex("healthbar"));

/*

        if(owner.IN_BATTLE || owner.stats.health.base > owner.stats.health.current){
            partOfCastbar = (float)owner.stats.health.current / owner.stats.health.base;
            batch.draw(healthbar, getX(), getY() + PalidorGame.TILE_SIZE/2/PPM, (2*PalidorGame.TILE_SIZE / PPM) * partOfCastbar, 16 / PPM);
            //batch.draw(castbar2, getX(), getY() + PalidorGame.TILE_SIZE/2/PPM, (2*PalidorGame.TILE_SIZE / PPM), 16 / PPM);
            //     batch.draw(battleMark, getX() + i * PalidorGame.TILE_SIZE / 2 / PPM, getY(), PalidorGame.TILE_SIZE / 2 / PPM, PalidorGame.TILE_SIZE / 2 / PPM);
        }

        if(owner.abilityToCastExecutionTime > 0.2 || timeSpentOnCast > 0) {
            partOfCastbar = (((timeSpentOnCast>0 ? timeSpentOnCast : owner.timeSpentOnCast) / (abilityToCastExecutionTime > 0 ? abilityToCastExecutionTime : owner.abilityToCastExecutionTime)));
            if (partOfCastbar > 1) partOfCastbar = 1;
            batch.draw(castbar, getX(), getY() + PalidorGame.TILE_SIZE/PPM, (2*PalidorGame.TILE_SIZE / PPM) * partOfCastbar, 16 / PPM);
            batch.draw(castbar2, getX(), getY() + PalidorGame.TILE_SIZE/PPM, (2*PalidorGame.TILE_SIZE / PPM), 16 / PPM);
            Fonts.NAMES.getFont().draw(batch, "".equals(abilityToCastName) ? owner.abilityToCast.getName() : abilityToCastName,   getX() - PalidorGame.TILE_SIZE/PPM, getY()  + PalidorGame.TILE_SIZE/PPM);
        }
        */

        updateHeroInfoTabel();

    }

    void updateHeroInfoTabel(){

        tableHeroBars = new Table();
        tableHeroBars.top();
        tableHeroBars.setFillParent(true);

        tableHeroBars.row().pad(10,10,10,10);
        tableHeroBars.add(heroNameLabel);
        tableHeroBars.row().pad(10,10,10,10);
        tableHeroBars.add(heroHealthBar);  //TODO update halthbar

        stage.addActor(tableHeroBars);
    }


    public void update(GameScreen screen) {

        this.screen = screen;
        this.hero = screen.hero;
        stage.dispose();

        heroNameLabel.setText(screen.hero.name);

        updateHeroInfoTabel();

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}