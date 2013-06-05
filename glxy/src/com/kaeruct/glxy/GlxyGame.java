package com.kaeruct.glxy;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.ArrayMap;
import com.kaeruct.glxy.screen.Screen;

public class GlxyGame implements ApplicationListener {
	private Screen screen;
	protected ArrayMap<String, Screen> screens;

	@Override
	public void create() {
		Dialog.fadeDuration = 0.01f;
        screens = new ArrayMap<String, Screen>();
        setScreen("MainMenuScreen");
	}

	public void setScreen(String name) {
		Screen newScreen = null;
		name = "com.kaeruct.glxy.screen." + name;  
		
		if (!screens.containsKey(name)) {
			try {
				Class<?> screenClass =  Class.forName(name);   
	            Constructor<?> constructor = screenClass.getConstructor(GlxyGame.class);      
				newScreen = (Screen) constructor.newInstance(this);
			} catch (Exception e) {
				e.printStackTrace();
			}  
            screens.put(name, newScreen); 
		} else {
			newScreen = screens.get(name);
		}
		screen = newScreen;
	}

	@Override
	public void dispose() {
		if (screen != null) screen.dispose();
	}

	@Override
	public void render() {		
		if (screen != null) {
			screen.render(Gdx.graphics.getDeltaTime());
		} else {
			Gdx.gl.glClearColor(0, 0, 0, 1);  
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);  
		}
	}

	@Override
	public void resize(int width, int height) {
		if (screen != null) screen.resize(width, height);
	}

	@Override
	public void pause() {
		if (screen != null) screen.pause();  
	}

	@Override
	public void resume() {
		if (screen != null) screen.resume();
	}

}
