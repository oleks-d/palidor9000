package com.mygdx.game3.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game3.CustomDialog;
import com.mygdx.game3.HuntersGame;
import com.mygdx.game3.enums.AbilityID;
import com.mygdx.game3.enums.EquipmentType;
import com.mygdx.game3.sprites.creatures.Hero;
import com.mygdx.game3.sprites.gameobjects.GameItem;
import com.mygdx.game3.tools.AnimationHelper;

import java.io.File;
import java.util.HashMap;

/**
 *  Inventory and Equiplment
 */
public class HeroAbilitiesPanel implements Disposable {

    private final AnimationHelper animhelper;
    public Stage stage;
    Viewport viewport;
    CustomDialog dialog;

    //Integer score;

    Label selectedHeader;
    Label abilitiesHeader;
    Label detailsHeader;
    Image background;

    Label atackLabel;
    Label defenseLabel;
    Label helpLabel;

    Image upButton;
    Image downButton;

    SpriteBatch sb;
    Hero hero;

    public AbilityID currentAbility = null;


    int MAX_NUMBER_OF_ROWS = 15;
    int LAST_DISPLAYED_ROW;
    int INITIAL_DISPLAYED_ROW;

    public HeroAbilitiesPanel(SpriteBatch sb, Hero hero, AnimationHelper animhelper){

        this.sb = sb;
        this.hero = hero;
        this.animhelper = animhelper;
        viewport = new FitViewport(HuntersGame.WIDTH, HuntersGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);

        selectedHeader = new Label(String.format("<< -  %s - >>", "SELECTED:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;
        abilitiesHeader= new Label(String.format("<< -  %s - >>", "ABILITIES:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;
        detailsHeader= new Label(String.format("<< -  %s - >>", "DETAILS:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;

        background = new Image(new Texture(HuntersGame.SPRITES_DIR + File.separator + "background_clear.png"));

        atackLabel = new Label(String.format(" %s: ",   "Atack  "), new Label.LabelStyle(new BitmapFont(), Color.RED));;
        defenseLabel = new Label(String.format(" %s: ", "Defense"), new Label.LabelStyle(new BitmapFont(), Color.RED));;
        helpLabel = new Label(String.format(" %s: ",    "Buff   "), new Label.LabelStyle(new BitmapFont(), Color.RED));;


        LAST_DISPLAYED_ROW = 0;
        INITIAL_DISPLAYED_ROW = 0;

        upButton = new Image(animhelper.getTextureRegionByIDAndIndex("icon_blank"));
        upButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(INITIAL_DISPLAYED_ROW != 0) {
                    INITIAL_DISPLAYED_ROW--;
                    LAST_DISPLAYED_ROW--;
                };
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                update();
            }
        });

        downButton = new Image(animhelper.getTextureRegionByIDAndIndex("icon_blank"));
        downButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                LAST_DISPLAYED_ROW++;
                INITIAL_DISPLAYED_ROW++;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                update();
            }
        });

        update();

    }

    public void update(){

        stage.dispose();

        Gdx.input.setInputProcessor(stage);
        stage.addActor(background);


        Table selectedTable = new Table();
        selectedTable.right().top();
        selectedTable.setFillParent(true);
        selectedTable.row();
        selectedTable.add(selectedHeader);

        Table abilitiestable = new Table();
        abilitiestable.left().top();
        abilitiestable.setFillParent(true);
        abilitiestable.row();
        abilitiestable.add(abilitiesHeader);

        Table detailstable = new Table();
        detailstable.bottom();
        detailstable.setFillParent(true);
        detailstable.row();
        detailstable.add(detailsHeader);

        // inventory table update

        Array<Label> abilityItems = new Array<Label>();
        Array<Image> abilityItemsImage = new Array<Image>();


        //TODO add scroll items

        //equiped table update
        selectedTable.row();
        selectedTable.add(atackLabel);

        for(int i =0; i < hero.selectedAtackAbilities.size; i++) {
            final int index = i;
            Image headImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.selectedAtackAbilities.get(index).getIcon()));
            headImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentAbility = hero.selectedAtackAbilities.get(index);
                    hero.deselectAtackAbility(index);
                    System.out.println("DeSelect:" + currentAbility.getName());
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });
            selectedTable.add(headImage);
        }

        selectedTable.row();
        selectedTable.add(defenseLabel);

        for(int i =0; i < hero.selectedDefenseAbilities.size; i++) {
            final int index = i;
            Image headImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.selectedDefenseAbilities.get(index).getIcon()));
            headImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentAbility = hero.selectedDefenseAbilities.get(index);
                    hero.deselectDefenseAbility(index);
                    System.out.println("DeSelect:" + currentAbility.getName());
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });
            selectedTable.add(headImage);
        }

        selectedTable.row();
        selectedTable.add(helpLabel);

        for(int i =0; i < hero.selectedHelpAbilities.size; i++) {
            final int index = i;
            Image headImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.selectedHelpAbilities.get(index).getIcon()));
            headImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentAbility = hero.selectedHelpAbilities.get(index);
                    hero.deselectHelpAbility(index);
                    System.out.println("DeSelect:" + currentAbility.getName());
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });
            selectedTable.add(headImage);
        }


        selectedTable.add();
        selectedTable.add();

        //abilities table update

        abilitiestable.row();
        abilitiestable.row();
        for (AbilityID item : hero.getAbilities()) {
            abilityItems.add(new Label(String.format("%s", item.getName()), new Label.LabelStyle(new BitmapFont(), Color.BROWN)));
            Image imageTop = new Image(animhelper.getTextureRegionByIDAndIndex(item.getIcon()));
            imageTop.setSize(20, 20);
            abilityItemsImage.add(imageTop);
        }
        abilitiestable.row();


        if (abilityItems.size > MAX_NUMBER_OF_ROWS) {
            if(LAST_DISPLAYED_ROW == 0)
                LAST_DISPLAYED_ROW = MAX_NUMBER_OF_ROWS;
        }
        else {
            INITIAL_DISPLAYED_ROW = 0;
            LAST_DISPLAYED_ROW = abilityItems.size;
        }


        if (abilityItems.size > MAX_NUMBER_OF_ROWS){
            abilitiestable.add(upButton);
            abilitiestable.row();
        }

        if(LAST_DISPLAYED_ROW > abilityItems.size) {
            INITIAL_DISPLAYED_ROW = abilityItems.size - MAX_NUMBER_OF_ROWS;
            LAST_DISPLAYED_ROW = abilityItems.size;
        }

        for (int i = INITIAL_DISPLAYED_ROW; i< LAST_DISPLAYED_ROW; i++)  {
            Label item  = abilityItems.get(i);
            Image select = abilityItemsImage.get(i);
            final int index = i;
            abilitiestable.add(item);
            abilitiestable.add(select);
            abilitiestable.row();
            item.addListener(new InputListener(){

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentAbility = hero.getAbilities().get(index);
                    System.out.println(currentAbility.getName());
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });

            select.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentAbility = hero.getAbilities().get(index);
                    hero.selectAbility(currentAbility);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });

        }
        abilitiestable.row();


        if (abilityItems.size > MAX_NUMBER_OF_ROWS){
            abilitiestable.add(downButton);
            abilitiestable.row();
        }

        //details table update
        detailstable.row();
        detailstable.row();
        if (currentAbility != null)
            detailstable.add(new Label(String.format("%s", currentAbility.getDescription()), new Label.LabelStyle(new BitmapFont(), Color.BROWN)));


        stage.addActor(abilitiestable);
        stage.addActor(selectedTable);
        stage.addActor(detailstable);

    }

    @Override
    public void dispose() { stage.dispose(); }
}
