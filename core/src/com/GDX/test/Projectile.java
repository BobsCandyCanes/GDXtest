package com.GDX.test;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;

public class Projectile extends Entity
{	
	final static int DFLT_WIDTH = 5;
	final static int DFLT_HEIGHT = 5;

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
		float vX = 6 * (float)Math.sin(Math.toRadians(getRotation()));
		float vY = 6 * (float)Math.cos(Math.toRadians(getRotation()));

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
		
		if(age > lifespan)
		{
			GDXtest.stage.addActor(new Splash(getX(), getY()));	
			
			destroy();
		}
		
		moveBy(xVelocity, yVelocity);

		setBounds(getX(), getY(), getWidth(), getHeight());
		
		if(age >= 5)
		{
			checkForCollision();
		}
		
		updateLights();
	}
	
	public void updateLights()
	{
		Camera camera = GDXtest.stage.getCamera();
		
		Vector3 lightLocation = camera.project(new Vector3(getX() + getOriginX(), getY() + getOriginY(), 0)
		, GDXtest.stage.getViewport().getScreenX(), GDXtest.stage.getViewport().getScreenY(), camera.viewportWidth, camera.viewportHeight);
	
		light.setPosition(lightLocation.x, lightLocation.y);
	}

	@Override
	public void hitEntity(Entity e)
	{
		/*
		destroy();

		if(e instanceof Ship)
		{
			Ship s = (Ship)e;
			s.takeDamage(damage);
		}
		*/
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
		light.remove();
		light.dispose();
		super.destroy();
	}
}
