package com.kaeruct.glxy.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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
		t.pad(5, 10, 5, 10);
		add(t).expandY();

		Label cpright = new Label("Copyright CHEKAE 2013 - All rights reserved", skin);
		cpright.setAlignment(Align.center);
		cpright.setColor(1f, 1f, .8f, 1f);
		add(cpright).pad(10);
	}

	@Override
	public void render (float delta) {
		super.render(delta);
		Table.drawDebug(stage);
	}
}
