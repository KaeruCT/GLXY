package com.kaeruct.glxy.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kaeruct.glxy.model.Settings.Setting;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class SettingsDialog extends Dialog {
    private final Universe universe;
    private final float padX;
    private final float padY;
    private boolean isShowing;

    public SettingsDialog(final Universe universe, Skin skin, float padX,
            float padY) {
        super("", skin);
        this.universe = universe;
        this.padX = padX;
        this.padY = padY;
        this.isShowing = false;

        final SettingsDialog dialog = this;

        setMovable(false);

        final Label toggleTitle = new Label("Toggleable", skin);
        toggleTitle.setAlignment(Align.left);
        toggleTitle.setFontScale(3);

        // make the dialog at least this wide
        getContentTable().add(toggleTitle).left()
                .width(Gdx.graphics.getWidth() * 0.7f).row();

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
        buttonTable.add(resetZoomButton).left();

        // add reset button
        final TextButton resetButton = new TextButton("Reset Particles", skin);
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                universe.clearParticles();
            }
        });
        resetButton.pad(padY, padX, padY, padX);
        buttonTable.add(resetButton).pad(padY, padX, padY, padX).left();

        buttonTable.setTransform(true);
        buttonTable.setScale(2f);

        getContentTable().row();
        final Label buttonTitle = new Label("Reset", skin);
        buttonTitle.setFontScale(3);
        buttonTitle.setAlignment(Align.left);
        getContentTable().add(buttonTitle).pad(0, 0, 20, 0).left().row();
        getContentTable().add(buttonTable).pad(0, 10, 0, 0).left().pad(padY, padX, padY, padX);

        // add close button
        final TextButton closeButton = new TextButton("Close", skin);
        closeButton.pad(padY, padX, padY, padX);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });
        closeButton.setTransform(true);
        closeButton.setScale(2f);
        add(closeButton).right().pad(0, 0, 30, 100);
    }

    private Cell<?> add(Button b) {
        Table content = getContentTable();
        content.row().pad(padY, padX, padY, padX);
        return content.add(b).left();
    }

    public void addButton(final Setting setting, Skin skin) {
        final TextButton button = new TextButton(
                universe.settings.getDescription(setting), skin, "toggle");
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                universe.settings.put(setting, button.isChecked());
                button.setText(universe.settings.getDescription(setting));
            }
        });
        button.pad(padY, padX, padY, padX);

        button.setChecked(universe.settings.get(setting));

        button.setTransform(true);
        button.setScale(2f);

        add(button).pad(30, 20, 30, 0);
    }

    @Override
    public Dialog show(Stage stage) {
        show(stage, null);
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        this.isShowing = true;
        universe.inMenu = true;
        return this;
    }

    @Override
    public void hide() {
        hide(null);
        this.isShowing = false;
        universe.inMenu = false;
    }

    public boolean isShowing() {
        return isShowing;
    }
}