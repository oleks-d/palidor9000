package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

import java.io.File;
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
    public Viewport viewport;

    public boolean touched0;
    public boolean touched1;
    public boolean touched2;
    public boolean touched3;

    public boolean touchedUp;
    public boolean touchedDown;
    public boolean touchedLeft;
    public boolean touchedRight;
    public boolean touchedJump;
    public boolean touchedUse;
    public boolean touchedAbility1;
    public boolean touchedAbility2;

    public boolean pressedUp;
    public boolean pressedDown;
    public boolean pressedJump;
    public boolean pressedUse;
    public boolean pressedAbility1;
    public boolean pressedAbility2;

    Image controllerImage;
    Image buttonsImage;

    Image image0;
    Image image1;
    Image image2;
    Image image3;

    Table tableController;
    Table tableButtons;
    Table tableAbilities;

    Image jumpImage;
    Image useImage;
    Image ability1;
    Image ability2;

    LinkedList<Image> atackabilities;
    LinkedList<Image> defenseabilities;
    LinkedList<Image> helpabilities;

    LinkedList<Label> atackalabels;
    LinkedList<Label> defenselabels;
    LinkedList<Label> helpalabels;

    com.mygdx.game.tools.AnimationHelper animhelper;

    private Vector2 lastTouch;
    Vector2 newTouch;

    int directionControllerSize = 120;
    int directionControllerSize3 = 40;

    public ControllerPanel(SpriteBatch sb, com.mygdx.game.tools.AnimationHelper animhelper){

        viewport = new FitViewport(PalidorGame.WIDTH, PalidorGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);
        this.animhelper = animhelper;

        Gdx.input.setInputProcessor(stage);

        lastTouch = new Vector2();
        newTouch = new Vector2();

        jumpImage = new Image(animhelper.getTextureRegionByIDAndIndex("icon_forward_green"));
        jumpImage.addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    pressedJump = true;
                    touchedJump = false;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    pressedJump = false;
                    touchedJump = true;
                }
            });
        useImage  = new Image(animhelper.getTextureRegionByIDAndIndex("icon_forward_red")) ;
        useImage.addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    touchedUse = false;
                    pressedUse = true;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    touchedUse = true;
                    pressedUse = false;
                }
            });

        updateDirections();

    }

    private void updateDirections() {

        controllerImage = new Image(new Texture(PalidorGame.SPRITES_DIR + File.separator + "controller.png"));
        controllerImage.setSize(directionControllerSize, directionControllerSize);

        controllerImage.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                checkDirectionsBasedOnCoords(x,y);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                checkDirectionsBasedOnCoords(x,y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchedRight = false;
                touchedDown = false;
                touchedUp = false;
                touchedLeft = false;
            }



        });

        tableController = new Table();
        tableController.left().bottom();
        tableController.setFillParent(true);

        tableController.row().padLeft(5);
        tableController.add(controllerImage);

        buttonsImage = new Image(new Texture(PalidorGame.SPRITES_DIR + File.separator + "controller_buttons.png"));
        buttonsImage.setSize(directionControllerSize, directionControllerSize);

        tableButtons = new Table();
        tableButtons.right().bottom();
        tableButtons.setFillParent(true);
        tableButtons.add(buttonsImage);
        tableButtons.row();

        stage.addActor(tableButtons);
        stage.addActor(tableController);
    }

    private void checkDirectionsBasedOnCoords(float x, float y) {

        if(x> 2*directionControllerSize3) {
            touchedRight = true;
            touchedLeft = false;
        }else if(x< directionControllerSize3){
            touchedRight = false;
            touchedLeft = true;
        } else {
            touchedRight = false;
            touchedLeft = false;
        }

        if(y>2*directionControllerSize3){
            pressedUp = true;
        touchedDown = false;
        touchedUp = true;
        } else if(y<directionControllerSize3){
            pressedDown = true;
            touchedDown = true;
            touchedUp = false;
        } else {
            pressedUp = false;
            pressedDown = false;
            touchedDown = false;
            touchedUp = false;
        }

    }

    public void update(com.mygdx.game.sprites.creatures.Hero hero){

        stage.dispose();

        updateDirections();

        Gdx.input.setInputProcessor(stage);

        tableAbilities = new Table();
        tableAbilities.right().bottom();
        tableAbilities.setFillParent(true);


        ability1 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.weapon1.getIcon())) ;
        ability1.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressedAbility1 = true;
                touchedAbility1 = false;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchedAbility1 = true;
                pressedAbility1 = false;
            }
        });

        ability2 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.weapon2.getIcon())) ;
        ability2.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressedAbility2 = true;
                touchedAbility2 = false;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchedAbility2 = true;
                pressedAbility2 = false;
            }
        });

        tableAbilities.row();
        tableAbilities.row().pad(5,5,5,5);
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add(jumpImage);
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.row().pad(5,5,5,5);;
        tableAbilities.add(ability1);
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add(ability2);
        tableAbilities.row().pad(5,5,5,5);
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.add(useImage);
        tableAbilities.add();
        tableAbilities.add();
        tableAbilities.row();
        tableAbilities.row().pad(5,5,5,5);
        tableAbilities.add();

        stage.addActor(tableButtons);
        stage.addActor(tableAbilities);
        stage.addActor(tableController);

//        atackabilities = new LinkedList<Image>();
//        defenseabilities = new LinkedList<Image>();
//        helpabilities = new LinkedList<Image>();
//
//        atackalabels = new LinkedList<Label>();
//        defenselabels = new LinkedList<Label>();
//        helpalabels = new LinkedList<Label>();
//
//        tableAbilitiesAtack = new Table();
//        tableAbilitiesAtack.right().top();
//        tableAbilitiesAtack.setFillParent(true);
//        tableAbilitiesAtack.row();
//
//        for(int i = 0; i < hero.selectedAtackAbilities.size; i++) {
//            AbilityID ability = hero.selectedAtackAbilities.get(i);
//            final int index = i;
//            image0 = new Image(animhelper.getTextureRegionByIDAndIndex(ability.getIcon()));
//            image0.setSize(40, 40);
//            titemout = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//
//            atackabilities.add(image0);
//            atackalabels.add(titemout);
//
//            image0.addListener(new ClickListener() {
//
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                    touched0 = false;
//                    return true;
//                }
//
//                @Override
//                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                    touched0 = true;
//                }
//            });
//
//            tableAbilitiesAtack.add(image0).size(image0.getWidth(), image0.getHeight());
//            tableAbilitiesAtack.add(titemout);
//        }
//
//
//        tableAbilitiesDefense = new Table();
//        tableAbilitiesDefense.right();
//        tableAbilitiesDefense.setFillParent(true);
//
//        for(int i = 0; i < hero.selectedDefenseAbilities.size; i++) {
//            AbilityID ability = hero.selectedDefenseAbilities.get(i);
//            final int index = i;
//            image0 = new Image(animhelper.getTextureRegionByIDAndIndex(ability.getIcon()));
//            image0.setSize(40, 40);
//            titemout = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//
//            defenseabilities.add(image0);
//            defenselabels.add(titemout);
//
//            image0.addListener(new InputListener() {
//
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                    touched1 = false;
//                    return true;
//                }
//
//                @Override
//                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                    touched1 = true;
//                }
//            });
//
//            tableAbilitiesDefense.add(image0).size(image0.getWidth(), image0.getHeight());
//            tableAbilitiesDefense.add(titemout);
//        }
//
//
//        tableAbilitiesBuff = new Table();
//        tableAbilitiesBuff.left().top();
//        tableAbilitiesBuff.setFillParent(true);
//
//        for(int i = 0; i < hero.selectedHelpAbilities.size; i++) {
//            AbilityID ability = hero.selectedHelpAbilities.get(i);
//            final int index = i;
//            image0 = new Image(animhelper.getTextureRegionByIDAndIndex(ability.getIcon()));
//            image0.setSize(40, 40);
//            titemout = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//
//            helpabilities.add(image0);
//            helpalabels.add(titemout);
//
//            image0.addListener(new InputListener() {
//
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                    touched2 = false;
//                    return true;
//                }
//
//                @Override
//                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                    touched2 = true;
//                }
//            });
//
//            tableAbilitiesBuff.add(image0).size(image0.getWidth(), image0.getHeight());
//            tableAbilitiesBuff.add(titemout);
//        }
//
//        stage.addActor(tableAbilitiesDefense);
//        stage.addActor(tableAbilitiesAtack);
//        stage.addActor(tableAbilitiesBuff);
//
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void updateLabels(com.mygdx.game.sprites.creatures.Hero hero) {

//        for(int i=0;i<hero.selectedAtackAbilities.size;i++)
//            atackalabels.get(i).setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.selectedAtackAbilities.get(i))));
//        for(int i=0;i<hero.selectedDefenseAbilities.size;i++)
//            defenselabels.get(i).setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.selectedDefenseAbilities.get(i))));
//        for(int i=0;i<hero.selectedHelpAbilities.size;i++)
//            helpalabels.get(i).setText(String.format("%.1f",hero.showWhenAbilityWillBeAvailable(hero.selectedHelpAbilities.get(i))));

    }

//   public  void makeAtackIconActive(int i){
//       Gdx.app.log("Big",i + " ");
//       if(atackabilities.size()>=i)
//        atackabilities.get(i).setSize(60,60);
//       if(atackabilities.size()>1)
//        atackabilities.get(i==0? atackabilities.size()-1 : i-1).setSize(40,40);
//   }
//
//    public  void makeDefenceIconActive(int i){
//
//        if(defenseabilities.size()>=i)
//        defenseabilities.get(i).setSize(60,60);
//        if(defenseabilities.size()>1)
//        defenseabilities.get(i==0? defenseabilities.size()-1 : i-1).setSize(40,40);
//    }
//    public  void makeHelpIconActive(int i){
//        if(helpabilities.size()>=i)
//        helpabilities.get(i).setSize(60,60);
//        if(helpabilities.size()>1)
//        helpabilities.get(i==0? helpabilities.size()-1 : i-1).setSize(40,40);
//    }
//
//    public  void makeAtackIconInActive(int i){
//        atackabilities.get(i==0? atackabilities.size()-1 : i-1).setSize(40,40);
//    }
//    public  void makeDefenceIconInActive(int i){
//        defenseabilities.get(i==0? defenseabilities.size()-1 : i-1).setSize(40,40);
//    }
//    public  void makeHelpIconInActive(int i){
//        helpabilities.get(i==0? helpabilities.size()-1 : i-1).setSize(40,40);
//    }

}
