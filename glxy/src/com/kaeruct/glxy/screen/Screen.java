package com.kaeruct.glxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kaeruct.glxy.GlxyGame;

public abstract class Screen implements com.badlogic.gdx.Screen {
	protected Stage stage;
	protected GlxyGame game;
	protected Table table;
	protected Skin skin;

	public Screen (GlxyGame gm) {
		game = gm;
		stage = new Stage();
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		table = new Table();
		
        table.setFillParent(true);
        stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
		stage.setViewport(width, height, true);
	}

	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}
