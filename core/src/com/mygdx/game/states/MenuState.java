package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

/**
 * Created by odiachuk on 12/15/17.
 */
public class MenuState extends State{
    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("forest.png");
        startButton = new Texture("arrow.png");
    }

    private Texture background;
    private Texture startButton;

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0);
        sb.draw(startButton, MyGdxGame.WIDTH/2-startButton.getWidth()/2, MyGdxGame.HIGHT/2-startButton.getHeight()/2);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        startButton.dispose();
    }


}
