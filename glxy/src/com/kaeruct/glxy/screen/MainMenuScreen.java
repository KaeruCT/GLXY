package com.kaeruct.glxy.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kaeruct.glxy.GlxyGame;

public class MainMenuScreen extends MenuScreen {
	
	public MainMenuScreen (GlxyGame gm) {
		super(gm);
		
		TextButton t = new TextButton("Start!", skin);
		t.addListener(new ClickListener() {
		    public void clicked(InputEvent event, float x, float y) {
		    	game.setScreen("GameScreen");
		    }
		});
		add(t);
	}

	@Override
	public void render (float delta) {
		super.render(delta);
	}
}
