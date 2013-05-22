package com.kaeruct.glxy;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.kaeruct.glxy.Particle;

public class GlxyGame implements ApplicationListener {
	final int MULTITOUCH_COUNT = 10;
	
	final float minRadius = 8;
    final float maxRadius = 100;
	final float G = 0.5f; // gravity constant
	final float sG = G * 0.1f; // multiplier for slingshot
	
	OrthographicCamera camera;
	ShapeRenderer sr;
	Particle protoParticle;
	Array<Particle> particles;
	Vector3 initPos, touchPos;
	CameraController controller;
	GestureDetector gestureDetector;
	boolean addedParticle;
	
	class CameraController implements GestureListener {
		float velX, velY;
		boolean flinging = false;
		float initialScale = 1;

		public boolean touchDown (float x, float y, int pointer, int button) {
			flinging = false;
			initialScale = camera.zoom;
			return false;
		}

		@Override
		public boolean tap (float x, float y, int count, int button) {
			return true;
		}

		@Override
		public boolean longPress (float x, float y) {
			Gdx.app.log("GestureDetectorTest", "long press at " + x + ", " + y);
			return false;
		}

		@Override
		public boolean fling (float velocityX, float velocityY, int button) {
			flinging = true;
			velX = camera.zoom * velocityX * 0.5f;
			velY = camera.zoom * velocityY * 0.5f;
			return false;
		}

		@Override
		public boolean pan (float x, float y, float deltaX, float deltaY) {
			if (!protoParticle.dragged) {
				camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0);
			}
			
			return false;
		}

		@Override
		public boolean zoom (float originalDistance, float currentDistance) {
			float ratio = originalDistance / currentDistance;
			camera.zoom = initialScale * ratio;
			return false;
		}

		@Override
		public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
			return false;
		}

		public void update () {
			if (flinging) {
				velX *= 0.98f;
				velY *= 0.98f;
				camera.position.add(-velX * Gdx.graphics.getDeltaTime(), velY * Gdx.graphics.getDeltaTime(), 0);
				if (Math.abs(velX) < 0.01f) velX = 0;
				if (Math.abs(velY) < 0.01f) velY = 0;
			}
		}
	}

	@Override
	public void create() {
		addedParticle = true;
		particles = new Array<Particle>();
		sr = new ShapeRenderer();
		touchPos = new Vector3();
		initPos = new Vector3();
		
		protoParticle = (new Particle()).radius(minRadius);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, 0);
		
		controller = new CameraController();
		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, controller);
		Gdx.input.setInputProcessor(gestureDetector);
	}
 
	@Override
	public void dispose() {
		sr.dispose();
	}
 
	@Override
	public void render() {		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    camera.update();
	    
	    manageInput();
	    updateParticles();
	    renderParticles();
	}
	
	public void manageInput() {
		if (Gdx.input.isTouched(0) && !Gdx.input.isTouched(1)) { // only one finger is touching
			addedParticle = false;
			touchPos.set(Gdx.input.getX(0), Gdx.input.getY(0), 0);
			camera.unproject(touchPos);
			
			if (!protoParticle.dragged) {
				protoParticle.stop();
				protoParticle.dragged = true;
				initPos.set(touchPos);
			}
			
			protoParticle.position(touchPos);
			
			sr.begin(ShapeType.Line);
		    sr.setColor(Color.LIGHT_GRAY);
			sr.line(initPos.x, initPos.y, touchPos.x, touchPos.y);
			sr.end();
		} else if (!addedParticle) {
			protoParticle.dragged = false;
			protoParticle.vel(initPos.sub(touchPos).mul(sG));
			protoParticle.position(touchPos);
			addParticle();
		}
	}
	
	private void updateParticles() {
		for (Particle p: particles) {
			if (p.dead) continue;

			for (Particle p2 : particles) {
				if (p2.dead || p == p2) continue;
				
				float dx = p2.x - p.x;
	            float dy = p2.y - p.y;
	            float d = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	            if (d == 0) d = 1;
	            
	            if (p.collidesWith(p2)) {
	            	// collision
	                float mtd = 2*(p.radius + p2.radius - d)/d;
	
	                p2.x += dx * mtd / p2.radius;
	                p2.y += dy * mtd / p2.radius;
	                p.x -= dx * mtd / p.radius;
	                p.y -= dy * mtd / p.radius;
	                p2.dx += dx * mtd / p2.mass;
	                p2.dy += dy * mtd / p2.mass;
	                p.dx -= dx * mtd / p.mass;
	                p.dy -= dy * mtd / p.mass;
	            } else {
	            	// "gravity"
	            	float force = (float) (G * p.mass * p2.mass / Math.pow(d, 2));
	                float fscale = force / d;
	                p.dx += fscale * dx / p.mass;
	                p.dy += fscale * dy / p.mass;
	            }
			}
	    }
		
		Iterator<Particle> it = particles.iterator();
		while (it.hasNext()) {
			Particle p = it.next();
			if (!p.update()) {
				it.remove();
			}
		}
	}
	
	private void renderParticles() {
		sr.begin(ShapeType.FilledCircle);
	    sr.setColor(Color.WHITE);
	    sr.setProjectionMatrix(camera.combined);
		for (Particle p : particles) {
			sr.filledCircle(p.x, p.y, p.radius);
	    }
		sr.end();
	}
 
	private void addParticle() {
		Particle p = new Particle(protoParticle);
		particles.add(p);
		addedParticle = true;
	}
 
	@Override
	public void resize(int width, int height) {
	}
 
	@Override
	public void pause() {
	}
 
	@Override
	public void resume() {
	}
}
