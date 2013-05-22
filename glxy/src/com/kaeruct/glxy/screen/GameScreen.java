package com.kaeruct.glxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kaeruct.glxy.GlxyGame;
import com.kaeruct.glxy.actor.Universe;

public class GameScreen extends Screen {
	final Universe universe;
	
	public GameScreen (GlxyGame gg) {
		super(gg);
		
		universe = new Universe();
		
		TextButton t1 = new TextButton("Increase", skin);
		t1.addListener(new ClickListener() {
		    public void clicked(InputEvent event, float x, float y) {
		    	universe.increaseParticleRadius();
		    }
		});
		t1.pad(5, 10, 5, 10);
		
		TextButton t2 = new TextButton("Decrease", skin);
		t2.addListener(new ClickListener() {
		    public void clicked(InputEvent event, float x, float y) {
		    	universe.decreaseParticleRadius();
		    }
		});
		t2.pad(5, 10, 5, 10);
		
		table.add(universe).fill().expand().colspan(2);
		table.row();
		table.add(t1).pad(10);
		table.add(t2).pad(10);
		
		InputMultiplexer im = new InputMultiplexer(stage, universe.gestureDetector); // Order matters here!
		Gdx.input.setInputProcessor(im);
	}
	
	@Override
	public void dispose() {
		universe.dispose();
	}
}
