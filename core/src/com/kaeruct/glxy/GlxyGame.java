package com.kaeruct.glxy;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ArrayMap;
import com.kaeruct.glxy.screen.GameScreen;
import com.kaeruct.glxy.screen.MainMenuScreen;
import com.kaeruct.glxy.screen.Screen;

public class GlxyGame implements ApplicationListener {
    private Screen screen;
    protected ArrayMap<String, Screen> screens;

    @Override
    public void create() {
        Gdx.input.setCatchMenuKey(true);
        Gdx.input.setCatchBackKey(true);
        screens = new ArrayMap<>();
        setScreen("MainMenuScreen");
    }

    public void setScreen(String name) {
        Screen newScreen = null;
        if (screen != null)
            screen.hide();

        if (!screens.containsKey(name)) {
            switch (name) {
                case "GameScreen":
                    newScreen = new GameScreen(this);
                    break;
                case "MainMenuScreen":
                    newScreen = new MainMenuScreen(this);
                    break;
            }
            screens.put(name, newScreen);
        } else {
            newScreen = screens.get(name);
        }
        screen = newScreen;
        screen.show();
    }

    @Override
    public void dispose() {
        if (screen != null)
            screen.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (screen != null) {
            screen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void resize(int width, int height) {
        if (screen != null)
            screen.resize(width, height);
    }

    @Override
    public void pause() {
        if (screen != null)
            screen.pause();
    }

    @Override
    public void resume() {
        if (screen != null)
            screen.resume();
    }

}
