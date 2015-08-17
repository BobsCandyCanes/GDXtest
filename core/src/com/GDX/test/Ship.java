package com.GDX.test;

import java.util.ArrayList;
import java.util.List;

import box2dLight.ConeLight;
import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ship extends Entity
{		
	final static int DFLT_WIDTH = 30;
	final static int DFLT_HEIGHT = 90;

	int maxHealth;

	float speed;
	float turnSpeed;

	int timeSinceLastShot;

	int health;

	boolean isColliding;

	public List<Turret> turrets = new ArrayList<Turret>();

	ParticleEffect fire;
	ParticleEffect wake;

	PointLight mainLight;
	ConeLight spotlight;

	public Ship(int x, int y, String name)
	{		
		loadFromFile(name);

		setX(x - getWidth() / 2);
		setY(y - getHeight() / 2);

		setBounds(getX(), getY(), getWidth(), getHeight());

		setOrigin(getWidth() / 2, getHeight() / 2);

		initBoundingPolygon();

		health = maxHealth;

		initParticles();
		initLights();
	}

	public void loadFromFile(String filename)
	{
		FileHandle file = Gdx.files.internal("data/Ships/" + filename + ".ship");
		String inputString = file.readString();
		String[] input = inputString.split("\\r?\\n");

		setTexture(input[1]);
		setWidth(Integer.parseInt(input[3]));
		setHeight(Integer.parseInt(input[5]));
		maxHealth = Integer.parseInt(input[7]);
		speed = Float.parseFloat(input[9]);
		turnSpeed = Float.parseFloat(input[11]);

		int numTurrets = Integer.parseInt(input[14]);
		int lineNumber = 14;

		String turretType;
		int turretX;
		int turretY;

		for(int i = 0; i < numTurrets; i++)
		{
			turretType = input[lineNumber + 2];
			turretX = Integer.parseInt(input[lineNumber + 4]);
			turretY = Integer.parseInt(input[lineNumber + 6]);

			addTurret(new Turret(this, turretX, turretY, turretType));

			lineNumber += 6;
		}
	}

	public void initParticles()
	{
		fire = new ParticleEffect();
		fire.load(Gdx.files.internal("data/Particles/Smoke"), Gdx.files.internal(""));

		wake = new ParticleEffect();
		wake.load(Gdx.files.internal("data/Particles/Wake"), Gdx.files.internal(""));	
	}

	public void initLights()
	{
		mainLight = new PointLight(GDXtest.rayHandler, 100, new Color(0.3f, 0.1f, 0.3f, 0.5f), 200, 0, 0);	

		//Rayhandler, rays, color, distance, x, y, direction, coneWidth
		spotlight = new ConeLight(GDXtest.rayHandler, 100,  new Color(0.3f, 0.1f, 0.3f, 0.8f), 600, 0, 0, 90, 15);	
	}

	@Override
	public void act(float deltaTime)
	{
		timeSinceLastShot++;

		moveBy(xVelocity, yVelocity);

		xVelocity *= 0.95;
		yVelocity *= 0.95;

		rotateBy(rotationSpeed);

		rotationSpeed *= 0.75;

		setBounds(getX(), getY(), getWidth(), getHeight());

		isColliding = false;
		checkForCollision();

		for(Actor a : getChildren())
		{
			a.act(deltaTime);
		}

		updateParticles(deltaTime);

		updateLights();

		if(health <= 0)
		{
			destroy();
		}

		//shoot();
	}

	public void updateParticles(float deltaTime)
	{
		fire.getEmitters().first().setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);

		fire.update(deltaTime);

		if(fire.isComplete())
		{
			fire.reset();
		}

		wake.getEmitters().first().setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);

		wake.update(deltaTime);

		if(wake.isComplete())
		{
			wake.reset();
		}
	}

	public void updateLights()
	{
		Camera camera = GDXtest.stage.getCamera();

		Vector3 lightLocation = camera.project(new Vector3(getX() + getOriginX(), getY() + getOriginY(), 0)
		, GDXtest.stage.getViewport().getScreenX(), GDXtest.stage.getViewport().getScreenY(), camera.viewportWidth, camera.viewportHeight);

		mainLight.setPosition(lightLocation.x, lightLocation.y);

		spotlight.setPosition(lightLocation.x, lightLocation.y);

		if(isPlayer())
		{
			spotlight.setDirection(getDirectionToMouse());
		}
		else
		{
			spotlight.setDirection(-getRotation() + 90);
		}
	}

	@Override
	public void hitEntity(Entity e)
	{
		if(e instanceof Ship)
		{
			xVelocity *= -0.02;
			yVelocity *= -0.02;
		}
	}

	@Override
	public void draw(Batch batch, float alpha)
	{	
		wake.draw(batch);

		batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(), -getRotation());

		drawChildren(batch, alpha);

		if(health <= maxHealth / 2)
		{
			fire.draw(batch);
		}
	}

	public void shoot()
	{
		for(Turret t: turrets)
		{
			t.shoot();
		}
	}

	public void addTurret(Turret t)
	{
		addActor(t);
		turrets.add(t);
	}

	public void takeDamage(float damage)
	{
		health -= damage;

		if(health <= 0)
		{
			destroy();
		}
	}

	public void checkForCollision()
	{
		Ship player = GDXtest.getPlayer();
		
		if(this.equals(player))
		{
			checkForCollisionWithTerrain();
		}
		else
		{
			if(player != null)
			{
				if(Intersector.overlapConvexPolygons(player.boundingPolygon, this.boundingPolygon))
				{
					player.isColliding = true;

					player.moveBy(-player.xVelocity * 1.2f, -player.yVelocity * 1.2f);
					player.rotateBy(-player.rotationSpeed);

					player.xVelocity = 0;
					player.yVelocity = 0;

					player.takeDamage(1);

					player = null;
				}
			}
		}
	}

	public void checkForCollisionWithTerrain()
	{
		int objectLayerId = 2;

		Vector2 endpoint1 = new Vector2();
		Vector2 endpoint2 = new Vector2();

		float[] vertices;

		TiledMap tiledMap = Background.tiledMap;

		MapLayer collisionObjectLayer = tiledMap.getLayers().get(objectLayerId);
		MapObjects objects = collisionObjectLayer.getObjects();

		// Uses polylines instead of polygons for better performance
		for (PolylineMapObject polylineObject : objects.getByType(PolylineMapObject.class)) 
		{
			Polyline polyline = polylineObject.getPolyline();

			vertices = polyline.getTransformedVertices();

			endpoint1.set(vertices[0], vertices[1]);
			endpoint2.set(vertices[2], vertices[3]);

			if (Intersector.intersectSegmentPolygon(endpoint1, endpoint2, this.boundingPolygon))
			{
				isColliding = true;

				moveBy(-xVelocity * 1.4f, -yVelocity * 1.4f);
				rotateBy(-rotationSpeed);

				xVelocity *= 0.05;
				yVelocity *= 0.05;
			}
		}

		endpoint1 = null;
		endpoint2 = null;
		vertices = null;
	}

	public float getDirectionToMouse()
	{
		Vector2 mouseLocation = GDXtest.stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

		double xDiff = mouseLocation.x - (getX() + getOriginX());
		double yDiff = mouseLocation.y - (getY() + getOriginY());

		return (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
	}

	public boolean isPlayer()
	{
		return this.equals(GDXtest.getPlayer());
	}

	@Override
	public void destroy()
	{
		mainLight.remove();
		mainLight.dispose();
		spotlight.remove();
		spotlight.dispose();

		if(this.equals(GDXtest.getPlayer()))
		{
			GDXtest.playerNum = 0;
		}
		
		GDXtest.stage.addActor(new Splash(getX() + getWidth() / 2, getY() + getHeight() / 2, 20, 20));	

		this.remove();
	}
}