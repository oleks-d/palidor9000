package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.dialogs.DialogAnswer;
import com.mygdx.game.dialogs.GameDialog;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.sprites.creatures.Creature;
import com.mygdx.game.tools.ConditionProcessor;

/**
 * Created by odiachuk on 12/18/17.
 */
public class DialogPanel implements Disposable {

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

    Array<Label> optionLabels;
    Array<DialogAnswer> currentDialogOptions;

    private Creature actor;
    Array<GameDialog> dialogs;

    Label dialogLabel;

    public int currentReplic = 1; // number of selected on a first screen dialog
    private GameDialog currentGameDialog;

    public DialogPanel(SpriteBatch sb, GameScreen screen){

        this.sb = sb;
        this.screen = screen;
        viewport = new FitViewport(PalidorGame.WIDTH, PalidorGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);

        //pictureClose = new Image(screen.animationHelper.getTextureRegionByIDAndIndex("icon_cross"));

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));;

        optionLabels = new Array<Label>();

        dialogLabel = new Label(" --- --- --- --- --- --- ", new Label.LabelStyle(new BitmapFont(), Color.GOLD));

        update();
    }

    public void update(){

        stage.dispose();

        // locatin of all widgets
        stage = new Stage(viewport,sb);
        Gdx.input.setInputProcessor(stage);

        if(textLabel != null) {
            //stage.addActor(background);

            Table table = new Table(skin);
            table.setPosition(PalidorGame.WIDTH/3,PalidorGame.HIGHT/3);
            table.setBackground(background);
            table.center().top();
            table.add(dialogLabel);

            //table.setFillParent(true);
            //table.setHeight(1);

            //levelLabel = new Label(lm.hero.currentLevel, new Label.LabelStyle(new BitmapFont(), Color.BROWN));
            //scoreLabel = new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.BROWN));

            //table.add(levelLabel).expandX().padTop(10);

            table.row().pad(5,5,5,5);
            table.add(pictureImage);
            table.row().pad(5,5,5,5);
            table.add(nameLabel);
            table.row().pad(5,5,5,5);
            table.add(dialogLabel);
            table.row().pad(5,5,5,5);
            table.add(textLabel);
            table.row().pad(5,5,5,5);
            table.add(dialogLabel);
            table.row().pad(10,10,10,10);
            for(Label option : optionLabels) {
                table.add(option);
                table.row();
            }
            table.pack();


            stage.addActor(table);
        }

    }

    @Override
    public void dispose() { stage.dispose(); }

    public void addMessage(String text, String who, TextureRegion picture){
        //background = new Image(new Texture(PalidorGame.SPRITES_DIR + File.separator + "dialog_background.png"));
        nameLabel = new Label(who, new Label.LabelStyle(new BitmapFont(), Color.CORAL));
        textLabel = new Label(text, new Label.LabelStyle(new BitmapFont(), Color.WHITE));;
        pictureImage = new Image(picture);
        update();
    }

    public void addOptions(Array<DialogAnswer> options){

        for(int i = 0; i < options.size;i++){
            DialogAnswer option = options.get(i);
            if(ConditionProcessor.conditionSatisfied(screen.hero, options.get(i).getCondition())) {
                final int index = i;
                Label label = new Label(String.format("\" %s \"",option.getText()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
                label.addListener(new InputListener() {

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        selectDialogOption(index);
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        update();
                    }
                });

                optionLabels.add(label);
            }
        }
        update();
    }

    //add select dialog options
    public void addDialogOptions(Array<GameDialog> options){

        for(int i = 0; i < options.size;i++){
            GameDialog option = options.get(i);
            if(ConditionProcessor.conditionSatisfied(screen.hero, option.getCondition())) {
                final int index = i;
                Label label = new Label(option.getTitle(), new Label.LabelStyle(new BitmapFont(), Color.CYAN));
                label.addListener(new InputListener() {

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        currentGameDialog = dialogs.get(index);
                        showDialog();
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        update();
                    }
                });

                optionLabels.add(label);
            }

        }
        update();
    }

    public void reset(){
        pictureImage = null;
        nameLabel = null;
        textLabel = null;
        optionLabels = new Array<Label>();
        actor = null;
        dialogs = null;
        currentReplic = 1;
        currentGameDialog = null;
        update();
    }

    public void setDialogs(Creature actor) {
        this.actor = actor;
        this.dialogs = new Array<GameDialog>();
        for(Integer i : actor.getDialogs())
            this.dialogs.add(screen.levelmanager.DIALOGS.get(i));

        addMessage(actor.creatureDescription, actor.name, actor.icon );
        addDialogOptions(dialogs);
    }

    private void showDialog() {
        clearMessage();
        addMessage(currentGameDialog.getReplic().get(currentReplic).getText(), actor.name, actor.icon );
        currentDialogOptions = currentGameDialog.getReplic().get(currentReplic).getAnswers();
        addOptions(currentGameDialog.getReplic().get(currentReplic).getAnswers());
        update();
    }

    private void clearMessage() {
        optionLabels = new Array<Label>();
    }

    public void selectDialogOption(int i) {
        if(currentDialogOptions.get(i).getProcess() != null)
            ConditionProcessor.conditionProcess(screen.hero, currentDialogOptions.get(i).getProcess());
        currentReplic = currentDialogOptions.get(i).getNext();
        if(currentReplic==0)
            screen.endDialog();
        else
            showDialog();
    }

    //showDialog(screen.levelmanager.DIALOGS.get(id), actor.name, actor.icon);

}
