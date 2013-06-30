package com.kaeruct.glxy.model;

import com.badlogic.gdx.utils.ArrayMap;

public class Settings extends ArrayMap<Settings.Setting, Boolean> {
	public enum Setting {
		TRAILS ("Show Particle Trails", "Don't Show Particle Trails", true),
		COLLISION ("Bounce Particles on Collision", "Join Particles on Collision", true),
		PAUSED ("Paused", "Not Paused", false);
		
		public final String descriptionOn;
		public final String descriptionOff;
		public final boolean defaultVal;
		
		Setting(String d1, String d2, boolean dv) {
			descriptionOn = d1;
			descriptionOff = d2;
			defaultVal = dv;
		}
	}
	
	public Settings() {
		for (Setting setting : Setting.values()) {
			put(setting, setting.defaultVal);
		}
	}

	public String getDescription(Setting setting) {
		return get(setting) ? setting.descriptionOn : setting.descriptionOff;
	}
}