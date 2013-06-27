package com.kaeruct.glxy.actor;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kaeruct.glxy.data.ImageCache;
import com.kaeruct.glxy.model.Particle;
import com.kaeruct.glxy.model.TrailParticle;

public class TrailParticleManager {
	private ArrayList<TrailParticle> particles;
	private int maxParticles;
	private final Texture texture;
	
	public TrailParticleManager(int maxParticles, Texture texture) {
		this.maxParticles = maxParticles;
		this.texture = texture;
		particles = new ArrayList<TrailParticle>(maxParticles);
	}
	
	public void add (Particle particle) {
		if (Math.abs(particle.x - particle.oldx) > 0.2
						|| Math.abs(particle.y - particle.oldy) > 0.2) {
			TrailParticle p;
			int size = particles.size();
			if (size == maxParticles) {
				p = particles.remove(size - 1);
				p.set(particle);
			} else {
				p = new TrailParticle(particle);
			}
			particles.add(0, p);
		}
	}
	
	public void render (SpriteBatch batch, boolean paused) {
		Iterator<TrailParticle> tpi = particles.iterator();
		while (tpi.hasNext()) {
			TrailParticle p = tpi.next();
			
			batch.setColor(p.color);
			batch.draw(texture, p.x-p.radius, p.y-p.radius, p.radius*2, p.radius*2);
			
			if (!paused) {
				p.shrink();
				
				if (p.radius <= 1) {
					tpi.remove();
				}
			}
		}
	}
	
	public void clear () {
		particles.clear();
	}
}