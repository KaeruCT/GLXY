package com.kaeruct.glxy.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.tablelayout.Cell;
import com.kaeruct.glxy.GlxyGame;

public abstract class MenuScreen extends Screen {
    public MenuScreen(GlxyGame gg) {
        super(gg);
    }

    public Cell<?> add(Actor actor) {
        return add(actor, 200);
    }

    public Cell<?> add(Actor actor, float width) {
        Cell<?> c = table.add(actor).width(width);
        table.row();
        return c;
    }
}
