package com.kaeruct.glxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kaeruct.glxy.GlxyGame;
import com.kaeruct.glxy.actor.SettingsDialog;
import com.kaeruct.glxy.actor.Universe;
import com.kaeruct.glxy.model.Settings.Setting;

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
		
		final Label l2 = new Label("Count: "+universe.getParticleCount(), skin);
		universe.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				l2.setText("Count: "+universe.getParticleCount());
			}
		});
		
		final SettingsDialog settingsDialog = new SettingsDialog(universe, skin, padX, padY);
		settingsDialog.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!universe.settings.get(Setting.TRAILS)) {
					universe.clearTrails();
				}
			}
		});
		final Texture settingsTexture = new Texture(Gdx.files.internal("data/gear.png"));
		final TextureRegion settingsImage = new TextureRegion(settingsTexture); 
		final ImageButton t4 = new ImageButton(new TextureRegionDrawable(settingsImage));
		t4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				settingsDialog.show(stage);
			}
		});
		
		final CheckBox b1 = new CheckBox("Pan", skin);
		b1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				universe.panning = b1.isChecked();
			}
		});
		b1.setChecked(true);
		b1.pad(padY, padX, padY, padX);
		
		// set up table layout
		table.add(universe).expand().fill().colspan(6).row();
		table.add(t2).pad(4);
		table.add(t1).pad(4);
		table.add(l1).pad(4);
		table.add(l2).pad(4);
		table.add(b1).right().expandX().pad(4);
		table.add(t4).right().pad(4);
		
		// set up input
		InputMultiplexer im = new InputMultiplexer(stage, universe.gestureDetector);
		Gdx.input.setInputProcessor(im);
	}
	
	@Override
	public void dispose() {
		universe.dispose();
	}
}
