package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.tools.Fonts;
import com.mygdx.game.tools.LevelManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by odiachuk on 12/18/17.
 */
public class MainPanel implements Disposable {

    private Label newHero;
    private Label selectHero;
    private Label exit;

    public Stage stage;
    Viewport viewport;

    Table table;
    Table savedHeroes;
    Table heroTypes;
    Table titleTable;

    Array<Label> heroNames;

    TextField usernameTextField;

    Label gameTitle;

    Image start;
    Image background;

    com.mygdx.game.tools.AnimationHelper animhelper;

    Skin skin;

    public boolean createNewFlag = false;
    public String createHeroWithType = "";
    public boolean loadHeroFlag = false;
    public boolean exitFlag = false;
    public String heroName;

    public MainPanel(SpriteBatch sb) {

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        viewport = new FitViewport(PalidorGame.WIDTH, PalidorGame.HIGHT, new OrthographicCamera());

        stage = new Stage(viewport, sb);

        gameTitle = new Label("YAG RPG", new Label.LabelStyle(Fonts.GAMEHEADER.getFont(), Color.GOLD));

        update();
    }

    void update() {

        titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.top();
        titleTable.row().pad(10,200,10,10);
        titleTable.add(gameTitle);


        table = new Table();
        table.setFillParent(true);

        usernameTextField = new TextField("", skin);

        savedHeroes = new Table();
        savedHeroes.setFillParent(true);
        savedHeroes.right();

        heroTypes = new Table();
        heroTypes.setFillParent(true);
        heroTypes.left();


        background = new Image(new Texture(PalidorGame.SPRITES_DIR + File.separator + "background_mainmenu.png"));

        //start = new Image(new Texture(PalidorGame.SPRITES_DIR + File.separator +"key.png"));

        heroNames = new Array<Label>();

        heroName = "";


        newHero = new Label("Set New hero name and click Start", new Label.LabelStyle(new BitmapFont(), Color.RED));


        selectHero = new Label("Select hero", new Label.LabelStyle(new BitmapFont(), Color.RED));
        ;

        exit = new Label("Exit", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        exit.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });


        Label labelForHeroName;
        Button removeSaveButton;

        ArrayList<String> listOfHeroes = LevelManager.getListOfSaveHeros();
        if(listOfHeroes.size()>0) {
            savedHeroes.row().pad(10, 10, 10, 100);
            savedHeroes.add(selectHero);
        }

        for (String savedHero : listOfHeroes) {

            final String savedHeroLabel = savedHero;

            labelForHeroName = new Label(savedHero, new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            labelForHeroName.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    heroName = savedHeroLabel;
                    loadHeroFlag = true;
                    return true;
                }
            });

            removeSaveButton = new TextButton("forget...", skin);
            removeSaveButton.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    LevelManager.removeSave(savedHeroLabel);
                    update();
                    return true;
                }
            });

            savedHeroes.row().pad(10,10,10,20);
            savedHeroes.add(labelForHeroName);
            savedHeroes.add(removeSaveButton);
            savedHeroes.add();
        }



        heroTypes.row().pad(10,100,10,10);
        heroTypes.add(newHero);
        heroTypes.row();
        heroTypes.row().pad(10,100,10,10);
        heroTypes.add(usernameTextField);
        heroTypes.row();
        heroTypes.row();
        for (String heroType : LevelManager.getListOfHeroTypes()) {

            final String newheroType = heroType;

            labelForHeroName = new Label(heroType, new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            labelForHeroName.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    heroName = usernameTextField.getText();
                    if (newheroType != null && heroName.matches("[\\w ]+")) {
                        createHeroWithType = newheroType;
                        createNewFlag = true;
                    } else createNewFlag = false;

                    return true;
                }
            });

            heroTypes.row().pad(10,100,10,10);
            heroTypes.add(labelForHeroName);
        }

        table.bottom();
        table.row().pad(10,10,100,10);
        table.add(exit);
        table.row();

        stage.addActor(background);
        stage.addActor(heroTypes);
        stage.addActor(titleTable);
        stage.addActor(savedHeroes);
        stage.addActor(table);


        Gdx.input.setInputProcessor(stage);

    }


    @Override
    public void dispose() {
        stage.dispose();
    }

}
