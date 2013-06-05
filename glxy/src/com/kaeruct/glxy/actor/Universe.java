package com.kaeruct.glxy.actor;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.kaeruct.glxy.model.Particle;
import com.kaeruct.glxy.model.Settings;
import com.kaeruct.glxy.model.Settings.Setting;

public class Universe extends Actor {
	final OrthographicCamera camera;
	final ShapeRenderer sr;
	final Particle protoParticle;
	final Array<Particle> particles;
	final TrailParticleManager trailParticles;
	final Vector3 initPos, touchPos, cinitPos, ctouchPos;
	final CameraController controller;
	final public GestureDetector gestureDetector;
	public Settings settings;
	boolean addedParticle;
	public boolean panning;

	final float minRadius = 5;
    final float maxRadius = 100;
	final float G = 0.09f; // gravity constant
	final float sG = G * 0.5f; // multiplier for slingshot
	final int maxTrails = 500; // max trails for a particle

	private enum ParticleColor {
		SMALL (5, 0.6f, 0.8f, 0.8f, 1.0f),
		MEDIUM (35, 1.0f, 0.95f, 0.27f, 1.0f),
		LARGE (70, 1.0f, 0.35f, 0.27f, 1.0f),
		HUGE (100, 0.24f, 0.1f, 0.27f, 1.0f);

		private final float cutoff; // the cutoff radius for this color
		private final Color color;
		private ParticleColor (float cutoff, float r, float g, float b, float a) {
			this.cutoff = cutoff;
			this.color = new Color(r, g, b, a);
		}

		public Color lerp(Color target, float t) {
			return new Color (
				this.color.r + t * (target.r - this.color.r),
				this.color.g + t * (target.g - this.color.g),
				this.color.b + t * (target.b - this.color.b),
				this.color.a + t * (target.a - this.color.a)
			);
		}

		// get a color for a specific radius, interpolating if necessary
		public static Color get(float radius) {
			ParticleColor prev = null;
			for (ParticleColor c: ParticleColor.values()) {
				if (radius < c.cutoff) {
					if (prev == null) {
						return c.color;
					} else {
						return prev.lerp(c.color, (radius - prev.cutoff) / c.cutoff);
					}
				}
				prev = c;
			}
			return HUGE.color;
		}
	}

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
			touchPos.set(x, y, 0);
			initPos.set(0, 0, 0); // just to avoid instantiating a new vector
			camera.unproject(touchPos);
			protoParticle.dragged = false;
			protoParticle.vel(initPos);
			protoParticle.position(touchPos);
			addParticle();
			return true;
		}

		@Override
		public boolean longPress (float x, float y) {
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
			if (panning) {
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

	public Universe (Settings settings) {
		addedParticle = true;
		panning = false;
		particles = new Array<Particle>();
		sr = new ShapeRenderer();
		touchPos = new Vector3();
		initPos = new Vector3();
		ctouchPos = new Vector3();
		cinitPos = new Vector3();

		protoParticle = (new Particle()).radius(minRadius);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, 0);
		controller = new CameraController();
		gestureDetector = new GestureDetector(20, 0.5f, 0.5f, 0.15f, controller);

		trailParticles = new TrailParticleManager(maxTrails);
		
		this.settings = settings;
	}

	public Universe () {
		this(new Settings());
	}

	public float increaseParticleRadius() {
		protoParticle.radius(Math.min(maxRadius, protoParticle.radius+5));
		return protoParticle.radius;
	}

	public float decreaseParticleRadius() {
		protoParticle.radius(Math.max(minRadius, protoParticle.radius-5));
		return protoParticle.radius;
	}

	@Override
	public void act(float delta) {
	    manageInput();
	    updateParticles();
	}

	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		camera.update();
		batch.end();

		// fill background
		sr.begin(ShapeType.FilledRectangle);
		sr.setColor(0.1f, 0.1f, 0.2f, 1);
		sr.filledRect(getX(), getY(), getWidth(), getHeight());
		sr.end();

	    if (!panning && protoParticle.dragged) {
	    	// draw "slingshot" line
	    	sr.begin(ShapeType.Line);
		    sr.setColor(Color.LIGHT_GRAY);

			sr.line(cinitPos.x,
					getY()+getHeight()-cinitPos.y,
					ctouchPos.x,
					getY()+getHeight()-ctouchPos.y);
			sr.end();
	    }
	    
	    // draw particles and trails
	    renderParticles();

	    // draw black bar on the bottom
	    sr.setProjectionMatrix(batch.getProjectionMatrix());
	    sr.setTransformMatrix(batch.getTransformMatrix());
	    sr.begin(ShapeType.FilledRectangle);
	    sr.setColor(0.1f, 0.1f, 0.1f, 1);
	    sr.filledRect(0, 0, getWidth(), Gdx.graphics.getHeight()-getHeight());
	    sr.end();
	    batch.begin();
	}

	public void manageInput() {
		if (panning) return;

		if (Gdx.input.isTouched(0) && !Gdx.input.isTouched(1) && !Gdx.input.justTouched()) { // only one finger is touching
			touchPos.set(Gdx.input.getX(0), Gdx.input.getY(0), 0);
			ctouchPos.set(touchPos);

			if (null == this.hit(touchPos.x, touchPos.y, false)) {
				addedParticle = true;
				protoParticle.dragged = false;
				return;
			}

			camera.unproject(touchPos);
			addedParticle = false;

			if (!protoParticle.dragged) {
				protoParticle.stop();
				protoParticle.dragged = true;
				initPos.set(touchPos);
				cinitPos.set(ctouchPos);
			}

			protoParticle.position(touchPos);
		} else if (!addedParticle) {
			protoParticle.dragged = false;
			protoParticle.vel(initPos.sub(touchPos).mul(sG));
			protoParticle.position(touchPos);
			addParticle();
		}
	}

	private void updateParticles() {
		Iterator<Particle> it;
		for (int i = 0; i < particles.size; i++) {
			Particle p = particles.get(i);
			if (p.dead) continue;

			for (int j = 0; j < particles.size; j++) {
				Particle p2 = particles.get(j);
				if (p2.dead || i == j) continue;

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

		it = particles.iterator();
		while (it.hasNext()) {
			Particle p = it.next();
			if (!p.update()) {
				it.remove();
			}
		}	
	}

	private void renderParticles() {
		Color c;

		sr.begin(ShapeType.FilledCircle);
	    sr.setProjectionMatrix(camera.combined);
		for (Particle p : particles) {
			c = ParticleColor.get(p.radius);
			if (settings.get(Setting.TRAILS)) {
				if (Math.abs(p.x - p.oldx) > 0.2 || Math.abs(p.y - p.oldy) > 0.2) {
					trailParticles.add(p.x, p.y, p.radius, c);
				}
			}

			sr.setColor(c);
			sr.filledCircle(p.x, p.y, p.radius);
	    }
		
		if (settings.get(Setting.TRAILS)) {
			trailParticles.render(sr);
		}
		
		sr.end();
	}

	private void addParticle() {
		Particle p = new Particle(protoParticle);
		particles.add(p);
		addedParticle = true;

		this.fire(new ChangeEvent());
	}

	public void clearParticles() {
		particles.clear();
		
		// TODO: reset trailParticles

		this.fire(new ChangeEvent());
	}

	public int getParticleCount() {
		return particles.size;
	}

	public void dispose() {
		sr.dispose();
	}
}
