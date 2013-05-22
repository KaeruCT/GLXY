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
		
		universe = new Universe();
		
		// set up widgets
		float r = universe.decreaseParticleRadius();
		final Label l1 = new Label("Size: "+r, skin);
		final TextButton t1 = new TextButton("+", skin);
		t1.addListener(new ClickListener() {
			@Override
		    public void clicked(InputEvent event, float x, float y) {
		    	float r = universe.increaseParticleRadius();
		    	l1.setText("Size: "+r);
		    }
		});
		t1.pad(5, 10, 5, 10);
		
		final TextButton t2 = new TextButton("-", skin);
		t2.addListener(new ClickListener() {
			@Override
		    public void clicked(InputEvent event, float x, float y) {
		    	float r = universe.decreaseParticleRadius();
		    	l1.setText("Size: "+r);
		    }
		});
		t2.pad(5, 10, 5, 10);
		
		final CheckBox b1 = new CheckBox("Pan", skin);
		b1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				universe.panning = b1.isChecked();
			}
		});
		b1.pad(5, 10, 5, 10);
		
		// set up table layout
		table.add(universe).fill().expand().colspan(4);
		table.row();
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
