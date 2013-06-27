package com.kaeruct.glxy.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.Cell;
import com.kaeruct.glxy.model.Settings.Setting;

public class SettingsDialog extends Dialog {
	private final Universe universe;
	private final float padX;
	private final float padY;
	
	public SettingsDialog (final Universe universe, Skin skin, float padX, float padY) {
		super("", skin);
		this.universe = universe;
		this.padX = padX;
		this.padY = padY;
		
		final SettingsDialog dialog = this;
		
		setMovable(false);
		// make the dialog at least this wide
		getContentTable().add().width(Gdx.graphics.getWidth() * 0.5f).row();
		
		// add checkboxes
		addButton(Setting.PAUSED, skin);
		addButton(Setting.TRAILS, skin);
		addButton(Setting.COLLISION, skin);
		
		final Table buttonTable = new Table();
		
		// add reset zoom button
		final TextButton resetZoomButton = new TextButton("Reset Zoom", skin);
		resetZoomButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				universe.resetZoom();
			}
		});
		resetZoomButton.pad(padY, padX, padY, padX);
		buttonTable.add(resetZoomButton).pad(padY, padX, padY, padX);
		
		// add reset button
		final TextButton resetButton = new TextButton("Reset Particles", skin);
		resetButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				universe.clearParticles();
			}
		});
		resetButton.pad(padY, padX, padY, padX);
		buttonTable.add(resetButton).pad(padY, padX, padY, padX);
		
		getContentTable().row();
		getContentTable().add(buttonTable);
		
		// add close button
		final TextButton closeButton = new TextButton("Close", skin);
		closeButton.pad(padY, padX, padY, padX);
		closeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialog.hide();
			}
		});
		add(closeButton).right();
	}
	
	private Cell<?> add(Button b) {
		Table content = getContentTable();
		content.row().pad(padY, padX, padY, padX);
		return content.add(b).left();
	}
	
	public void addCheckbox(final Setting setting, Skin skin) {
		
		final CheckBox checkbox = new CheckBox("  "+setting.description, skin);
		checkbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				universe.settings.put(setting, ((CheckBox)actor).isChecked());
			}
		});
		
		checkbox.setChecked(universe.settings.get(setting));
		
		add(checkbox);
	}
	
	public void addButton(final Setting setting, Skin skin) {
		final TextButton button = new TextButton(setting.description, skin, "toggle");
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {	
				universe.settings.put(setting, button.isChecked());
			}
		});
		button.pad(padY, padX, padY, padX);
		
		button.setChecked(universe.settings.get(setting));
		add(button);
	}
}