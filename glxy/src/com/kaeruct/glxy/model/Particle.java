package com.kaeruct.glxy.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class Particle extends Circle {
	public float oldx;
	public float oldy;
	public double dx;
	public double dy;
	public float mass;
	public boolean dead;
	public boolean dragged;
	public Color color;

	private enum ParticleColor {
		SMALL(5, 0.6f, 0.8f, 0.8f, 1.0f), MEDIUM(35, 1.0f, 0.95f, 0.27f, 1.0f), LARGE(
				70, 1.0f, 0.35f, 0.27f, 1.0f), HUGE(120, 0.7f, 0.4f, 0.5f, 1.0f);

		private final float cutoff; // the cutoff radius for this color
		private final Color color;

		private ParticleColor(float cutoff, float r, float g, float b, float a) {
			this.cutoff = cutoff;
			this.color = new Color(r, g, b, a);
		}

		public Color lerp(Color target, float t) {
			return new Color(color.r + t * (target.r - this.color.r), color.g
					+ t * (target.g - this.color.g), color.b + t
					* (target.b - this.color.b), color.a + t
					* (target.a - this.color.a));
		}

		// get a color for a specific radius, interpolating if necessary
		public static Color get(float radius) {
			ParticleColor prev = null;
			for (ParticleColor c : ParticleColor.values()) {
				if (radius < c.cutoff) {
					if (prev == null) {
						return c.color;
					} else {
						return prev.lerp(c.color, (radius - prev.cutoff)
								/ c.cutoff);
					}
				}
				prev = c;
			}
			return HUGE.color;
		}
	}

	public Particle() {
		dx = 0;
		dy = 0;
		dead = false;
	}

	public Particle radius(float r) {
		radius = r;
		mass = (float) (0.5 * Math.pow(r, 3));
		color = ParticleColor.get(radius);
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

	public Particle(Particle p2) {
		dx = p2.dx;
		dy = p2.dy;
		x = p2.x;
		y = p2.y;
		radius(p2.radius);
		dead = false;
	}

	public void inc(float x, float y) {
		this.x += x;
		this.y += y;
	}

	// returns whether the particle is still alive
	public boolean update() {
		oldx = x;
		oldy = y;
		x += dx;
		y += dy;
		return !dead;
	}

	public void kill() {
		dead = true;
	}

	public boolean collidesWith(Particle p2) {
		float dx = p2.x - x, dy = p2.y - y;

		return Math.pow(dx, 2) + Math.pow(dy, 2) < Math.pow(radius + p2.radius,
				2);
	}
}
