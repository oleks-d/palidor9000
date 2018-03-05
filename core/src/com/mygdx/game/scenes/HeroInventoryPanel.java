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
import com.mygdx.game.enums.AbilityID;
import com.mygdx.game.enums.EquipmentType;
import com.mygdx.game.sprites.creatures.Hero;
import com.mygdx.game.sprites.gameobjects.GameItem;
import com.mygdx.game.stuctures.Effect;
import com.mygdx.game.tools.AnimationHelper;
import com.mygdx.game.tools.ConditionProcessor;

import java.io.File;
import java.util.HashMap;

/**
 *  Inventory and Equiplment
 */
public class HeroInventoryPanel implements Disposable {

    private final AnimationHelper animhelper;
    private final Skin skin;
    public Stage stage;
    Viewport viewport;

    //Integer score;

    Label inventoryHeader;
    Label equipedHeader;
    Label effectsHeader;
    Label equipedHeadLabel;
    Label equipedArmorLabel;
    Label equipedWeapon1Label;
    Label equipedWeapon2Label;


    Label moneyLabel;
    Label healthLabel;

    Label detailsHeader;
    Image background;

    Image upButton;
    Image downButton;

    Label selectedHeader;
    Label atackLabel;
    Label defenseLabel;

    SpriteBatch sb;
    Hero hero;

    Label windowHeader;
    Image closeWindow;
    public boolean closeTouched;

    public GameItem currentItem = null;
    public AbilityID currentAbility = null;
    String currentDetails = "";

    int MAX_NUMBER_OF_ROWS = 8;
    int LAST_DISPLAYED_ROW;
    int INITIAL_DISPLAYED_ROW;

    public HeroInventoryPanel(SpriteBatch sb, Hero hero, AnimationHelper animhelper){

        this.sb = sb;
        this.hero = hero;
        this.animhelper = animhelper;
        viewport = new FitViewport(PalidorGame.WIDTH, PalidorGame.HIGHT, new OrthographicCamera());

        // locatin of all widgets
        stage = new Stage(viewport,sb);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        inventoryHeader = new Label(String.format("<< -  %s - >>", "ITEMS:"), new Label.LabelStyle(new BitmapFont(), Color.RED));
        equipedHeader= new Label(String.format("<< -  %s - >>", "EQUIPMENT:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;
        detailsHeader= new Label(String.format("<< -  %s - >>", "DETAILS:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;
        effectsHeader= new Label(String.format("<< -  %s - >>", "EFFECTS:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;

         equipedHeadLabel = new Label(String.format("%s", "Head:"), new Label.LabelStyle(new BitmapFont(), Color.BLACK));;
         equipedArmorLabel= new Label(String.format("%s", "Body:"), new Label.LabelStyle(new BitmapFont(), Color.BLACK));;
         equipedWeapon1Label= new Label(String.format("%s", "Main:"), new Label.LabelStyle(new BitmapFont(), Color.BLACK));;
         equipedWeapon2Label= new Label(String.format("%s", "Secondary:"), new Label.LabelStyle(new BitmapFont(), Color.BLACK));;

        selectedHeader = new Label(String.format("<< -  %s - >>", "SELECTED:"), new Label.LabelStyle(new BitmapFont(), Color.RED));;

        atackLabel = new Label(String.format(" %s: ",   "Atack  "), new Label.LabelStyle(new BitmapFont(), Color.RED));;
        defenseLabel = new Label(String.format(" %s: ", "Defense"), new Label.LabelStyle(new BitmapFont(), Color.RED));;

        background = new Image(new Texture(PalidorGame.SPRITES_DIR + File.separator + "background_clear.png"));

        LAST_DISPLAYED_ROW = 0;
        INITIAL_DISPLAYED_ROW = 0;

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


        windowHeader = new Label(String.format("  %s ",   "Inventory"), new Label.LabelStyle(new BitmapFont(), Color.BLACK));;;
        closeWindow = new Image(animhelper.getTextureRegionByIDAndIndex("close_button"));;
        closeWindow.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                closeTouched = true;
                return true;
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

        Table inventorytable = new Table();
        inventorytable.left().top();
        inventorytable.setFillParent(true);
        inventorytable.row();
        inventorytable.add(inventoryHeader);
        inventorytable.row();
        moneyLabel = new Label(String.format("Money: %d", hero.money), new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        inventorytable.add(moneyLabel);

        Table equipmenttable = new Table();
        equipmenttable.right().top();
        equipmenttable.setFillParent(true);
        equipmenttable.row();
        equipmenttable.add();
        equipmenttable.add(equipedHeader);
        equipmenttable.row();

        Table detailstable = new Table();
        detailstable.bottom();
        detailstable.setFillParent(true);
        detailstable.row();
        detailstable.add(detailsHeader);

        Table selectedTable = new Table();
        selectedTable.right().center();
        selectedTable.setFillParent(true);
        selectedTable.row();
        selectedTable.add(selectedHeader);


        Table effectstable = new Table();
        effectstable.right();
        effectstable.setFillParent(true);
        effectstable.row();
        effectstable.add(effectsHeader);
        effectstable.row();
        healthLabel = new Label(String.format("Health: %d / %d", hero.stats.health.current, hero.stats.health.base), new Label.LabelStyle(new BitmapFont(), Color.RED));
        effectstable.add(healthLabel);

        // inventory tableController update

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
                Image itemImage = inventoryItemsImage.get(i);

                inventorytable.add(item);
                inventorytable.add(itemImage);

                final int index = inventoryIndex.get(i);

                if(hero.getInventory().get(index).getType()!=EquipmentType.NONE) {
                    TextButton equip = new TextButton("Equip", skin);
                    inventorytable.add(equip);

                    equip.addListener(new InputListener(){
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            currentItem = hero.getInventory().get(index);
                            currentDetails = hero.equipItem(hero.getInventory().get(index));
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            update();
                        }
                    });
                };
                // usable
                if(hero.getInventory().get(index).isUsable()) {
                    TextButton equip1 = new TextButton("Use", skin);
                    inventorytable.add(equip1);
                    equip1.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            currentItem = hero.getInventory().get(index);
                            currentDetails = hero.useItem(hero.getInventory().get(index));
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            update();
                        }
                    });
                }

                inventorytable.row();
                item.addListener(new InputListener(){

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if(ConditionProcessor.conditionSatisfied(hero,hero.getInventory().get(index).getCondition()))
                            currentDetails = hero.getInventory().get(index).itemdescription;
                        else
                            currentDetails = "You don't know what is it";
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

        //equiped tableController update
        equipmenttable.row();

        if(hero.head != null) {
            equipmenttable.row();
            equipmenttable.add(equipedHeadLabel);
            Image headImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.head.getIcon()));
            headImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentItem = hero.head;
                    currentDetails = currentItem.itemdescription;
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

        if(hero.armor != null) {
            equipmenttable.row();
            equipmenttable.add(equipedArmorLabel);
            Image armorImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.armor.getIcon()));
            equipmenttable.add(armorImage);
            armorImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentItem = hero.armor;
                    currentDetails = currentItem.itemdescription;
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

        if(hero.weapon1 != null) {
            equipmenttable.row();
            equipmenttable.add(equipedWeapon1Label);
            Image weaponImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.weapon1.getIcon()));
            weaponImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentItem = hero.weapon1;
                    currentDetails = currentItem.itemdescription;
                    hero.unEquipItem(hero.weapon1);
                    System.out.println("UnEquip:" + currentItem.itemname);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });
            equipmenttable.add(weaponImage);
        }


        if(hero.weapon2 != null) {
            equipmenttable.row();
            equipmenttable.add(equipedWeapon2Label);

            Image weaponImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.weapon2.getIcon()));
            weaponImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentItem = hero.weapon2;
                    currentDetails = currentItem.itemdescription;
                    hero.unEquipItem(hero.weapon2);
                    System.out.println("UnEquip:" + currentItem.itemname);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });
            equipmenttable.add(weaponImage);
        }


        selectedTable.row();
        selectedTable.add(atackLabel);

        for(int i =0; i < hero.selectedAtackAbilities.size; i++) {
            final int index = i;
            Image headImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.selectedAtackAbilities.get(index).getIcon()));
            headImage.addListener(new InputListener(){

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentDetails = hero.selectedAtackAbilities.get(index).getDescription();
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });
            selectedTable.add(headImage);
        }

        //selected abilities
        selectedTable.row();
        selectedTable.add(defenseLabel);

        for(int i =0; i < hero.selectedDefenseAbilities.size; i++) {
            final int index = i;
            Image headImage = new Image(animhelper.getTextureRegionByIDAndIndex(hero.selectedDefenseAbilities.get(index).getIcon()));
            headImage.addListener(new InputListener(){

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentDetails = hero.selectedDefenseAbilities.get(index).getDescription();
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });
            selectedTable.add(headImage);
        }

        for(int i =0; i < hero.activeEffects.size; i++){
            final int index = i;
            Label item;
            Image itemImg;
            effectstable.row().pad(10,10,10,10);
            Effect effect = hero.activeEffects.get(i);
            if(effect.duration > 0) {
                if (effect.id.isPositive()) {
                    item = new Label(String.format(" %.1f  %.1f", effect.magnitude, effect.removeTime - hero.existingTime), new Label.LabelStyle(new BitmapFont(), Color.GREEN));
                } else {
                    item = new Label(String.format(" %.1f  %.1f", effect.magnitude, effect.removeTime - hero.existingTime), new Label.LabelStyle(new BitmapFont(), Color.RED));
                }
            } else {
                if (effect.id.isPositive()) {
                    item = new Label(String.format(" %.1f", effect.magnitude), new Label.LabelStyle(new BitmapFont(), Color.GREEN));
                } else {
                    item = new Label(String.format(" %.1f", effect.magnitude), new Label.LabelStyle(new BitmapFont(), Color.RED));
                }
            }

            itemImg = new Image(animhelper.getTextureRegionByIDAndIndex(effect.id.getIcon()));
            itemImg.addListener(new InputListener(){

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    currentDetails = hero.activeEffects.get(index).id.getDescritption();
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    update();
                }
            });

            effectstable.add(itemImg);
            effectstable.add(item);

        }

        //details tableController update
        detailstable.row();
        detailstable.row().pad(15,15,15,15);

        detailstable.add(new Label(String.format("%s", currentDetails), new Label.LabelStyle(new BitmapFont(), Color.BROWN)));

        stage.addActor(titleTable);
        stage.addActor(inventorytable);
        stage.addActor(equipmenttable);
        stage.addActor(selectedTable);
        stage.addActor(detailstable);
        stage.addActor(effectstable);
    }

    @Override
    public void dispose() { stage.dispose(); }
}
