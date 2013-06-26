package com.kaeruct.glxy.model;

public class StickyParticle extends Particle {
	@Override
	public boolean update() {
		return !dead;
	}
}