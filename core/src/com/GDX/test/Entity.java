package com.GDX.test;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class Entity extends Group
{	
	TextureRegion region;

	float xVelocity;
	float yVelocity;

	float rotationSpeed;

	Polygon boundingPolygon;
	
	final static int DFLT_WIDTH = 10;
	final static int DFLT_HEIGHT = 10;

	public Entity()
	{	
		this(0, 0);
	}
	
	public Entity(float x, float y)
	{
		this(x, y, DFLT_WIDTH, DFLT_HEIGHT);
	}
	
	public Entity(float x, float y, float width, float height)
	{
		setWidth(width);
		setHeight(height);
		
		setX(x - getWidth() / 2);
		setY(y - getHeight() / 2);
		
		setTexture("projectile");
		
		setBounds(getX(), getY(), getWidth(), getHeight());
		
		setOrigin(getWidth() / 2, getHeight() / 2);
	}

	public float getVelocity()
	{
		return Math.abs(xVelocity) + Math.abs(yVelocity);
	}

	@Override
	public void setBounds(float x, float y, float w, float h)
	{
		super.setBounds(x, y, w, h);
		
		if(boundingPolygon == null)
		{
			initBoundingPolygon();
			
			/*
			boundingPolygon = new Polygon(new float[]{
					getX() - getWidth() / 2, getY() - getHeight() / 2,
					getX() - getWidth() / 2, getY() + getHeight() / 2, 
					getX() + getWidth() / 2, getY() + getHeight() / 2,
					getX() + getWidth() / 2, getY() - getHeight() / 2});
			boundingPolygon.setOrigin(getX(), getY());
			*/
		}
		
		boundingPolygon.setPosition(getX(), getY());
		boundingPolygon.setRotation(-getRotation());
	}

	public void initBoundingPolygon()
	{
		boundingPolygon = new Polygon(new float[]{
				0, 0,
				0, getHeight(),
				getWidth(), getHeight(),
				getWidth(), 0});
		boundingPolygon.setOrigin(getWidth() / 2, getHeight() / 2);
		
		boundingPolygon.setPosition(getX(), getY());
	}

	public void hitEntity(Entity e)
	{
	}
	
	public void destroy()
	{
		remove();
	}
	
	public void setTexture(String name)
	{
		region = GDXtest.getTexture(name);
		region.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
