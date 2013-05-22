package com.kaeruct.glxy.model;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.kaeruct.glxy.model.Particle;

public class Particle extends Circle {
	public double dx;
	public double dy;
	public float mass;
	public boolean dead;
	public boolean dragged;
	
	public Particle () {
		dx = 0;
		dy = 0;
		dead = false;
	}
	
	public Particle radius(float r) {
		radius = r;
		mass = (float) (0.5 * Math.pow(r, 3));
		return this;
	}
	
	public Particle position(Vector3 pos) {
		x = pos.x;
		y = pos.y;
		return this;
	}
	
	public Particle stop() {
		dx = 0;
		dy = 0;
		return this;
	}
	
	public Particle vel(Vector3 d) {
		dx = d.x;
		dy = d.y;
		return this;
	}

	public Particle (Particle p2) {
		dx = p2.dx;
		dy = p2.dy;
		x = p2.x;
		y = p2.y;
		radius(p2.radius);
		dead = false;
	}
	
	// returns wether the particle is still alive
	public boolean update() {
		x += dx;
		y += dy;
    	return !dead;
	}
	
	public void kill() {
		dead = true;
	}
	
	public boolean collidesWith(Particle p2) {
		float dx = p2.x - x,
			  dy = p2.y - y;

		return Math.pow(dx, 2) + Math.pow(dy, 2) < Math.pow(radius + p2.radius, 2);
	}
}
