package com.kaeruct.glxy.actor;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kaeruct.glxy.model.TrailParticle;

public class TrailParticleManager {
	private ArrayList<TrailParticle> particles;
	private int maxParticles;
	
	public TrailParticleManager(int maxParticles) {
		this.maxParticles = maxParticles;
		particles = new ArrayList<TrailParticle>(maxParticles);
	}
	
	public void add (float x, float y, float radius, Color color) {
		TrailParticle p;
		int size = particles.size();
		if (size == maxParticles) {
			p = particles.remove(size - 1);
			p.set(x, y, radius, color);
		} else {
			p = new TrailParticle(x, y, radius, color);
		}
		particles.add(0, p);
	}
	
	public void render (ShapeRenderer sr) {
		for (TrailParticle p : particles) {
			sr.setColor(p.color);
			sr.filledCircle(p.x, p.y, p.radius);
		}
	}	
}