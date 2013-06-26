package com.kaeruct.glxy.model;

public class StickyParticle extends Particle {
	@Override
	public boolean update() {
		return !dead;
	}
	
	@Override
	public void inc (float x, float y) {
		// do nothing
	}
}