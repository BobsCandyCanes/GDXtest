package com.GDX.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

	class Splash extends Entity
	{
		int age;
		float splashAlpha = 1;
		
		public Splash(float x, float y)
		{
			this(x, y, 5, 5);
		}
		
		public Splash(float x, float y, float w, float h)
		{
			super(x, y, w, h);
			
			setTexture("splash");
		}

		@Override
		public void act(float deltaTime)
		{			
			if(splashAlpha >= 0.05)
			{
				splashAlpha -= 0.05;
			}

			age++;
			
			setWidth(getWidth() + 2);
			setHeight(getHeight() + 2);
			
			setX(getX() - 1);
			setY(getY() - 1);
			
			if(age > 30)
			{
				destroy();
			}
		}

		@Override
		public void draw(Batch batch, float alpha)
		{	
			Color color = getColor();
	        batch.setColor(color.r, color.g, color.b, splashAlpha);
			
	        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
	            getWidth(), getHeight(), getScaleX(), getScaleY(), -getRotation());
	        
	        batch.setColor(color.r, color.g, color.b, 1);
			
	        
			drawChildren(batch, alpha);
		}
	}