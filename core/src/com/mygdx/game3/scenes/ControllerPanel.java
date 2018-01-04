package com.mygdx.game3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game3.HuntersGame;
import com.mygdx.game3.enums.AbilityID;
import com.mygdx.game3.sprites.creatures.Hero;
import com.mygdx.game3.tools.AnimationHelper;

import java.util.LinkedList;

/**
 * Created by odiachuk on 12/18/17.
 */
public class ControllerPanel implements Disposable{

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

    Table tableAbilitiesDefense;
    Table tableAbilitiesAtack;
    Table tableAbilitiesBuff;
    Table table;
    Table tableJump;

    LinkedList<Image> atackabilities;
    LinkedList<Image> defenseabilities;
    LinkedList<Image> helpabilities;

    LinkedList<Label> atackalabels;
    LinkedList<Label> defenselabels;
    LinkedList<Label> helpalabels;

    AnimationHelper animhelper;

    private Vector2 lastTouch;

    public ControllerPanel(SpriteBatch sb, AnimationHelper animhelper){

        viewport = new FitViewport(HuntersGame.WIDTH, HuntersGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);
        this.animhelper = animhelper;

        Gdx.input.setInputProcessor(stage);

        lastTouch = new Vector2();

        updateDirections();

        //titemout1 = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

    }

    private void updateDirections() {

        imageTop = new Image(animhelper.getTextureRegionByIDAndIndex("up_button"));
        imageTop.setSize(50, 50);
        imageLeft = new Image(animhelper.getTextureRegionByIDAndIndex("left_button"));
        imageLeft.setSize(50, 50);
        imageRight = new Image(animhelper.getTextureRegionByIDAndIndex("right_button"));
        imageRight.setSize(50, 50);
        imageDown = new Image(animhelper.getTextureRegionByIDAndIndex("down_button"));
        imageDown.setSize(40, 40);


        imageTop.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchedUp = true;
                //lastTouch.set(x, y);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchedUp = false;
                //touchedLeft = false;
                //touchedRight = false;
            }

//            @Override
//            public void touchDragged(InputEvent event, float x, float y, int pointer) {
//                //touchedUp = true;
//                Vector2 newTouch = new Vector2(x, y);
//                // delta will now hold the difference between the last and the current touch positions
//                // delta.x > 0 means the touch moved to the right, delta.x < 0 means a move to the left
//                Vector2 delta = newTouch.cpy().sub(lastTouch);
//                //lastTouch = newTouch;
//                if(delta.x > 0) {
//                    touchedRight = true;
//                    touchedLeft = false;
//                }else {
//                    touchedLeft = true;
//                    touchedRight = false;
//                }
//                if (delta.y < 0) {
//                        touchedDown = true;
//                        touchedUp = false;
//                }
//                else  if (delta.y > 20) {
//                    if(touchedDown && !touchedUp) {
//                        jumpNow = true;
//                        lastTouch = newTouch;
//                    }
//                    touchedUp = true;
//                    touchedDown = false;
//                }
//            }

//            @Override
//            public boolean (InputEvent event, float x, float y) {
//                //return super.mouseMoved(event, x, y);
//                touchedUp = true;
//                Gdx.app.log("x","" + y);
//                return true;
//            }
        });

        imageDown.addListener(new InputListener(){

//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                touchedDown = true;
//                return true;
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                touchedDown = false;
//            }

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
        //table.add(imageTop).size(imageTop.getWidth(), imageTop.getHeight());
        table.add();
        table.row().pad(5,5,5,5);
        table.add(imageLeft).size(imageLeft.getWidth(), imageLeft.getHeight());
        table.add();
        table.add();
        table.add();
        table.add();
        //table.add(imageDown).size(imageDown.getWidth(), imageDown.getHeight());
        table.add(imageRight).size(imageRight.getWidth(), imageRight.getHeight());


        tableJump = new Table();
        tableJump.right().bottom();
        tableJump.setFillParent(true);
        tableJump.row();
        tableJump.add(imageTop).size(imageTop.getWidth(), imageTop.getHeight());

        stage.addActor(tableJump);
        stage.addActor(table);
    }

    public void update(Hero hero){

        stage.dispose();

        updateDirections();

        Gdx.input.setInputProcessor(stage);

        atackabilities = new LinkedList<Image>();
        defenseabilities = new LinkedList<Image>();
        helpabilities = new LinkedList<Image>();

        atackalabels = new LinkedList<Label>();
        defenselabels = new LinkedList<Label>();
        helpalabels = new LinkedList<Label>();

        tableAbilitiesAtack = new Table();
        tableAbilitiesAtack.right().top();
        tableAbilitiesAtack.setFillParent(true);
        tableAbilitiesAtack.row();

        for(int i = 0; i < hero.selectedAtackAbilities.size; i++) {
            AbilityID ability = hero.selectedAtackAbilities.get(i);
            final int index = i;
            image0 = new Image(animhelper.getTextureRegionByIDAndIndex(ability.getIcon()));
            image0.setSize(40, 40);
            titemout = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

            atackabilities.add(image0);
            atackalabels.add(titemout);

            image0.addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    touched0 = false;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    touched0 = true;
                }
            });

            tableAbilitiesAtack.add(image0).size(image0.getWidth(), image0.getHeight());
            tableAbilitiesAtack.add(titemout);
        }


        tableAbilitiesDefense = new Table();
        tableAbilitiesDefense.right().bottom();
        tableAbilitiesDefense.setFillParent(true);

        for(int i = 0; i < hero.selectedDefenseAbilities.size; i++) {
            AbilityID ability = hero.selectedDefenseAbilities.get(i);
            final int index = i;
            image0 = new Image(animhelper.getTextureRegionByIDAndIndex(ability.getIcon()));
            image0.setSize(40, 40);
            titemout = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

            defenseabilities.add(image0);
            defenselabels.add(titemout);

            image0.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    touched1 = false;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    touched1 = true;
                }
            });

            tableAbilitiesDefense.add(image0).size(image0.getWidth(), image0.getHeight());
            tableAbilitiesDefense.add(titemout);
        }


        tableAbilitiesBuff = new Table();
        tableAbilitiesBuff.left().top();
        tableAbilitiesBuff.setFillParent(true);

        for(int i = 0; i < hero.selectedHelpAbilities.size; i++) {
            AbilityID ability = hero.selectedHelpAbilities.get(i);
            final int index = i;
            image0 = new Image(animhelper.getTextureRegionByIDAndIndex(ability.getIcon()));
            image0.setSize(40, 40);
            titemout = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

            helpabilities.add(image0);
            helpalabels.add(titemout);

            image0.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    touched2 = false;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    touched2 = true;
                }
            });

            tableAbilitiesBuff.add(image0).size(image0.getWidth(), image0.getHeight());
            tableAbilitiesBuff.add(titemout);
        }

        stage.addActor(tableAbilitiesDefense);
        stage.addActor(tableAbilitiesAtack);
        stage.addActor(tableAbilitiesBuff);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void updateLabels(Hero hero) {

        for(int i=0;i<hero.selectedAtackAbilities.size;i++)
            atackalabels.get(i).setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.selectedAtackAbilities.get(i))));
        for(int i=0;i<hero.selectedDefenseAbilities.size;i++)
            defenselabels.get(i).setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.selectedDefenseAbilities.get(i))));
        for(int i=0;i<hero.selectedHelpAbilities.size;i++)
            helpalabels.get(i).setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.selectedHelpAbilities.get(i))));

    }

   public  void makeAtackIconActive(int i){
       Gdx.app.log("Big",i + " ");
       if(atackabilities.size()>=i)
        atackabilities.get(i).setSize(60,60);
       if(atackabilities.size()>1)
        atackabilities.get(i==0? atackabilities.size()-1 : i-1).setSize(40,40);
   }

    public  void makeDefenceIconActive(int i){

        if(defenseabilities.size()>=i)
        defenseabilities.get(i).setSize(60,60);
        if(defenseabilities.size()>1)
        defenseabilities.get(i==0? defenseabilities.size()-1 : i-1).setSize(40,40);
    }
    public  void makeHelpIconActive(int i){
        if(helpabilities.size()>=i)
        helpabilities.get(i).setSize(60,60);
        if(helpabilities.size()>1)
        helpabilities.get(i==0? helpabilities.size()-1 : i-1).setSize(40,40);
    }

    public  void makeAtackIconInActive(int i){
        atackabilities.get(i==0? atackabilities.size()-1 : i-1).setSize(40,40);
    }
    public  void makeDefenceIconInActive(int i){
        defenseabilities.get(i==0? defenseabilities.size()-1 : i-1).setSize(40,40);
    }
    public  void makeHelpIconInActive(int i){
        helpabilities.get(i==0? helpabilities.size()-1 : i-1).setSize(40,40);
    }
}
