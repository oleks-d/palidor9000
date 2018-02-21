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
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.tools.AnimationHelper;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by odiachuk on 12/18/17.
 */
public class ControllerPanel implements Disposable{

    public Stage stage;
    public Viewport viewport;

    public boolean touchedUp;
    public boolean touchedDown;
    public boolean touchedLeft;
    public boolean touchedRight;
    public boolean touchedJump;
    public boolean touchedUse;
    public boolean touchedAbility1;
    public boolean touchedAbility2;
    public boolean showMenuTouched;
    public boolean helpTouched;

    public boolean pressedUp;
    public boolean pressedDown;
    public boolean pressedJump;
    public boolean pressedUse;
    public boolean pressedAbility1;
    public boolean pressedAbility2;

    Image controllerImage;
    Image buttonsImage;

    Table tableController;
    Table tableButtons;
    Table tableAbilities;
    Table tableSummon;
    Table tableMenu;


    Table tableAbilitiesAtack;
    Table tableAbilitiesDefense;

    Image image0;
    Label abilitytimeout;

    Image menuImage;
    Image inventoryImage;
    Image abilitiesImage;
    Image escapeImage;
    Image helpImage;

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

    Label summonLabel;

    AnimationHelper animhelper;

    private Vector2 lastTouch;
    Vector2 newTouch;

    int directionControllerSize = 210;
    int directionControllerSize3 = 70;
    public boolean escapeTouched;
    public boolean inventoryToched;
    public boolean abilitiesToched;
    private Hero hero;
    public boolean touchedSummon0;

    public int currentSummonCreatureIndex = -1;

    public ControllerPanel(SpriteBatch sb, AnimationHelper animhelper){

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
                    touchedJump = true;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    pressedJump = false;
                    touchedJump = false;
                }
            });
        useImage  = new Image(animhelper.getTextureRegionByIDAndIndex("icon_forward_red")) ;
        useImage.addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    touchedUse = true;
                    pressedUse = true;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    touchedUse = false;
                    pressedUse = false;
                }
            });


        menuImage  = new Image(animhelper.getTextureRegionByIDAndIndex("dialog2")) ;
        menuImage.setSize(30, 30);
        menuImage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showMenuTouched = !showMenuTouched;
                update();
                return true;
            }
        });

        inventoryImage  = new Image(animhelper.getTextureRegionByIDAndIndex("hummer")) ;
        inventoryImage.setSize(30, 30);
        inventoryImage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                inventoryToched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               inventoryToched = false;
            }
        });

        abilitiesImage = new Image(animhelper.getTextureRegionByIDAndIndex("red_bottle")) ;
        abilitiesImage.setSize(30, 30);
        abilitiesImage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                abilitiesToched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                abilitiesToched = false;
            }
        });

        escapeImage  = new Image(animhelper.getTextureRegionByIDAndIndex("close_button")) ;
        escapeImage.setSize(30, 30);
        escapeImage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                escapeTouched = true;
                return true;
            }
        });


        helpImage  = new Image(animhelper.getTextureRegionByIDAndIndex("dialog1")) ;
        helpImage.setSize(30, 30);
        helpImage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                helpTouched = true;
                return true;
            }
        });
        updateMenuTabel();

        updateDirections();

    }

    void updateMenuTabel(){
        tableMenu = new Table();
        tableMenu.left().top();
        tableMenu.setFillParent(true);

        tableMenu.row().pad(10,10,10,10);
        tableMenu.add(menuImage);
        tableMenu.add(inventoryImage);
        tableMenu.add(abilitiesImage);

        if(showMenuTouched){
            tableMenu.row().pad(10,10,10,10);
            tableMenu.add(helpImage);
            tableMenu.row().pad(10,10,10,10);
            tableMenu.add(escapeImage);
        }

        stage.addActor(tableMenu);
    }

    private void updateDirections() {

        controllerImage = new Image(new Texture(PalidorGame.SPRITES_DIR + File.separator + "controller_small.png"));
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

        buttonsImage.addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    checkButtonsBasedOnCoords(x,y);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    touchedAbility1 = false;
                    touchedAbility2 = false;
                    touchedJump = false;
                }
            });

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
    }

    private void checkDirectionsBasedOnCoords_old(float x, float y) {

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

    private void checkButtonsBasedOnCoords(float x, float y) {

        if(x> directionControllerSize3 && x< 2*directionControllerSize3 && y>2*directionControllerSize3) {
            touchedJump = true;
        }

        if(y> directionControllerSize3 && y< 2*directionControllerSize3 && x< directionControllerSize3 ) {
            touchedAbility1 = true;
        }

        if(y> directionControllerSize3 && y< 2*directionControllerSize3 && x> 2*directionControllerSize3 ) {
            touchedAbility2 = true;
        }

        if(x> directionControllerSize3 && x< 2*directionControllerSize3 && y<directionControllerSize3) {
            touchedUse = true;
        }

    }

    public void update(){
        update(hero);
    }

    public void update(Hero hero) {

        this.hero = hero;
        stage.dispose();

        updateDirections();
        updateMenuTabel();

        Gdx.input.setInputProcessor(stage);

        if(currentSummonCreatureIndex == -1){
            if(hero.summonAbilities.size > 0 )
                currentSummonCreatureIndex = 0;
            else
                currentSummonCreatureIndex = -1;
            }


        tableSummon = new Table();
        tableSummon.bottom();
        tableSummon.setFillParent(true);

        if(hero.summonAbilities.size > 0){
            Image imageOfCreature = new Image(animhelper.getTextureRegionByIDAndIndex(hero.summonAbilities.get(currentSummonCreatureIndex).getIcon()));
            abilitytimeout = new Label( hero.summonAbilities.get(currentSummonCreatureIndex).getName(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
            summonLabel = abilitytimeout;
            imageOfCreature.addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    touchedSummon0= true;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    touchedSummon0 = false;
                }
            });
            tableSummon.row().pad(10,10,10,10);
            tableSummon.add();
            tableSummon.add(imageOfCreature);
            tableSummon.add(abilitytimeout);
        }
        tableSummon.row().pad(10,10,10,10);

//        tableAbilities = new Table();
//        tableAbilities.right().bottom();
//        tableAbilities.setFillParent(true);
//
//
//        if(hero.selectedAtackAbilities.size>1) {
//            ability1 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.selectedAtackAbilities.get(0).getIcon()));
//            ability1.addListener(new ClickListener() {
//
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                    pressedAbility1 = true;
//                    touchedAbility1 = true;
//                    return true;
//                }
//
//                @Override
//                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                    touchedAbility1 = false;
//                    pressedAbility1 = false;
//                }
//            });
//        }
//
//        if(hero.selectedDefenseAbilities.size>1) {
//            ability2 = new Image(animhelper.getTextureRegionByIDAndIndex(hero.selectedDefenseAbilities.get(0).getIcon()));
//            ability2.addListener(new ClickListener() {
//
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                    pressedAbility2 = true;
//                    touchedAbility2 = true;
//                    return true;
//                }
//
//                @Override
//                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                    touchedAbility2 = false;
//                    pressedAbility2 = false;
//                }
//            });
//        }

//        tableAbilities.row();
//        tableAbilities.row().pad(10,10,10,10);
//        tableAbilities.add();
//        tableAbilities.add();
//        tableAbilities.add(jumpImage);
//        tableAbilities.add();
//        tableAbilities.add();
//        tableAbilities.row().pad(10,10,10,10);
//        if(ability1 != null)
//        tableAbilities.add(ability1);
//        tableAbilities.add();
//        tableAbilities.add();
//        tableAbilities.add();
//        if(ability2 != null)
//        tableAbilities.add(ability2);
//        tableAbilities.row().pad(10,10,10,10);
//        tableAbilities.add();
//        tableAbilities.add();
//        tableAbilities.add(useImage);
//        tableAbilities.add();
//        tableAbilities.add();
//        tableAbilities.row();
//        tableAbilities.row().pad(10,10,10,10);
//        tableAbilities.add();

        stage.addActor(tableButtons);
        //stage.addActor(tableAbilities);
        stage.addActor(tableController);

        stage.addActor(tableSummon);

//        atackabilities = new LinkedList<Image>();
//        defenseabilities = new LinkedList<Image>();
//        helpabilities = new LinkedList<Image>();
//
        atackalabels = new LinkedList<Label>();
        defenselabels = new LinkedList<Label>();
//        helpalabels = new LinkedList<Label>();

//
//        tableAbilitiesAtack = new Table();
//        tableAbilitiesAtack.bottom();
//        tableAbilitiesAtack.setFillParent(true);
//        tableAbilitiesAtack.row();

        for(int i = 0; i < hero.selectedAtackAbilities.size; i++) {
            AbilityID ability = hero.selectedAtackAbilities.get(i);
            final int index = i;
            image0 = new Image(animhelper.getTextureRegionByIDAndIndex(ability.getIcon()));
            image0.setSize(30, 30);
            abilitytimeout = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

            atackalabels.add(abilitytimeout);

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

            tableSummon.add(image0).size(image0.getWidth(), image0.getHeight());
            tableSummon.add(abilitytimeout);
            tableSummon.add();
        }
        tableSummon.row().pad(10,10,10,10);


//        tableAbilitiesDefense = new Table();
//        tableAbilitiesDefense.bottom();
//        tableAbilitiesDefense.setFillParent(true);

        for(int i = 0; i < hero.selectedDefenseAbilities.size; i++) {
            AbilityID ability = hero.selectedDefenseAbilities.get(i);
            final int index = i;
            image0 = new Image(animhelper.getTextureRegionByIDAndIndex(ability.getIcon()));
            image0.setSize(30, 30);
            abilitytimeout = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

            defenselabels.add(abilitytimeout);

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

            tableSummon.add(image0).size(image0.getWidth(), image0.getHeight());
            tableSummon.add(abilitytimeout);
            tableSummon.add();
        }

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

    public void updateLabels(Hero hero) {

        for(int i=0;i<hero.selectedAtackAbilities.size;i++) {
            if(hero.showWhenAbilityWillBeAvailable(hero.selectedAtackAbilities.get(i)) == 0){
                atackalabels.get(i).setText("+");
                atackalabels.get(i).setColor(Color.GREEN);
            } else {
                atackalabels.get(i).setText(String.format("%.1f", hero.showWhenAbilityWillBeAvailable(hero.selectedAtackAbilities.get(i))));
                atackalabels.get(i).setColor(Color.RED);
            }
        }
        for(int i=0;i<hero.selectedDefenseAbilities.size;i++) {
            if(hero.showWhenAbilityWillBeAvailable(hero.selectedDefenseAbilities.get(i)) == 0){
                defenselabels.get(i).setText("+");
                defenselabels.get(i).setColor(Color.GREEN);
            }else{
                defenselabels.get(i).setText(String.format("%.1f", hero.showWhenAbilityWillBeAvailable(hero.selectedDefenseAbilities.get(i))));
                defenselabels.get(i).setColor(Color.RED);
            }
        }

        if(currentSummonCreatureIndex > -1) {
            if (hero.showWhenAbilityWillBeAvailable(hero.summonAbilities.get(currentSummonCreatureIndex)) == 0) {
                summonLabel.setText(hero.summonAbilities.get(currentSummonCreatureIndex).getName());
                summonLabel.setColor(Color.GREEN);
            } else {
                summonLabel.setText(String.format("%.1f", hero.showWhenAbilityWillBeAvailable(hero.summonAbilities.get(currentSummonCreatureIndex))));
                summonLabel.setColor(Color.RED);
            }
        }

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
