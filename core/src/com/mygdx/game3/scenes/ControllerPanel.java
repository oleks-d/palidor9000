package com.mygdx.game3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
public class ControllerPanel implements Disposable{

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

    AnimationHelper animhelper;


    public ControllerPanel(SpriteBatch sb, AnimationHelper animhelper){

        viewport = new FitViewport(HuntersGame.WIDTH, HuntersGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);
        this.animhelper = animhelper;

        Gdx.input.setInputProcessor(stage);

        imageTop = new Image(animhelper.getTextureRegionByIDAndIndex("up_button"));
        imageTop.setSize(40, 40);
         imageLeft = new Image(animhelper.getTextureRegionByIDAndIndex("left_button"));
        imageLeft.setSize(40, 40);
         imageRight = new Image(animhelper.getTextureRegionByIDAndIndex("right_button"));
        imageRight.setSize(40, 40);
         imageDown = new Image(animhelper.getTextureRegionByIDAndIndex("down_button"));
        imageDown.setSize(40, 40);


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

        Table table = new Table();
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

        Gdx.input.setInputProcessor(stage);

        Image image = new Image(animhelper.getTextureRegionByIDAndIndex(hero.ABILITY0.getIcon()));
        image.setSize(40, 40);
        Image image1 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.ABILITY1.getIcon()));
        image.setSize(40, 40);
        Image image2 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.ABILITY2.getIcon()));
        image.setSize(40, 40);
        Image image3 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.ABILITY3.getIcon()));
        image.setSize(40, 40);

        image.addListener(new InputListener(){

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


        Table tableAbilities = new Table();
        tableAbilities.right().bottom();
        tableAbilities.setFillParent(true);
        tableAbilities.row();
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add(image).size(image.getWidth(), image.getHeight());
        tableAbilities.row();
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add(image1).size(image.getWidth(), image.getHeight());
        tableAbilities.row();
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add(image2).size(image.getWidth(), image.getHeight());
        tableAbilities.row();
        tableAbilities.add(imageTop).size(image.getWidth(), image.getHeight());
        tableAbilities.add();
        tableAbilities.add(image3).size(image.getWidth(), image.getHeight());

        stage.addActor(tableAbilities);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
