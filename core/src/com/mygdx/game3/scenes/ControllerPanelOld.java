package com.mygdx.game3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game3.HuntersGame;
import com.mygdx.game3.sprites.creatures.Hero;
import com.mygdx.game3.tools.AnimationHelper;

/**
 * Created by odiachuk on 12/18/17.
 */
public class ControllerPanelOld implements Disposable{

    private  Label titemout;
    private  Label titemout1;
    private  Label titemout2;
    private  Label titemout3;

    public Stage stage;
    Viewport viewport;

    public boolean touched0;
    public boolean touched1;
    public boolean touched2;
    public boolean touched3;
    public boolean touchedUp;
    public boolean touchedDown;
    public boolean touchedLeft;
    public boolean touchedRight;

    Image imageTop;
    Image imageLeft;
    Image imageRight;
    Image imageDown;

    Image image0;
    Image image1;
    Image image2;
    Image image3;

    Table tableAbilities;
    Table table;

    AnimationHelper animhelper;


    public ControllerPanelOld(SpriteBatch sb, AnimationHelper animhelper){

        viewport = new FitViewport(HuntersGame.WIDTH, HuntersGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);
        this.animhelper = animhelper;

        Gdx.input.setInputProcessor(stage);

        updateDirections();

    }

    private void updateDirections() {

        imageTop = new Image(animhelper.getTextureRegionByIDAndIndex("up_button"));
        imageTop.setSize(40, 40);
        imageLeft = new Image(animhelper.getTextureRegionByIDAndIndex("left_button"));
        imageLeft.setSize(40, 40);
        imageRight = new Image(animhelper.getTextureRegionByIDAndIndex("right_button"));
        imageRight.setSize(40, 40);
        imageDown = new Image(animhelper.getTextureRegionByIDAndIndex("down_button"));
        imageDown.setSize(40, 40);


        titemout = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        titemout1 = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        titemout2 = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        titemout3 = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        imageTop.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchedUp = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchedUp = false;
            }
        });

        imageDown.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchedDown = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchedDown = false;
            }
        });

        imageRight.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchedRight = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchedRight = false;
            }
        });

        imageLeft.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchedLeft = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchedLeft = false;
            }
        });

        table = new Table();
        table.left().bottom();
        table.setFillParent(true);

        table.row().padLeft(5);
        table.add();
        table.add();
        table.row().pad(5,5,5,5);
        table.add(imageLeft).size(imageLeft.getWidth(), imageLeft.getHeight());
        table.add(imageDown).size(imageDown.getWidth(), imageDown.getHeight());
        table.add(imageRight).size(imageRight.getWidth(), imageRight.getHeight());

        stage.addActor(table);
    }

    public void update(Hero hero){

        stage.dispose();

        updateDirections();

        Gdx.input.setInputProcessor(stage);

//        image0 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.ABILITY0.getIcon()));
//        image0.setSize(40, 40);
//        image1 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.ABILITY1.getIcon()));
//        image1.setSize(40, 40);
//
//        image2 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.ABILITY2.getIcon()));
//        image2.setSize(40, 40);
//
//        image3 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.ABILITY3.getIcon()));
//        image3.setSize(40, 40);

        updateLabels(hero);

        image0.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touched0 = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touched0 = false;
            }
        });

        image1.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touched1 = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touched1 = false;
            }
        });

        image2.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touched2 = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touched2 = false;
            }
        });

        image3.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touched3 = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touched3 = false;
            }
        });


        tableAbilities = new Table();
        tableAbilities.right().bottom();
        tableAbilities.setFillParent(true);
        tableAbilities.row();
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add(image0).size(image0.getWidth(), image0.getHeight());
        tableAbilities.add(titemout);
        tableAbilities.row();
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add(image1).size(image1.getWidth(), image1.getHeight());
        tableAbilities.add(titemout1);
        tableAbilities.row();
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add(image2).size(image2.getWidth(), image2.getHeight());
        tableAbilities.add(titemout2);
        tableAbilities.row();
        tableAbilities.add(imageTop).size(imageTop.getWidth(), imageTop.getHeight());
        tableAbilities.add();
        tableAbilities.add(image3).size(image3.getWidth(), image3.getHeight());
        tableAbilities.add(titemout3);

        stage.addActor(tableAbilities);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void updateLabels(Hero hero) {
//        titemout.setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.ABILITY0)));
//        titemout1.setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.ABILITY1)));
//        titemout2.setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.ABILITY2)));
//        titemout3.setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.ABILITY3)));
    }
}
