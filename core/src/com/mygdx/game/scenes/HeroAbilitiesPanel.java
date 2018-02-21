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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PalidorGame;
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.stuctures.Skill;
import com.mygdx.game.tools.AnimationHelper;

import java.io.File;

/**
 *  Inventory and Equiplment
 */
public class HeroAbilitiesPanel implements Disposable {

    private final AnimationHelper animhelper;
    public Stage stage;
    Viewport viewport;

    //Integer score;

    Label abilitiesHeader;
    Label detailsHeader;
    Image background;

    Label expLabel;

    Image upButton;
    Image downButton;

    SpriteBatch sb;
    Hero hero;


    Label windowHeader;
    Image closeWindow;
    public boolean closeTouched;

    public String currentDetails = "";


    int MAX_NUMBER_OF_ROWS = 18;
    int LAST_DISPLAYED_ROW;
    int INITIAL_DISPLAYED_ROW;

    public HeroAbilitiesPanel(SpriteBatch sb, Hero hero, AnimationHelper animhelper){

        this.sb = sb;
        this.hero = hero;
        this.animhelper = animhelper;
        viewport = new FitViewport(PalidorGame.WIDTH, PalidorGame.HIGHT, new OrthographicCamera());

        background = new Image(new Texture(PalidorGame.SPRITES_DIR + File.separator + "background_clear.png"));

        // locatin of all widgets
        stage = new Stage(viewport,sb);

        abilitiesHeader= new Label(String.format("<< -  %s - >>", "ABILITIES:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;

        windowHeader = new Label(String.format("  %s ",   "Skills/Abilities"), new Label.LabelStyle(new BitmapFont(), Color.BLACK));;;
        closeWindow = new Image(animhelper.getTextureRegionByIDAndIndex("close_button"));;
        closeWindow.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                closeTouched = true;
                return true;
            }
        });

        upButton = new Image(animhelper.getTextureRegionByIDAndIndex("up_button"));
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

        downButton = new Image(animhelper.getTextureRegionByIDAndIndex("down_button"));
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


        Table titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.center().top();
        titleTable.row();
        titleTable.add(windowHeader);
        titleTable.add(closeWindow);


        Table skillsTable = new Table();
        skillsTable.right().bottom();
        skillsTable.debug();
        skillsTable.setFillParent(true);
        skillsTable.row();

        Table abilitiestable = new Table();
        abilitiestable.left().top();
        abilitiestable.setFillParent(true);
        abilitiestable.row();
        abilitiestable.add(abilitiesHeader);
        abilitiestable.row();
        expLabel = new Label(String.format("Experience: %d", hero.experience), new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        abilitiestable.add(expLabel);

        Table detailstable = new Table();
        detailstable.right().top();
        detailstable.setFillParent(true);
        detailstable.row().pad(10,10,10,10);
        detailstable.add(detailsHeader);

        // inventory tableController update

        Array<Label> abilityItems = new Array<Label>();
        Array<Image> abilityItemsImage = new Array<Image>();



        Label skillImage;
        Image takeButton;


        skillsTable.row().pad(8,8,8,8);

        for(int i=0; i<Skill.values().length; i++) {
            final Skill skill = Skill.values()[i];

            if(!hero.skills.contains(skill,true))
                skillImage = new Label(String.format(" %s ", skill.name()), new Label.LabelStyle(new BitmapFont(), Color.DARK_GRAY));
            else
                skillImage = new Label(String.format(" %s ", skill.name()), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
            skillImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentDetails = skill.getDescription();
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });


            skillsTable.add(skillImage);
            if(!hero.skills.contains(skill,true)) {

                takeButton = new Image(animhelper.getTextureRegionByIDAndIndex("icon_cross_blue"));
                takeButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        currentDetails = skill.getDescription();
                        if(hero.hasSkillByID(skill.getIdOfRequiredSkill())){
                            if (hero.experience >= skill.getPrice() ){
                                hero.addSkill(skill);

                                hero.experience = hero.experience - skill.getPrice();
                                //update();
                            } else
                                currentDetails = "NOT ENOUGH EXPERIENCE";
                        } else
                            currentDetails = "REQUIRED SKILL WAS NOT LEARNED";
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        update();
                    }
                });
            }else{
                takeButton = new Image(animhelper.getTextureRegionByIDAndIndex("icon_blank"));
            }
            skillsTable.add(takeButton);


            if(((i+1)%3)==0)  skillsTable.row().pad(8,8,8,8);
        }

        // show all abilities

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
            final int index = i;
            abilitiestable.add(item);
            abilitiestable.row();
            item.addListener(new InputListener(){

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentDetails = hero.getAbilities().get(index).getDescription();
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

        //details tableController update
        detailstable.row();
        detailstable.row().pad(15,15,15,15);
        detailstable.add(new Label(String.format("%s", currentDetails), new Label.LabelStyle(new BitmapFont(), Color.BROWN)));


        stage.addActor(abilitiestable);
        stage.addActor(skillsTable);
        stage.addActor(detailstable);
        stage.addActor(titleTable);

    }

    @Override
    public void dispose() { stage.dispose(); }
}
