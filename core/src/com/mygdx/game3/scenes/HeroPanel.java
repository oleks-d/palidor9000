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
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

import java.util.HashMap;

/**
 *  Inventory and Equiplment
 */
public class HeroPanel implements Disposable {

    private final AnimationHelper animhelper;
    public Stage stage;
    Viewport viewport;
    CustomDialog dialog;

    //Integer score;

    Label inventoryHeader;
    Label equipedHeader;
    Label abilitiesHeader;
    Label detailsHeader;
    Image background;

    Image upButton;
    Image downButton;

    SpriteBatch sb;
    Hero hero;

    public GameItem currentItem = null;
    public AbilityID currentAbility = null;

    int MAX_NUMBER_OF_ROWS = 2;
    int LAST_DISPLAYED_ROW;
    int INITIAL_DISPLAYED_ROW;

    public HeroPanel(SpriteBatch sb, Hero hero, AnimationHelper animhelper){

        this.sb = sb;
        this.hero = hero;
        this.animhelper = animhelper;
        viewport = new FitViewport(HuntersGame.WIDTH, HuntersGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);


        inventoryHeader = new Label(String.format("<< -  %s - >>", "ITEMS:"), new Label.LabelStyle(new BitmapFont(), Color.RED));
        equipedHeader= new Label(String.format("<< -  %s - >>", "EQUIPMENT:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;
        abilitiesHeader= new Label(String.format("<< -  %s - >>", "ABILITIES:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;
        detailsHeader= new Label(String.format("<< -  %s - >>", "DETAILS:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;

        background = new Image(new Texture("forest2.jpg"));

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

        Table inventorytable = new Table();
        inventorytable.left().top();
        inventorytable.setFillParent(true);
        inventorytable.row();
        inventorytable.add(inventoryHeader);

        Table equipmenttable = new Table();
        equipmenttable.right().top();
        equipmenttable.setFillParent(true);
        equipmenttable.row();
        equipmenttable.add(equipedHeader);

        Table abilitiestable = new Table();
        abilitiestable.left();
        abilitiestable.setFillParent(true);
        abilitiestable.row();
        abilitiestable.add(abilitiesHeader);

        Table detailstable = new Table();
        detailstable.right();
        detailstable.setFillParent(true);
        detailstable.row();
        detailstable.add(detailsHeader);

        // inventory table update

        Array<Label> inventoryItems = new Array<Label>();
        Array<Image> inventoryItemsImage = new Array<Image>();
        Array<Integer> inventoryIndex = new Array<Integer>();

        Array<Label> abilityItems = new Array<Label>();
        Array<Image> abilityItemsImage = new Array<Image>();

        HashMap<String, Integer> uniqueItems = new HashMap<String, Integer>();
        for (GameItem item : hero.getInventory())
            if(uniqueItems.containsKey(item.itemname))
                uniqueItems.put(item.itemname, uniqueItems.get(item.itemname) + 1);
            else
                uniqueItems.put(item.itemname, 1);

        //TODO add scroll items

        inventorytable.row();
        inventorytable.row();
            for (int i = 0; i < hero.getInventory().size; i++ ) {
                GameItem item = hero.getInventory().get(i);
                if(uniqueItems.get(item.itemname) > 0) {
                    if(uniqueItems.get(item.itemname) > 1)
                        inventoryItems.add(new Label(String.format("%s X %d", item.itemname, uniqueItems.get(item.itemname)), new Label.LabelStyle(new BitmapFont(), Color.BROWN)));
                    else
                        inventoryItems.add(new Label(String.format("%s", item.itemname), new Label.LabelStyle(new BitmapFont(), Color.BROWN)));

                    Image imageTop = new Image(animhelper.getTextureRegionByIDAndIndex(item.getIcon()));
                    imageTop.setSize(20, 20);
                    inventoryItemsImage.add(imageTop);
                    uniqueItems.put(item.itemname,0);
                    inventoryIndex.add(i);
                };

            }
            inventorytable.row();


        if (inventoryItems.size > MAX_NUMBER_OF_ROWS) {
            if(LAST_DISPLAYED_ROW == 0)
                LAST_DISPLAYED_ROW = MAX_NUMBER_OF_ROWS;
        }
        else {
            INITIAL_DISPLAYED_ROW = 0;
            LAST_DISPLAYED_ROW = inventoryItems.size;
        }


        if (inventoryItems.size > MAX_NUMBER_OF_ROWS){
            inventorytable.add(upButton);
            inventorytable.row();
        }

        if(LAST_DISPLAYED_ROW > inventoryItems.size) {
            INITIAL_DISPLAYED_ROW = inventoryItems.size - MAX_NUMBER_OF_ROWS;
            LAST_DISPLAYED_ROW = inventoryItems.size;
        }

            for (int i = INITIAL_DISPLAYED_ROW; i< LAST_DISPLAYED_ROW; i++) {
                Label item  = inventoryItems.get(i);
                Image equip = inventoryItemsImage.get(i);
                final int index = inventoryIndex.get(i);
                inventorytable.add(item);
                inventorytable.add(equip);
                inventorytable.row();
                item.addListener(new InputListener(){

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        currentItem = hero.getInventory().get(index);
                        currentAbility = null;
                        System.out.println(currentItem.itemname);
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        update();
                    }
                });

                equip.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        currentItem = hero.getInventory().get(index);
                        currentAbility = null;
                        if(currentItem.getType() != EquipmentType.NONE) {
                            hero.equipItem(hero.getInventory().get(index));
                            System.out.println("Equip:" + currentItem.itemname);
                        } else
                        if (currentItem.isUsable())
                            hero.useItem(currentItem);
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        update();
                    }
                });
            }

            inventorytable.row();

        if (inventoryItems.size > MAX_NUMBER_OF_ROWS){
            inventorytable.add(downButton);
            inventorytable.row();
        }

        //equiped table update
        equipmenttable.row();
        equipmenttable.add();

        if(hero.head != null) {
            Image headImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.head.getIcon()));
            headImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentItem = hero.head;
                    hero.unEquipItem(hero.head);
                    System.out.println("UnEquip:" + currentItem.itemname);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });
            equipmenttable.add(headImage);
        }

        equipmenttable.row();
        if(hero.armor != null) {
            Image armorImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.armor.getIcon()));
            equipmenttable.add(armorImage);
            armorImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentItem = hero.armor;
                    hero.unEquipItem(hero.armor);
                    System.out.println("UnEquip:" + currentItem.itemname);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });
        }
        equipmenttable.add();
        equipmenttable.add();

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

        for (int i = 0; i<abilityItems.size;i++) {
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
                    currentItem = null;
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
                    setAbility(currentAbility);
                    currentItem = null;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });

        }
        abilitiestable.row();


        //details table update
        detailstable.row();
        detailstable.row();
        if(currentItem != null)
            detailstable.add(new Label(String.format("%s", currentItem.itemdescription), new Label.LabelStyle(new BitmapFont(), Color.BROWN)));
        else if (currentAbility != null)
            detailstable.add(new Label(String.format("%s", currentAbility.getDescription()), new Label.LabelStyle(new BitmapFont(), Color.BROWN)));


        stage.addActor(inventorytable);
        stage.addActor(abilitiestable);
        stage.addActor(equipmenttable);
        stage.addActor(detailstable);

    }

    private void setAbility(AbilityID currentAbility) {
        //Skin uiskin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        //dialog = new CustomDialog("Exit?", uiskin);
        //dialog.getTitleTable().add(new Label("Error", new Label.LabelStyle(new BitmapFont(), Color.WHITE))).center().expand();

        //CustomDialog dialog = new CustomDialog("Exit?", uiskin);
        //stage.addActor(dialog);

        //dialog.show(stage);

//        Table detailstable = new Table();
//        detailstable.center();
//        detailstable.setFillParent(true);
//        detailstable.row();
//        detailstable.add(new Label(String.format("%s", "OOOOOO"), new Label.LabelStyle(new BitmapFont(), Color.BROWN)));
//        stage.addActor(detailstable);
    }

    @Override
    public void dispose() { stage.dispose(); }
}
