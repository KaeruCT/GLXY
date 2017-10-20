package com.kaeruct.glxy;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "glxy";
        cfg.useGL20 = true;
        cfg.width = 720;
        cfg.height = 480;

        new LwjglApplication(new GlxyGame(), cfg);
    }
}
