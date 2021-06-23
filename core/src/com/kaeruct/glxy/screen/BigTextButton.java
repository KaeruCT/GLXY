package com.kaeruct.glxy.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class BigTextButton extends TextButton {
    public BigTextButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        scale();
    }

    public BigTextButton(String text, Skin skin) {
        super(text, skin);
        scale();
    }

    private void scale() {
        setTransform(true);
        scaleBy(1.5f);
    }
}
