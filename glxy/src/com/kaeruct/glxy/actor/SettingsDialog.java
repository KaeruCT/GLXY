package com.kaeruct.glxy.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SettingsDialog extends Dialog {
	private final Universe universe;
	
	public SettingsDialog (Universe universe, Skin skin) {
		super("Settings", skin);
		this.universe = universe;
		
		getContentTable().pad(4);
		getButtonTable().pad(4);
		
		button("Done");
	}
	
	public void addCheckbox(final String setting, Skin skin) {
		Table content = getContentTable();
		final CheckBox c1 = new CheckBox(setting, skin);
		c1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				universe.setSetting(setting, c1.isChecked());
			}
		});
		content.row();
		content.add(c1).pad(4).left();
	}
}