package com.kaeruct.glxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Cell;
import com.kaeruct.glxy.GlxyGame;
import com.kaeruct.glxy.actor.Universe;

public class GameScreen extends Screen {
	private final Universe universe;
	
	public GameScreen (GlxyGame gg) {
		super(gg);
		final int padX = 20;
		final int padY = 10;
		
		universe = new Universe();
		
		// set up widgets
		float r = universe.decreaseParticleRadius();
		final Label l1 = new Label("Size: "+r, skin);
		final TextButton t1 = new TextButton("+", skin);
		t1.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		    	float r = universe.increaseParticleRadius();
		    	l1.setText("Size: "+r);
		    	return false;
		    }
		});
		t1.pad(padY, padX, padY, padX);
		
		final TextButton t2 = new TextButton("-", skin);
		
		t2.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		    	float r = universe.decreaseParticleRadius();
		    	l1.setText("Size: "+r);
				return false;
		    }
		});
		t2.pad(padY, padX, padY, padX);
		
		final CheckBox b1 = new CheckBox("Pan", skin);
		b1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				universe.panning = b1.isChecked();
			}
		});
		b1.pad(padY, padX, padY, padX);
		
		// set up table layout
		table.add(universe).expand().fill().colspan(4).row();
		table.add(t2).pad(4);
		table.add(t1).pad(4);
		table.add(l1).pad(4);
		table.add(b1).align(BaseTableLayout.RIGHT).expandX().pad(4);
		
		// set up input
		InputMultiplexer im = new InputMultiplexer(stage, universe.gestureDetector);
		Gdx.input.setInputProcessor(im);
	}
	
	@Override
	public void dispose() {
		universe.dispose();
	}
}
