package com.kaeruct.glxy.model;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.graphics.Color;

public class TrailParticle extends Circle {
	public Color color;
	private float rsqrt;
	
	public TrailParticle (Particle p) {
		set(p);
	}
	
	public void set(Particle p) {
		this.x = p.x;
		this.y = p.y;
		this.radius = p.radius;
		this.color = p.color;
		rsqrt = (float) Math.sqrt(radius);
	}
	
	public void set(float x, float y, float radius, Color color) {
		set(x, y, radius);
		this.color = color;
	}

	public void shrink() {
		radius -= rsqrt * 0.2f;
	}
}