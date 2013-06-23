package com.kaeruct.glxy.data;

import com.badlogic.gdx.Gdx;  
import com.badlogic.gdx.graphics.g2d.TextureAtlas;  
import com.badlogic.gdx.graphics.g2d.TextureRegion;  
  
public class ImageCache {  
   
    public static TextureAtlas atlas;  
      
    public static void load () {  
    	if (atlas == null) {
	        String textureFile = "data/textures.txt";  
	        atlas = new TextureAtlas(Gdx.files.internal(textureFile), Gdx.files.internal("data"));
    	}
    }  
      
    public static TextureRegion getTexture (String name) {  
        return atlas.findRegion(name);  
    }  
      
    public static TextureRegion getFrame (String name, int index) {  
        return atlas.findRegion(name, index);  
    }  
}  