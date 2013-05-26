package com.kaeruct.glxy.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kaeruct.glxy.model.Settings.Setting;

public class SettingsDialog extends Dialog {
	private final Universe universe;
	
	public SettingsDialog (Universe universe, Skin skin) {
		super("Settings", skin);
		this.universe = universe;
		
		getContentTable().setWidth(Gdx.graphics.getWidth() * 0.8f);
		getContentTable().pad(4);
		getButtonTable().pad(4);
		
		addCheckbox(Setting.TRAILS, skin);
		
		button("Done");
	}
	
	public void addCheckbox(final Setting setting, Skin skin) {
		Table content = getContentTable();
		final CheckBox c1 = new CheckBox(setting.description, skin);
		c1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				universe.settings.put(setting, c1.isChecked());
			}
		});
		content.row();
		content.add(c1).pad(4).left();
	}
}