package com.GDX.test;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Projectile extends Entity
{	
	final static int DFLT_WIDTH = 5;
	final static int DFLT_HEIGHT = 5;

	int speed = 7;
	int range = 6;
	int damage = 10;
	int age;
	int lifespan;

	Ship sourceShip;

	PointLight light;

	public Projectile(float x, float y, float angle, Ship sourceShip)
	{		
		super(x, y, DFLT_WIDTH, DFLT_HEIGHT);

		setTexture("projectile");

		setRotation(angle);

		// Calculate velocity based on firing angle
		float vX = speed * (float)Math.sin(Math.toRadians(getRotation()));
		float vY = speed * (float)Math.cos(Math.toRadians(getRotation()));

		this.sourceShip = sourceShip;

		vX += sourceShip.xVelocity;
		vY += sourceShip.yVelocity;

		xVelocity = vX;
		yVelocity = vY;

		calcLifespan();

		initLight();
	}

	public void initLight()
	{
		light = new PointLight(GDXtest.rayHandler, 100, new Color(1.0f, 0.1f, 0.1f, 0.8f), 20, 0, 0);	
	}

	@Override
	public void act(float deltaTime)
	{
		age++;

		moveBy(xVelocity, yVelocity);

		setBounds(getX(), getY(), getWidth(), getHeight());

		checkForCollision();

		updateLights();

		if(age > lifespan)
		{
			GDXtest.stage.addActor(new Splash(getX(), getY()));	

			destroy();
		}
	}

	public void updateLights()
	{
		Camera camera = GDXtest.stage.getCamera();

		Vector3 lightLocation = camera.project(new Vector3(getX() + getOriginX(), getY() + getOriginY(), 0)
		, GDXtest.stage.getViewport().getScreenX(), GDXtest.stage.getViewport().getScreenY(), camera.viewportWidth, camera.viewportHeight);

		light.setPosition(lightLocation.x, lightLocation.y);
	}

	public void checkForCollision()
	{
		for(Actor a : getStage().getActors())
		{
			if(a instanceof Ship)
			{
				Ship ship = (Ship)a;

				if(!ship.equals(sourceShip))
				{
					if(Intersector.overlapConvexPolygons(ship.boundingPolygon, this.boundingPolygon))
					{
						ship.takeDamage(damage);
						GDXtest.stage.addActor(new Explosion(getX(), getY()));	
						destroy();
					}
				}
			}
		}
	}

	@Override
	public void draw(Batch batch, float alpha)
	{
		batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(), -getRotation());

		drawChildren(batch, alpha);
	}

	public void calcLifespan()
	{
		int scalarVelocity = Math.abs((int)xVelocity) + Math.abs((int)yVelocity);

		lifespan = range * scalarVelocity;
	}

	public void destroy()
	{
		light.setActive(false);
		light.remove();
		light.dispose();
		this.remove();
	}
}
