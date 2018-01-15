package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.sprites.HeroSprite;
import com.mygdx.game.sprites.PlatformSprite;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by odiachuk on 12/15/17.
 */
public class PlayState extends State{
    public PlayState(GameStateManager gsm) {
        super(gsm);
        //hero = new Texture("hero1.png");
        cam.setToOrtho(false, 200,200);
        background = new Texture("forest2.jpg");
        hero = new HeroSprite(300,100);


        platforms = new ArrayList<PlatformSprite>();
        for (int i = 0; i < new Random().nextInt(20); i++){
            platforms.add(i, new PlatformSprite(new Random().nextInt(400),new Random().nextInt(400)));
        }

    }

    private HeroSprite hero;
    //private Texture hero;
    private Texture background;
    private ArrayList<PlatformSprite> platforms;

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            if(Gdx.input.getX() < hero.getPosition().x)
                hero.move(-50);
            if(Gdx.input.getX() > hero.getPosition().x)
                hero.move(50);

            hero.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        hero.update(dt);

        for(PlatformSprite ps : platforms){
            Rectangle bounds = ps.getBounds();
            if(hero.collides(bounds)){
                if (hero.getBounds().y >=bounds.y) {

                }
            }
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        //sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0);
        sb.draw(hero.getImage(), hero.getPosition().x, hero.getPosition().y, hero.WIDTH, hero.HIGTH);

        for(PlatformSprite ps : platforms){
            sb.draw(ps.getImage(), ps.getPosition().x, ps.getPosition().y, ps.WIDTH, ps.HIGTH);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        hero.getImage().dispose();
        background.dispose();
    }


}
