package com.kaeruct.glxy.model;

import com.badlogic.gdx.utils.ArrayMap;

public class Settings extends ArrayMap<String, Boolean> {
	public Boolean get (String key, Boolean dVal) {
		if (containsKey(key)) {
			return get(key);
		}
		return dVal;
	}
}